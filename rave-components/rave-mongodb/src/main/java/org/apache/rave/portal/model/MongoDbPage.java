package org.apache.rave.portal.model;

import org.apache.rave.portal.model.impl.PageImpl;

public class MongoDbPage extends PageImpl {
    private String _ownerId;

    public String get_ownerId() {
        return _ownerId;
    }

    public void set_ownerId(String _ownerId) {
        this._ownerId = _ownerId;
    }
}
