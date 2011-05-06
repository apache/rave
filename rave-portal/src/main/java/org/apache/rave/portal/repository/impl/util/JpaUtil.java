package org.apache.rave.portal.repository.impl.util;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.List;

/**
 * @author mfranklin
 *         Date: 4/25/11
 *         Time: 10:13 PM
 */
public class JpaUtil {
	 //Private constructor for utility classes
	 public JpaUtil () {;}

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
}
