/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.rave.util;

import com.google.common.collect.Lists;
import org.apache.rave.model.ActivityStreamsEntry;
import org.apache.rave.model.ActivityStreamsMediaLink;
import org.apache.rave.model.ActivityStreamsItem;
import org.apache.rave.model.ActivityStreamsObject;
import org.apache.rave.portal.model.impl.ActivityStreamsEntryImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsMediaLinkImpl;
import org.apache.rave.portal.model.impl.ActivityStreamsObjectImpl;
import org.apache.rave.portal.util.ModelUtil;
import org.apache.shindig.protocol.model.ExtendableBean;
import org.apache.shindig.protocol.model.ExtendableBeanImpl;
import org.apache.shindig.social.core.model.ActivityEntryImpl;
import org.apache.shindig.social.core.model.ActivityObjectImpl;
import org.apache.shindig.social.core.model.MediaLinkImpl;
import org.apache.shindig.social.opensocial.model.ActivityEntry;
import org.apache.shindig.social.opensocial.model.ActivityObject;
import org.apache.shindig.social.opensocial.model.MediaLink;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.rave.portal.util.ModelUtil.dateToString;


public class ActivityConversionUtil {

    public List<ActivityEntry> convert(Collection<? extends ActivityStreamsEntry> all) {
        if(all == null) {
            return null;
        }
        List<ActivityEntry> list = Lists.newArrayList();
        for (ActivityStreamsEntry entry : all) {
            list.add(convert(entry));
        }
        return list;
    }

    public List<ActivityStreamsObject> convertObjectEntries(Collection<ActivityObject> all) {
        if(all == null) {
            return null;
        }
        List<ActivityStreamsObject> list = Lists.newArrayList();
        for (ActivityObject entry : all) {
            list.add(convert(entry));
        }
        return list;
    }

    public ActivityEntryImpl convert(ActivityStreamsEntry saved) {
        if(saved == null) {
            return null;
        }
        ActivityEntryImpl impl = new ActivityEntryImpl();
        impl.setActor(convert(saved.getActor()));
        impl.setContent(saved.getContent());
        impl.setExtensions(convert(saved.getExtensions()));
        impl.setGenerator(convert(saved.getGenerator()));
        impl.setIcon(convert(saved.getIcon()));
        impl.setId(saved.getId());
        impl.setObject(convert(saved.getObject()));
        impl.setOpenSocial(convert(saved.getOpenSocial()));
        impl.setProvider(convert(saved.getProvider()));
        impl.setPublished(dateToString(saved.getPublished()));
        impl.setTarget(convert(saved.getTarget()));
        impl.setUpdated(dateToString(saved.getUpdated()));
        impl.setTitle(saved.getTitle());
        impl.setUrl(saved.getUrl());
        impl.setVerb(saved.getVerb());
        return impl;
    }

    public ActivityObject convert(ActivityStreamsObject object) {
        if(object == null) {
            return null;
        }
        ActivityObjectImpl impl = new ActivityObjectImpl();
        impl.setAttachments(convert(object.getAttachments()));
        impl.setAuthor(convert(object.getAuthor()));
        impl.setContent(object.getContent());
        impl.setDisplayName(object.getDisplayName());
        impl.setDownstreamDuplicates(object.getDownstreamDuplicates());
        impl.setId(object.getId());
        impl.setImage(convert(object.getImage()));
        impl.setObjectType(object.getObjectType());
        impl.setPublished(dateToString(object.getPublished()));
        impl.setSummary(object.getSummary());
        impl.setUpdated(dateToString(object.getUpdated()));
        impl.setUpstreamDuplicates(object.getUpstreamDuplicates());
        impl.setUrl(object.getUrl());
        impl.setOpenSocial(convert(object.getOpenSocial()));
        return impl;
    }

    public ActivityObject convert(ActivityStreamsItem object) {
        if (object instanceof ActivityStreamsObject){
            return convert((ActivityStreamsObject)object);
        }
        return null;

    }

    public List<ActivityObject> convert(List<? extends ActivityStreamsObject> attachments) {
        if(attachments == null) {
            return null;
        }
        List<ActivityObject> list = Lists.newArrayList();
        for (ActivityStreamsObject entity : attachments) {
            list.add(convert(entity));
        }
        return list;
    }

    public ActivityStreamsEntryImpl convert(ActivityEntry activity){
        ActivityStreamsEntryImpl entity = new ActivityStreamsEntryImpl();
        if (activity.getActor()!=null){entity.setActor(convert(activity.getActor()));  }
        if (activity.getContent()!=null){entity.setContent(activity.getContent());}
        if (activity.getGenerator()!=null){entity.setGenerator(convert(activity.getGenerator()));  }
        if (activity.getIcon()!=null){ entity.setIcon(convert(activity.getIcon()));  }
        if (activity.getId()!=null){entity.setId(activity.getId());  }
        if (activity.getObject()!=null){entity.setObject(convert(activity.getObject())); }
        if (activity.getPublished()!=null){entity.setPublished(ModelUtil.stringToDate(activity.getPublished())); }
        if (activity.getProvider()!=null){ entity.setProvider(convert(activity.getProvider()));  }
        if (activity.getTarget()!=null){entity.setTarget(convert(activity.getTarget())); }
        if (activity.getTitle()!=null){ entity.setTitle(activity.getTitle());   }
        if (activity.getUpdated()!=null){entity.setUpdated(ModelUtil.stringToDate(activity.getUpdated()));}
        if (activity.getUrl()!=null){entity.setUrl(activity.getUrl());}
        if (activity.getVerb()!=null){entity.setVerb(activity.getVerb());  }
        if (activity.getOpenSocial()!=null){entity.setOpenSocial(convert(activity.getOpenSocial()));  }
        if (activity.getExtensions()!=null){entity.setExtensions(convert(activity.getExtensions())); }
        return entity;
    }

    public ActivityStreamsObject convert(ActivityObject object){
        ActivityStreamsObject entity = new ActivityStreamsObjectImpl();

        if (object.getAuthor()!=null){entity.setAuthor(convert(object.getAuthor()));}
        if (object.getAttachments()!=null){entity.setAttachments(convertObjectEntries(object.getAttachments()));}
        if (object.getContent()!=null){ entity.setContent(object.getContent());}
        if (object.getDisplayName()!=null){ entity.setDisplayName(object.getDisplayName());}
        if (object.getDownstreamDuplicates()!=null){entity.setDownstreamDuplicates(object.getDownstreamDuplicates());}
        if (object.getId()!=null){ entity.setId(object.getId());   }
        if (object.getImage()!=null){entity.setImage(convert(object.getImage()));}
        if (object.getObjectType()!=null){entity.setObjectType(object.getObjectType());  }
        if (object.getPublished()!=null){entity.setPublished(ModelUtil.stringToDate(object.getPublished()));   }
        if (object.getSummary()!=null){entity.setSummary(object.getSummary());  }
        if (object.getUpdated()!=null){entity.setUpdated(ModelUtil.stringToDate(object.getUpdated()));   }
        if (object.getUpstreamDuplicates()!=null){entity.setUpstreamDuplicates(object.getUpstreamDuplicates()); }
        if (object.getUrl()!=null){entity.setUrl(object.getUrl());  }
        if (object.getOpenSocial()!=null){entity.setOpenSocial(convert(object.getOpenSocial()));  }
        return entity;

    }

    public ActivityStreamsMediaLink convert (MediaLink icon) {
        if(icon == null) return null;
        ActivityStreamsMediaLink entity = new ActivityStreamsMediaLinkImpl();
        entity.setDuration(icon.getDuration());
        entity.setHeight(icon.getHeight());
        entity.setOpenSocial(convert(icon.getOpenSocial()));
        entity.setUrl(icon.getUrl());
        entity.setWidth(icon.getWidth());
        return entity;
    }

    public MediaLink convert (ActivityStreamsMediaLink icon) {
        if(icon == null) return null;
        MediaLinkImpl entity = new MediaLinkImpl();
        entity.setDuration(icon.getDuration());
        entity.setHeight(icon.getHeight());
        entity.setOpenSocial(convert(icon.getOpenSocial()));
        entity.setUrl(icon.getUrl());
        entity.setWidth(icon.getWidth());
        return entity;
    }

    public Map convert (ExtendableBean bean) {
        if(bean == null) return null;
        Map entity = new HashMap();
        entity.putAll(bean);
        return entity;
    }

    public ExtendableBean convert(Map map){
        if(map == null) return null;
        ExtendableBean bean = new ExtendableBeanImpl();
        bean.putAll(map);
        return bean;
    }

}
