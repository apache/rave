/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/14/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
define(["core/rave_api"], function(api){
    function setContext(contextPath) {
        context = contextPath;
        api.setContext(contextPath);
    }

    function getContext() {
        return context;
    }

    return{
        setContext:setContext,
        getContext:getContext
    }
})
