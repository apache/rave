package org.apache.rave.rest.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: erinnp
 * Date: 7/19/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonResponseWrapper {

    private HashMap<String, String> metadata;
    private Object data;

    //constructor for single resource objects
    public JsonResponseWrapper(Object data) {
        this.metadata = new HashMap<String, String>();
        this.data = data;
    }

    //constructor for list objects
    public JsonResponseWrapper(Object data, Integer limit, Integer offset, Integer count) {
        this.metadata = new HashMap<String, String>();
        this.data = data;

        buildPaginationData(limit, offset, count);
    }

    private void buildPaginationData(Integer limit, Integer offset, Integer count){
        Integer prevOffset = null;
        Integer nextOffset = null;

        if(offset > 0) {
            //build prev offset
            prevOffset = offset - limit;
            if(prevOffset < 0) {
                prevOffset = 0;
            }
        }
        if(limit+offset < count) {
            //build next offset
            nextOffset = limit+offset;
        }

        if(prevOffset != null) {
            this.metadata.put("prev", "?limit=" + limit + "&offset=" + prevOffset);
        }
        if(nextOffset != null) {
            this.metadata.put("next", "?limit=" + limit + "&offset=" + nextOffset);
        }

        this.metadata.put("limit", limit.toString());
        this.metadata.put("offset", offset.toString());
        this.metadata.put("count", count.toString());
    }

    public HashMap<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
