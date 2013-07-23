package org.apache.rave.rest.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.rave.rest.model.JsonResponseWrapper;

/**
 * Created with IntelliJ IDEA.
 * User: erinnp
 * Date: 7/22/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonResponseWrapperInterceptor extends AbstractPhaseInterceptor<Message> {

    public JsonResponseWrapperInterceptor() {
        super(Phase.WRITE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Object o = message.getContent(Object.class);

        JsonResponseWrapper wrapper = new JsonResponseWrapper(o);

        message.setContent(JsonResponseWrapper.class, wrapper);
    }
}
