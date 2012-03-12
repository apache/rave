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

package org.apache.rave.provider.w3c.service.impl;

import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetMetadataResolver;
import org.apache.rave.provider.w3c.Constants;
import org.apache.rave.provider.w3c.repository.W3CWidgetMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class WookieWidgetMetadataResolver implements WidgetMetadataResolver {
    private static Logger logger = LoggerFactory.getLogger(WookieWidgetMetadataResolver.class);
    private W3CWidgetMetadataRepository widgetMetadataRepository;

    @Autowired
    public WookieWidgetMetadataResolver(W3CWidgetMetadataRepository widgetMetadataRepository){
        this.widgetMetadataRepository = widgetMetadataRepository;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getSupportedContext()
     */
    public String getSupportedContext() {
        return Constants.WIDGET_TYPE;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getMetadata(java.lang.String)
     */
    public Widget getMetadata(String url) {
        try {
            String xmlResult = widgetMetadataRepository.getWidgetMetadata(url);
            Widget[] widgets = processResponse(xmlResult);
            return widgets[0];
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.apache.rave.portal.service.WidgetMetadataResolver#getMetadataGroup(java.lang.String)
     */
    @Override
    public Widget[] getMetadataGroup(String url) {
        try {
            String xmlResult = widgetMetadataRepository.getWidgetMetadata(url);
            return processResponse(xmlResult);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget (group) metadata call", e);
        }
    }

    /**
     * Method sets up the xml parsing routine. (from a string supplied from wookie)
     * @param rawXml - raw xml string
     * @return an array of widgets
     */
    private Widget[] processResponse(String rawXml){
        Widget[] widgets = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xml = db.parse(new InputSource(new StringReader(rawXml)));
            widgets = parseWookieAdvert(xml);
        } catch (ParserConfigurationException e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        } catch (SAXException e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error occurred while processing response for Widget metadata call", e);
        }
        return widgets;
    }

    /**
     * Method to parse an xml response from wookie into a set of Widget objects
     * @param xml -a w3c dom document
     * @return an array of widgets
     */
    private Widget[] parseWookieAdvert(Document xml){
        List<Widget> widgets = new ArrayList<Widget>();
        Widget widget;
        Element currentElement = null;
        Element rootEl = xml.getDocumentElement();
        NodeList widgetNodes = rootEl.getElementsByTagName("widget");
        if(widgetNodes != null && widgetNodes.getLength() > 0) {
            for(int i = 0 ; i < widgetNodes.getLength();i++) {
                widget = new Widget();
                widget.setType(getSupportedContext());
                // get the guid
                Element el = (Element)widgetNodes.item(i);
                widget.setUrl(el.getAttribute("identifier"));
                // get the title
                Node titleNode = el.getElementsByTagName("title").item(0);
                if(titleNode != null){
                    currentElement = (Element)titleNode;
                    widget.setTitle(currentElement.getTextContent());
                }
                // get the description
                Node descriptionNode = el.getElementsByTagName("description").item(0);
                if(descriptionNode != null){
                    currentElement = (Element)descriptionNode;
                    widget.setDescription(currentElement.getTextContent());
                }
                // get the icon
                Node iconNode = el.getElementsByTagName("icon").item(0);
                if(iconNode != null){
                    currentElement = (Element)iconNode;
                    widget.setThumbnailUrl(currentElement.getTextContent());
                }
                // get the icon
                Node authorNode = el.getElementsByTagName("author").item(0);
                if(authorNode != null){
                    currentElement = (Element)authorNode;
                    widget.setAuthor(currentElement.getTextContent());
                }
                // add to the list
                widgets.add(widget);
            }
        }
        Widget[] widgetsArr = new Widget[widgets.size()];
        widgetsArr = widgets.toArray(widgetsArr);
        return widgetsArr;
    }

}
