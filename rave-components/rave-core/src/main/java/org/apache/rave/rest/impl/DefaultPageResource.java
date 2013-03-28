package org.apache.rave.rest.impl;


import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.web.renderer.RenderService;
import org.apache.rave.rest.PageResource;
import org.apache.rave.rest.model.Page;
import org.apache.rave.rest.model.RegionWidget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;

public class DefaultPageResource implements PageResource {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PageService pageService;
    private RenderService renderService;

    @Override
    public Response deletePage(String id) {
        logger.debug("Deleting page " + id);
        pageService.deletePage(id);
        return Response.noContent().build();
    }

    @Override
    public Response getPage(String id) {
        logger.debug("Retrieving page for export: " + id);
        return Response.ok(pageService.getPage(id)).build();
    }

    @Override
    public Response updatePage(String id, Page page) {
        return Response.ok(pageService.updatePage(id, page.getName(), page.getPageLayoutCode())).build();
    }

    @Override
    public Response getPageOmdl(@PathParam("id") String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response getPagesForRender(@PathParam("context") String context, @PathParam("identifier") String identifier) {
        return null;
    }

    @Override
    public Response getPageForRender(@PathParam("context") String context, @PathParam("identifier") String identifier, @PathParam("id") String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response clonePage(@PathParam("context") String context, @PathParam("identifier") String identifier, @PathParam("id") String id) {
        return null;
    }

    @Override
    public Response importOmdlPage(@PathParam("context") String context, @PathParam("identifier") String identifier, @Multipart(value = "root", type = "application/octet-stream") File page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response createPage(@PathParam("context") String context, @PathParam("identifier") String identifier, Page page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response deletePageInContext(@PathParam("id") String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageInContext(@PathParam("id") String id, Page page) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response movePage(@PathParam("id") String id, @QueryParam("moveAfterPageId") String moveAfterPageId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addWidgetToPage(@PathParam("id") String id, RegionWidget widget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response removeWidgetFromPage(@PathParam("id") String id, @PathParam("regionWidgetId") String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addWidgetToRegion(@PathParam("id") String id, @PathParam("regionId") String regionId, RegionWidget widget) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response moveWidgetOnPage(@PathParam("id") String id, @PathParam("toRegionId") String toRegionId, @PathParam("regionWidgetId") String regionWidgetId, @QueryParam("position") int position) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response moveWidgetToPage(@PathParam("targetPageId") String targetPageId, @PathParam("regionWidgetId") String regionWidgetId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response addMemberToPage(@PathParam("id") String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response removeMemberFromPage(@PathParam("id") String id, @PathParam("userId") String userId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updateSharedPageStatus(@PathParam("id") String id, @PathParam("userId") String userId, String status) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response updatePageEditingStatus(@PathParam("id") String id, @PathParam("userId") String userId, boolean editor) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Inject
    public void setPageService(PageService pageService) {
        this.pageService = pageService;
    }
}
