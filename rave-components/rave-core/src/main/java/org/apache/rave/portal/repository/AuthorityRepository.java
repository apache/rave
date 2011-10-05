package org.apache.rave.portal.repository;

import org.apache.rave.persistence.Repository;
import org.apache.rave.portal.model.Authority;

/**
 * Repository interface for {@link org.apache.rave.portal.model.Authority}
 */
public interface AuthorityRepository extends Repository<Authority> {

    /**
     * Finds the {@link Authority} by its name
     *
     * @param authorityName (unique) name of the Authority
     * @return Authority if it can be found, otherwise {@literal null}
     */
    Authority getByAuthority(String authorityName);
}
