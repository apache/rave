package org.apache.rave.portal.repository.impl;

import org.apache.rave.persistence.jpa.AbstractJpaRepository;
import org.apache.rave.portal.model.Authority;
import org.apache.rave.portal.repository.AuthorityRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

import static org.apache.rave.persistence.jpa.util.JpaUtil.getSingleResult;

/**
 * JPA implementation for {@link org.apache.rave.portal.repository.AuthorityRepository}
 */
@Repository
public class JpaAuthorityRepository extends AbstractJpaRepository<Authority>
        implements AuthorityRepository {

    public JpaAuthorityRepository() {
        super(Authority.class);
    }

    @Override
    public Authority getByAuthority(String authorityName) {
        TypedQuery<Authority> query = manager.createNamedQuery(Authority.GET_BY_AUTHORITY_NAME, Authority.class);
        query.setParameter(Authority.PARAM_AUTHORITY_NAME, authorityName);
        return getSingleResult(query.getResultList());
    }
}
