package org.apache.rave.portal.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: DSULLIVAN
 * Date: 12/10/12
 * Time: 12:39 PM
 */
public class MongoDbPageLayoutTest {
    private  MongoDbPageLayout pageLayout;
    private Long id;

    @Before
    public void setup(){
        pageLayout = new MongoDbPageLayout();
        id = (long)123;
        pageLayout.setId(id);
    }

    @Test
    public void equals_Same(){
          assertTrue(pageLayout.equals(pageLayout));
    }

    @Test
    public void equals_Diff_Instance(){
        Object o = new Object();
        assertFalse(pageLayout.equals(o));
    }

    @Test
    public void equals_Super(){
        PageLayout p = new MongoDbPageLayout();
        assertFalse(pageLayout.equals(p));
    }

    @Test
    public void equals_Null_Id(){
        MongoDbPageLayout p_1 = new MongoDbPageLayout();
        MongoDbPageLayout p_2 = new MongoDbPageLayout();
        p_1.setId((long)321);
        assertFalse(p_1.equals(p_2));
        assertFalse(p_2.equals(p_1));
    }

    @Test
    public void equals_Same_Id(){
        MongoDbPageLayout p_1 = new MongoDbPageLayout();
        MongoDbPageLayout p_2 = new MongoDbPageLayout();
        p_1.setId((long)321);
        p_2.setId((long)321);
        assertTrue(p_1.equals(p_2));
    }

    @Test
    public void hashCode_Valid(){
        assertNotNull(pageLayout.hashCode());
    }
}
