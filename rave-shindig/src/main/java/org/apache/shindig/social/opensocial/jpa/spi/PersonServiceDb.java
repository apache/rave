/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.opensocial.jpa.spi;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.rave.os.DatabasePopulateContextListener;
import org.apache.rave.os.ShindigUtil;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.protocol.conversion.BeanConverter;
import org.apache.shindig.social.opensocial.jpa.PersonDb;
import org.apache.shindig.social.opensocial.jpa.api.FilterCapability;
import org.apache.shindig.social.opensocial.jpa.api.FilterSpecification;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.PersonService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.json.JSONObject;

/**
 * Implements the PersonService from the SPI binding to the JPA model and providing queries to
 * support the OpenSocial implementation.
 */
public class PersonServiceDb implements PersonService {

  /**
   * This is the JPA entity manager, shared by all threads accessing this service (need to check
   * that its really thread safe).
   */
  private EntityManager entityManager;

  /**
   * Create the PersonServiceDb, injecting an entity manager that is configured with the social
   * model.
   *
   * @param entityManager the entity manager containing the social model.
   */
//  @Inject
  public PersonServiceDb(EntityManager entityManager) {
    this.entityManager = entityManager;
  }
  
  public PersonServiceDb() {
      this.entityManager = DatabasePopulateContextListener.getEntityManager();
  }

  /**
   * {@inheritDoc}
   */
  public Future<RestfulCollection<Person>> getPeople(Set<UserId> userIds,
       GroupId groupId, CollectionOptions collectionOptions, Set<String> fields,
       SecurityToken token) throws ProtocolException {
    // for each user id get the filtered userid using the token and then, get the users identified
    // by the group id, the final set is filtered
    // using the collectionOptions and return the fields requested.

    // not dealing with the collection options at the moment, and not the fields because they are
    // either lazy or at no extra costs, the consumer will either access the properties or not
    List<Person> plist = null;
    int lastPos = 1;
    Long totalResults = null;

    StringBuilder sb = new StringBuilder();
    // sanitize the list to get the uid's and remove duplicates
    List<String> paramList = SPIUtils.getUserList(userIds, token);
    // select the group Id as this will drive the query
    switch (groupId.getType()) {
    case all:
      // select all contacts
      sb.append(PersonDb.JPQL_FINDALLPERSON);
      lastPos = JPQLUtils.addInClause(sb, "p", "id", lastPos, paramList.size());
      break;
    case friends:
      // select all friends (subset of contacts)
      sb.append(PersonDb.JPQL_FINDPERSON_BY_FRIENDS);
      lastPos = JPQLUtils.addInClause(sb, "p", "id", lastPos, paramList.size());
      sb.append(") ");
      // TODO Group by doesn't work in HSQLDB or Derby - causes a "Not in aggregate function or group by clause" jdbc exception
      // sb.append(" group by p ");
      break;
    case groupId:
      // select those in the group
      sb.append(PersonDb.JPQL_FINDPERSON_BY_GROUP);
      lastPos = JPQLUtils.addInClause(sb, "p", "id", lastPos, paramList.size());
      sb.append(" and g.id = ?").append(lastPos);
      lastPos++;
      break;
    case deleted:
      // ???
      break;
    case self:
      // select self
      sb.append(PersonDb.JPQL_FINDPERSON);
      lastPos = JPQLUtils.addInClause(sb, "p", "id", lastPos, paramList.size());
      break;
    default:
      throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Group ID not recognized");

    }

    if (GroupId.Type.self.equals(groupId.getType())) {
      plist = JPQLUtils.getListQuery(entityManager, sb.toString(), paramList, collectionOptions);
      totalResults = Long.valueOf(1);
      if (plist.isEmpty()) {
        throw new ProtocolException(HttpServletResponse.SC_BAD_REQUEST, "Person not found");
      }
    } else {
      int filterPos = addFilterClause(sb, PersonDb.getFilterCapability(), collectionOptions,
          lastPos);
      if (filterPos > 0) {
        paramList.add(collectionOptions.getFilterValue());
      }

      // Get total results, that is count the total number of rows for this query
      totalResults = JPQLUtils.getTotalResults(entityManager, sb.toString(), paramList);

      // Execute ordered and paginated query
      if (totalResults > 0) {
        addOrderClause(sb, collectionOptions);
        plist = JPQLUtils.getListQuery(entityManager, sb.toString(), paramList, collectionOptions);
      }

      if (plist == null) {
        plist = Lists.newArrayList();
      }
    }

    // all of the above could equally have been placed into a thread to overlay the
    // db wait times.
    RestfulCollection<Person> restCollection = new RestfulCollection<Person>(
        plist, collectionOptions.getFirst(), totalResults.intValue(), collectionOptions.getMax());
    return ImmediateFuture.newInstance(restCollection);

  }
  
  private ShindigUtil shindigUtil = new ShindigUtil();
  private BeanConverter beanConverter = shindigUtil.getBeanConverter();
  
  /**
   * {@inheritDoc}
   */
  public Future<Person> getPerson(UserId id, Set<String> fields, SecurityToken token)
      throws ProtocolException {
    try {
        String uid = id.getUserId(token);
        Query q = entityManager.createNamedQuery(PersonDb.FINDBY_PERSONID);
        q.setParameter(PersonDb.PARAM_PERSONID, uid);
        q.setFirstResult(0);
        q.setMaxResults(1);
        List<?> plist = q.getResultList();
        Person person = null;
        if (plist != null && !plist.isEmpty()) {
            person = (Person) plist.get(0);
        }
        String jsonStrRep = beanConverter.convertToString(person);
        JSONObject jsonRep = new JSONObject(jsonStrRep);
        if (!fields.isEmpty()) {
            if (fields.size() != 1 || !fields.contains("id"))
                jsonRep = new JSONObject(jsonRep, fields.toArray(new String[fields.size()]));
        }
        person = beanConverter.convertToObject(jsonRep.toString(), Person.class);
        return ImmediateFuture.newInstance(person);
    } catch (Exception je) {
        throw new ProtocolException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, je.getMessage(), je);
    }
  }


    /**
     * Add a filter clause specified by the collection options.
     *
     * @param sb                the query {@link StringBuilder}
     * @param filterable        {@link FilterCapability}
     * @param collectionOptions the options
     * @param lastPos           the last positional parameter that was used so far in the query
     * @return position of the parameter for the filter
     */
    // TODO: if filter is special, it returns 0 and appends nothing to sb
    int addFilterClause(StringBuilder sb, FilterCapability filterable,
                        CollectionOptions collectionOptions, int lastPos) {
        // this makes the filter value saf
        String filter = filterable.findFilterableProperty(collectionOptions.getFilter(),
                collectionOptions.getFilterOperation());
        String filterValue = collectionOptions.getFilterValue();
        int filterPos = 0;
        if (!FilterSpecification.isValid(filter)) {
            return filterPos;
        }
        
        if (FilterSpecification.isSpecial(filter)) {
            if (PersonService.HAS_APP_FILTER.equals(filter)) {
                // Retrieves all friends with any data for this application.
                // TODO: how do we determine which application is being talked about,
                // the assumption below is wrong
                filterPos = lastPos + 1;
                sb.append(" f.application_id  = ?").append(filterPos);
            } else if (PersonService.TOP_FRIENDS_FILTER.equals(filter)) {
                // Retrieves only the user's top friends, this is defined here by the implementation
                // and there is an assumption that the sort order has already been applied.
                // to do this we need to modify the collections options
                // there will only ever b x friends in the list and it will only ever start at 1

                collectionOptions.setFirst(1);
                collectionOptions.setMax(20);

            } else if (PersonService.ALL_FILTER.equals(filter)) {
                // select all, ie no filtering
            } else if (PersonService.IS_WITH_FRIENDS_FILTER.equals(filter)) {
                filterPos = lastPos + 1;
                sb.append(" f.friend  = ?").append(filterPos);
            }
        } else {
            sb.append("p.").append(filter);
            switch (collectionOptions.getFilterOperation()) {
                case contains:
                    filterPos = lastPos + 1;
                    sb.append(" like ").append(" ?").append(filterPos);
                    filterValue = '%' + filterValue + '%';
                    collectionOptions.setFilter(filterValue);
                    break;
                case equals:
                    filterPos = lastPos + 1;
                    sb.append(" = ").append(" ?").append(filterPos);
                    break;
                case present:
                    sb.append(" is not null ");
                    break;
                case startsWith:
                    filterPos = lastPos + 1;
                    sb.append(" like ").append(" ?").append(filterPos);
                    filterValue = '%' + filterValue + '%';
                    collectionOptions.setFilter(filterValue);
                    break;
            }
        }

        return filterPos;
    }

    /**
   * Add an order clause to the query string.
   *
   * @param sb the buffer for the query string
   * @param collectionOptions the options to use for the order.
   */
  void addOrderClause(StringBuilder sb, CollectionOptions collectionOptions) {
      String sortBy = collectionOptions.getSortBy();
      if (StringUtils.isBlank(sortBy)) {
          return;
      }
      if (PersonService.TOP_FRIENDS_SORT.equals(sortBy)) {
          // TODO sorting by friend.score doesn't work right now because of group by issue (see above TODO)
          // this assumes that the query is a join with the friends store.
          sb.append(" order by f.score ");
      } else {
          if ("name".equals(sortBy)) {
              // TODO Is this correct?
              // If sortBy is name then order by p.name.familyName, p.name.givenName.
              sb.append(" order by p.name.familyName, p.name.givenName ");
          } else {
              sb.append(" order by p.").append(sortBy);
          }
          if (collectionOptions.getSortOrder() == null) {
              return;
          }
          switch (collectionOptions.getSortOrder()) {
              case ascending:
                  sb.append(" asc ");
                  break;
              case descending:
                  sb.append(" desc ");
                  break;
          }
      }
  }
}
