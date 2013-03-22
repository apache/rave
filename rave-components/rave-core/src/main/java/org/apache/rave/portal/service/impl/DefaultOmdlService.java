/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.rave.exception.DuplicateItemException;
import org.apache.rave.model.Page;
import org.apache.rave.model.Widget;
import org.apache.rave.portal.model.impl.WidgetImpl;
import org.apache.rave.portal.model.util.omdl.BadOmdlXmlFormatException;
import org.apache.rave.portal.model.util.omdl.OmdlConstants;
import org.apache.rave.portal.model.util.omdl.OmdlInputAdapter;
import org.apache.rave.portal.model.util.omdl.OmdlModelUtils;
import org.apache.rave.portal.model.util.omdl.OmdlOutputAdapter;
import org.apache.rave.portal.model.util.omdl.OmdlWidgetReference;
import org.apache.rave.portal.service.OmdlService;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.RemoteWidgetResolverService;
import org.apache.rave.portal.service.WidgetService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
/**
 * This class is responsible for the import and export of page objects to/from OMDL format
 * 
 * See: http://omdl.org 
 * 
 * for more information on the specification.
 */
@Service
public class DefaultOmdlService implements OmdlService, OmdlConstants {
    
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(DefaultOmdlService.class);

    @Value("${provider.wookie.wookieServerUrl}")
    private String wookieUrl;

    private PageService pageService;
    private WidgetService widgetService;
    private UserService userService;
    private RemoteWidgetResolverService widgetResolverService;
    
    public DefaultOmdlService(){}

    @Autowired
    public DefaultOmdlService(PageService pageService, WidgetService widgetService, 
            UserService userService, RemoteWidgetResolverService widgetResolverService){
        this.pageService = pageService;
        this.widgetService = widgetService;
        this.userService = userService;
        this.widgetResolverService = widgetResolverService;
    }
    
    @Override
    public OmdlOutputAdapter exportOmdl(String pageId, String omdlUrl) {
        Page page = pageService.getPage(pageId);
        OmdlOutputAdapter omdl = new OmdlOutputAdapter(widgetService, userService);
        omdl.buildModel(page, omdlUrl, wookieUrl);
        return omdl;
    }

    @Override
    public Page importOmdl(MultipartFile multipartFile, String pageName) throws DuplicateItemException {
        Page page = null;
        OmdlInputAdapter omdlInputAdapter = new OmdlInputAdapter();
        File temp = null;
        String xml = null;
        try {
            if(multipartFile != null){
                if(multipartFile.getSize() > 0) {
                    String tempUploadFolder = System.getProperty("java.io.tmpdir");
                    temp = new File(tempUploadFolder, multipartFile.getOriginalFilename());
                    multipartFile.transferTo(temp);
                    xml =  FileUtils.readFileToString(temp);
                }
            }
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        Document root = initializeBuilder(xml);
        if(root != null){
            try {
                parseOmdlFile(root, omdlInputAdapter);
            } catch (BadOmdlXmlFormatException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        page = pageService.addNewUserPage(pageName, omdlInputAdapter.getLayoutCode());
        switch (page.getRegions().size()){
        case 1:
            populateRegionWidgets(page, omdlInputAdapter.getAllUrls(), page.getRegions().get(0).getId());
            break;
        case 2:
            populateRegionWidgets(page, omdlInputAdapter.getAllLeftUrls(), page.getRegions().get(0).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllRightUrls(), page.getRegions().get(1).getId());
            break;
        case 3:
            populateRegionWidgets(page, omdlInputAdapter.getAllLeftUrls(), page.getRegions().get(0).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllCenterUrls(), page.getRegions().get(1).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllRightUrls(), page.getRegions().get(2).getId());
            break;
        case 4:
            populateRegionWidgets(page, omdlInputAdapter.getAllLeftUrls(), page.getRegions().get(0).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllCenterUrls(), page.getRegions().get(1).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllRightUrls(), page.getRegions().get(2).getId());
            populateRegionWidgets(page, omdlInputAdapter.getAllUnknownUrls(), page.getRegions().get(3).getId());
            break;
        default:
            // there are no layouts with more than 4 regions at present
        }
        return page;
    }

    private void populateRegionWidgets(Page page, List<OmdlWidgetReference> widgetReferences, String regionId ){
        Widget raveWidget = null;
        for (OmdlWidgetReference widgetReference : widgetReferences){
            // try to find if the widget is already installed in rave by its identifier (should be the rave widget url)
            logger.info("Found OMDL widget reference ("+widgetReference.getWidgetIdentifier()+")");
            raveWidget = widgetService.getWidgetByUrl(widgetReference.getWidgetIdentifier());
            
            // If not found download and install to widget container, then to rave.
            if(raveWidget==null){
                String providerType = widgetReference.getRaveWidgetTypeFromFormatType();
                if(!providerType.equals(null)){
                    try {
                        Widget resolvedWidget = widgetResolverService.resolveAndDownloadWidgetMetadata(widgetReference.getWidgetLink(), providerType);
                        if(resolvedWidget!=null){
                            // Check again in case the OMDL id attribute is not the same as the one found in the href attribute
                            if(widgetService.getWidgetByUrl(resolvedWidget.getUrl())==null){
                                raveWidget = widgetResolverService.addWidget(resolvedWidget);
                                logger.info("Widget added to rave. ("+raveWidget.getUrl()+")");
                            }else{
                                logger.info("Widget was already added to rave. ("+resolvedWidget.getUrl()+")");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Problem installing widget: "+ e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
            }
            
            if(raveWidget != null){
                if(widgetResolverService.isPublished()){
                    pageService.addWidgetToPageRegion(page.getId(), raveWidget.getId(), regionId);
                }
                else{
                    logger.error("Unable to add widget to page as the default status is set to PREVIEW. (" + raveWidget.getUrl() + ")");
                }
            }
            else{
                logger.error("Unable to resolve widget entry found in OMDL file. " + widgetReference.getWidgetIdentifier() 
                        + " : " + widgetReference.getWidgetLink());
            }
        }
    }

    private void parseOmdlFile(Document xml, OmdlInputAdapter omdlInputAdapter) throws BadOmdlXmlFormatException{
        Element rootEl = xml.getDocumentElement();
        String rootNodename = rootEl.getNodeName();
        String namespace = rootEl.getNamespaceURI();
        if (rootNodename!=WORKSPACE){
            throw new BadOmdlXmlFormatException("Root node must be <" + WORKSPACE + ">");
        }
        // TODO - which is it - spec examples show both?
        if (namespace!=NAMESPACE && namespace!=NAMESPACE+"/"){
            throw new BadOmdlXmlFormatException("Default xml namespace must be " + NAMESPACE);
        }
        //child <identifier> - omit
        //child <goal> - omit
        //child <status> - omit
        //child <description> - omit
        //child <creator> - omit
        //child <date> - omit
        
        // Try to get the page name (title in omdl)
        String title = null;
        NodeList nodeList = rootEl.getElementsByTagNameNS(namespace, TITLE);
        if(nodeList.getLength() > 0){
            Node node = rootEl.getElementsByTagNameNS(namespace, TITLE).item(0);
            if(node.getTextContent()!=null){
                title = node.getTextContent();
            }
        }
        // store this if found, although at present it will be overwritten by the users page name
        omdlInputAdapter.setName(title);

        String layoutCode = null;
        // Try to get the layoutCode
        nodeList = rootEl.getElementsByTagNameNS(namespace, LAYOUT);
        if(nodeList.getLength() > 0){
            Node node = rootEl.getElementsByTagNameNS(namespace, LAYOUT).item(0);
            if(node.getTextContent() != null){
                layoutCode = node.getTextContent();
            }
        }
        // Next try to find all the <app> elements (widgets)
        nodeList = rootEl.getElementsByTagNameNS(namespace, APP);
        if(nodeList != null && nodeList.getLength() > 0) {
            for(int i = 0 ; i < nodeList.getLength();i++) {
                Element appElement = (Element)nodeList.item(i);
                String id = appElement.getAttribute(ID_ATTRIBUTE);
                if(id != null){
                    String position = null;
                    String hrefLink = null;
                    String hrefType = null;
                    Node positionNode = appElement.getElementsByTagNameNS(namespace, POSITION).item(0);
                    if(positionNode != null){
                        Element positionElement = (Element)positionNode;
                        position = positionElement.getTextContent();
                    }
                    Node linkNode = appElement.getElementsByTagNameNS(namespace, LINK).item(0);
                    if(linkNode != null){
                        Element linkElement = (Element)linkNode;
                        hrefLink = linkElement.getAttribute(HREF);
                        hrefType = linkElement.getAttribute(TYPE_ATTRIBUTE);
                    }
                    omdlInputAdapter.addToAppMap(new OmdlWidgetReference(id, hrefLink, hrefType), position);
                }
            }
        }
        // store the string found in the xml file
        omdlInputAdapter.setLayoutCode(layoutCode);
        // update this string into a RAVE layout
        omdlInputAdapter.setLayoutCode(OmdlModelUtils.getRaveLayoutForImport(omdlInputAdapter));
    }

    private Document initializeBuilder(String rawXml){
        Document xmlRoot = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            xmlRoot = db.parse(new InputSource(new StringReader(rawXml)));
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (SAXException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return xmlRoot;
    }

}
