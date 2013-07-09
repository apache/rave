/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.opensocial.service.impl;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;
import org.apache.rave.portal.repository.ActivityStreamsRepository;
import org.apache.rave.util.ActivityConversionUtil;
import org.apache.shindig.auth.BasicSecurityToken;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.ImmediateFuture;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.protocol.model.SortOrder;
import org.apache.shindig.social.core.model.ActivityEntryImpl;
import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.spi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;


@Service
public class DefaultActivityStreamsService implements ActivityStreamService {

    public static final String OBJECT_TYPE_PERSON = "person";
    @Autowired
    private ActivityStreamsRepository repository;

    @Autowired
    public DefaultActivityStreamsService(ActivityStreamsRepository repository, PersonService personService) {
        this.repository = repository;
        this.personService = personService;
    }


    private PersonService personService;

    private static Logger log = Logger.getLogger(DefaultActivityStreamsService.class.getName());
    private static ActivityConversionUtil converter = new ActivityConversionUtil();


    /**
     * ActivityStreamService impl:
     */

    /**
     * Returns a list of activities that correspond to the passed in users and group.
     * <p/>
     * Specified by: getActivityEntries(...) in ActivityStreamService
     * <p/>
     * Parameters:
     * userIds The set of ids of the people to fetch activities for.
     * groupId Indicates whether to fetch activities for a group.
     * appId The app id.
     * fields The fields to return. Empty set implies all
     * options The sorting/filtering/pagination options
     * token A valid SecurityToken
     * Returns:
     * a response item with the list of activities.
     */
    @Override
    public Future<RestfulCollection<ActivityEntry>> getActivityEntries(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, SecurityToken token) {
        List<ActivityEntry> result = getFromRepository(userIds, groupId, appId, fields, options, token);
        return ImmediateFuture.newInstance(new RestfulCollection<ActivityEntry>(result));

    }

    /**
     * Returns a set of activities for the passed in user and group that corresponds to a list of activityIds.
     * <p/>
     * Specified by: getActivityEntries(...) in ActivityStreamService
     * <p/>
     * Parameters:
     * userId The set of ids of the people to fetch activities for.
     * groupId Indicates whether to fetch activities for a group.
     * appId The app id.
     * fields The fields to return. Empty set implies all
     * options The sorting/filtering/pagination options
     * activityIds The set of activity ids to fetch.
     * token A valid SecurityToken
     * Returns:
     * a response item with the list of activities.
     * Throws:
     * ProtocolException - if any.
     */
    @Override
    public Future<RestfulCollection<ActivityEntry>> getActivityEntries(UserId userId, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, Set<String> activityIds, SecurityToken token) throws ProtocolException {
        List<ActivityEntry> entries = Lists.newLinkedList();
        Map<String, Person> peopleById = Maps.newHashMap();

        for (String id : activityIds) {
            entries.add(getActivity(fields, userId.getUserId(token), peopleById, id));
        }

        return ImmediateFuture.newInstance(new RestfulCollection<ActivityEntry>(entries));
    }

    /**
     * Returns an activity for the passed in user and group that corresponds to a single activityId.
     * <p/>
     * Specified by: getActivityEntry(...) in ActivityStreamService
     * Parameters:
     * userId The id to fetch activities for.
     * groupId Indicates whether to fetch activities for a group.
     * appId The app id.
     * fields The fields to return. Empty set implies all
     * activityId The activity id to fetch.
     * token A valid SecurityToken
     * Returns:
     * a response item with the list of activities.
     * Throws:
     * ProtocolException - if any.
     */
    @Override
    public Future<ActivityEntry> getActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields, String activityId, SecurityToken token) throws ProtocolException {
        return ImmediateFuture.newInstance(getActivity(fields, userId.getUserId(token), Maps.<String, Person>newHashMap(), activityId));
    }

    /**
     * Deletes the activity for the passed in user and group that corresponds to the activityId.
     * <p/>
     * Specified by: deleteActivityEntries(...) in ActivityStreamService
     * Parameters:
     * userId The user.
     * groupId The group.
     * appId The app id.
     * activityIds A list of activity ids to delete.
     * token A valid SecurityToken.
     * Returns:
     * a response item containing any errors
     * Throws:
     * ProtocolException - if any.
     */
    @Override
    public Future<Void> deleteActivityEntries(UserId userId, GroupId groupId, String appId, Set<String> activityIds, SecurityToken token) throws ProtocolException {

        String uid = userId.getUserId(token);

        for (String id : activityIds) {
            ActivityStreamsEntry activity = repository.get(id);
            //TODO: should we be checking against the user id?
            if (activity != null && activity.getUserId().equalsIgnoreCase(uid)) {
                repository.delete(activity);
            }
        }

        return ImmediateFuture.newInstance(null);
    }

    /**
     * Updates the specified Activity.
     * <p/>
     * Specified by: updateActivityEntry(...) in ActivityStreamService
     * Parameters:
     * userId The id of the person to update the activity for
     * groupId The group
     * appId The app id
     * fields The fields to return
     * activity The updated activity
     * activityId The id of the existing activity to update
     * token A valid SecurityToken
     * Returns:
     * a response item containing any errors
     * Throws:
     * ProtocolException - if any
     */
    @Override
    public Future<ActivityEntry> updateActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields, ActivityEntry activity, String activityId, SecurityToken token) throws ProtocolException {

        String uid = userId.getUserId(token);

        //Verify / set ids
        if (activityId == null) {
            if (activity.getId() == null) {
                //One of these must have an id
                //TODO: Throw an error?
                return null;
            } else {
                activityId = activity.getId();
            }
        } else {
            if (activity.getId() == null) {
                activity.setId(activityId);
            } else if (!activity.getId().equalsIgnoreCase(activityId)) {
                //TODO: should this throw an error if two different ids are passed in?
                return null;
            }
        }

        ActivityStreamsEntryImpl tmp = converter.convert(activity);
        tmp.setUserId(uid);
        tmp.setGroupId(groupId.getObjectId().toString());
        tmp.setAppId(appId);

        ActivityStreamsEntry saved = repository.save(tmp);
        ActivityEntryImpl impl = converter.convert(saved);
        return ImmediateFuture.newInstance((ActivityEntry) impl);
    }

    /**
     * Creates the passed in activity for the passed in user and group. Once createActivity is called, getActivities will be able to return the Activity.
     * <p/>
     * Specified by: createActivityEntry(...) in ActivityStreamService
     * Parameters:
     * userId The id of the person to create the activity for.
     * groupId The group.
     * appId The app id.
     * fields The fields to return.
     * activity The activity to create.
     * token A valid SecurityToken
     * Returns:
     * a response item containing any errors
     * Throws:
     * ProtocolException - if any.
     */
    @Override
    public Future<ActivityEntry> createActivityEntry(UserId userId, GroupId groupId, String appId, Set<String> fields, ActivityEntry activity, SecurityToken token) throws ProtocolException {

        String uid = userId.getUserId(token);


        ActivityStreamsEntryImpl activityEntity = converter.convert(activity);

        ActivityStreamsEntry saved = repository.save(activityEntity);
        return ImmediateFuture.newInstance((ActivityEntry)converter.convert(saved));

    }


    /**
     * AggregatorProcessor impl:
     */

    //@Override
    public void process(Object input) {
        // TODO: what data format will be returned by ActivityStream-producing feeds? JSON Collection?
        // parse Activities out of stream; turn into ActivityEntries
        // store ActivityEntries in repository

        if (input instanceof ArrayList) {
            ArrayList<ActivityStreamsEntryImpl> activities = (ArrayList<ActivityStreamsEntryImpl>) input;
            for (ActivityStreamsEntryImpl activity : activities) {
                repository.save(activity);
            }
        }
    }

    //@Override
    public Collection<? extends ActivityEntry> getContent() {
        return converter.convert(repository.getAll());
    }


    /**
     * Utilities
     */

    /**
     * From JsonDbOpensocialService. I believe this is supposed to find the intersection of
     * the set of userids passed in, and the set of users belonging to the group. If the
     * group type is "friends", then maybe it's supposed to return the list of friends
     * for each of the user ids provided?
     *
     * @param users
     * @param group
     * @param token
     * @return
     */
    public Set<String> getIdSet(Set<UserId> users, GroupId group, SecurityToken token) {
        Set<String> ids = Sets.newLinkedHashSet();
        for (UserId user : users) {
            ids.addAll(getIdSet(user, group, token));
        }
        return ids;
    }

    private ActivityEntry getActivity(Set<String> fields, String uid, Map<String, Person> peopleById, String id) {
        ActivityStreamsEntry entry = repository.get(id);

        if (entry!=null){
            if (entry.getUserId().equalsIgnoreCase(uid)) {
                populatePersonObjects(entry, peopleById);
                return filterFields(entry, fields);
            }
        }
        return null;
    }

    //From JsonDbOpensocialService
    private Set<String> getIdSet(UserId user, GroupId group, SecurityToken token) {
        String userId = user.getUserId(token);


        if (group == null) {
            return ImmutableSortedSet.of(userId);
        }

        Set<String> returnVal = Sets.newLinkedHashSet();
        returnVal.add(userId);

        Set<UserId> userIdSortedSet = new HashSet<UserId>();
        userIdSortedSet.add(user);

        try{
            List<Person> people = personService.getPeople(userIdSortedSet,group,null,returnVal,token).get().getList();
            for (Person p : people){
                returnVal.add(p.getId());
            }

        }catch(InterruptedException e){
            log.info("request interrupted " + e);
        } catch(ExecutionException x){
            log.info("execution exception " + x);
        }  catch(Exception e){
            log.info("problem: " + e);
        }

        return returnVal;
    }

    /**
     * Given an DeserializableActivityEntry, return a new one that only has
     * the specified fields set on it.
     *
     * @param entry
     * @param fields
     * @return
     */
    private ActivityEntry filterFields(ActivityStreamsEntry entry,
                                       Set<String> fields) {
        // TODO: implement

        return converter.convert(entry);
    }

    private List<ActivityEntry> getFromRepository(Set<UserId> userIds, GroupId groupId, String appId, Set<String> fields, CollectionOptions options, SecurityToken token) {
        List<ActivityStreamsEntry> result = Lists.newArrayList();
        Map<String, Person> peopleById = Maps.newHashMap();
        Set<String> idSet = getIdSet(userIds, groupId, token);
        for (String id : idSet) {
            List<ActivityStreamsEntry> entries = repository.getByUserId(id);

            if (entries!=null){
                result.addAll(populateActivityEntries(entries, peopleById));
            }
        }
        sortByPublished(result, options == null ? null : options.getSortOrder());
        return convert(result);
    }

    private List<ActivityStreamsEntry> populateActivityEntries(List<ActivityStreamsEntry> entries, Map<String, Person> peopleById) {
        for(ActivityStreamsEntry entry : entries) {
            populatePersonObjects(entry, peopleById);
        }
        return entries;
    }

    private void sortByPublished(List<ActivityStreamsEntry> result, final SortOrder order) {
        Collections.sort(result, new Comparator<ActivityStreamsEntry>() {
            @Override
            public int compare(ActivityStreamsEntry one, ActivityStreamsEntry two) {
                Date publishedOne = one.getPublished();
                Date publishedTwo = two.getPublished();
                if (publishedOne == null) {
                    return publishedTwo == null ? 0 : -1;
                } else if (publishedTwo == null) {
                    return 1;
                } else {
                    return order != null && order == SortOrder.descending ? publishedOne.compareTo(publishedTwo) : publishedTwo.compareTo(publishedOne);
                }
            }
        });
    }

    private List<ActivityEntry> convert(List<ActivityStreamsEntry> result) {
        List<ActivityEntry> converted = Lists.newArrayList();
        for(ActivityStreamsEntry entry : result) {
            converted.add(converter.convert(entry));
        }
        return converted;
    }

    private void populatePersonObjects(ActivityStreamsEntry entry, Map<String, Person> peopleById) {
        ActivityStreamsObject actor = entry.getActor();
        if(entry.getActor() != null && OBJECT_TYPE_PERSON.equals(actor.getObjectType())) {
            populatePerson(peopleById, actor);
        }
        if(entry.getObject() != null && OBJECT_TYPE_PERSON.equals(entry.getObject().getObjectType())) {
            populatePerson(peopleById, entry.getObject());
        }
        if(entry.getTarget() != null && OBJECT_TYPE_PERSON.equals(entry.getTarget().getObjectType())) {
            populatePerson(peopleById, entry.getTarget());
        }
    }

    private void populatePerson(Map<String, Person> peopleById, ActivityStreamsObject actor) {
        String id = actor.getId();
        Person person = peopleById.containsKey(id) ? peopleById.get(id) : getPerson(id);
        if(person != null) {
            peopleById.put(id, person);
            actor.setUrl(person.getProfileUrl());
            actor.setDisplayName(person.getDisplayName());
            ActivityStreamsMediaLinkImpl image = new ActivityStreamsMediaLinkImpl();
            image.setUrl(person.getThumbnailUrl());
            actor.setImage(image);
        }
    }

    private Person getPerson(String id) {
        Person person;
        SecurityToken token = new BasicSecurityToken(null, id, null, null, null, null, null, null, null);
        try {
            person =  personService.getPerson(new UserId(UserId.Type.viewer, id), null, token).get();
        } catch (Exception e) {
            throw new RuntimeException("Unable to retrieve person", e);
        }
        return person;
    }

    /**
     * @return the repository
     */
    public ActivityStreamsRepository getRepository() {
        return repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(ActivityStreamsRepository repository) {
        this.repository = repository;
    }

}
