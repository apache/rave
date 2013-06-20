/**
 * Created with IntelliJ IDEA.
 * User: DGORNSTEIN
 * Date: 6/11/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
define(["./rave_wookie", "./rave_opensocial", "./rave_ajax", "./rave_api", "./rave_core", "./rave_view_manager", "./rave_widget",
        "portal/rave_admin", "portal/rave_backbone", "portal/rave_context", "portal/rave_display", "portal/rave_forms", "portal/rave_layout", "portal/rave_models", "portal/rave_person_profile", "portal/rave_portal", "portal/rave_store", "portal/rave_templates", "portal/rave_ui"],
    function(wookie, os, raveAjax, raveApi, raveCore, raveViewManager, raveRegionWidget,
             raveAdmin, raveBackbone, raveContext, raveDisplay, raveForms, raveLayout, raveModels, ravePersonProfile, ravePortal, raveStore, raveTemplates, raveUi){

        rave = {};

        rave.ajax = raveAjax;
        rave.api = raveApi;
        _.extend(rave, raveCore);
        _.extend(rave, raveViewManager);
        rave.RegionWidget = raveRegionWidget;
        rave.admin = raveAdmin;
        rave.Model = raveBackbone.Model;
        rave.Collection = raveBackbone.Collection;
        rave.View = raveBackbone.View;
        rave.forms = raveForms;
        rave.layout = raveLayout;
        rave.models = raveModels;
        rave.personprofile = ravePersonProfile;
        ravePortal = _.extend(ravePortal, raveTemplates);
        ravePortal = _.extend(ravePortal, raveDisplay);
        ravePortal = _.extend(ravePortal, raveContext);
        rave = _.extend(rave, ravePortal);
        rave.store = raveStore;
        rave.ui = raveUi;


})
