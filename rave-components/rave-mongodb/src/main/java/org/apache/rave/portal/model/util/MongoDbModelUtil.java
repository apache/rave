package org.apache.rave.portal.model.util;

import java.util.Random;

/**
 *
 */
public class MongoDbModelUtil {

    private MongoDbModelUtil(){}

    public static long generateId() {
        return new Random().nextLong();
    }
}
