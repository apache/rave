package org.apache.rave.portal.repository.impl.util;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * JPA utilities
 */
public class JpaUtil {

    public static <T> T getSingleResult(List<T> list) {
        if (list == null) {
            return null;
        }
        
        switch(list.size()) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                throw new IncorrectResultSizeDataAccessException(1);
        }
    }
    public static <T, I> T saveOrUpdate(I id, EntityManager entityManager, T entity) {
        if (id == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
