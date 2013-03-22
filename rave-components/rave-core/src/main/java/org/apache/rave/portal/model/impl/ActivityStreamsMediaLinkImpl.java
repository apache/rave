
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

package org.apache.rave.portal.model.impl;


import org.apache.rave.model.ActivityStreamsMediaLink;

import java.util.Map;


public class ActivityStreamsMediaLinkImpl implements ActivityStreamsMediaLink {

	private static final long serialVersionUID = 1L;

    private Integer duration;

    private Integer height;

    private String url;

    private Integer width;

    private String id;

    private Map openSocial;



	  /**
	   * Create a new MediaLink
	   */
	  public ActivityStreamsMediaLinkImpl() {
	  }

     public String getId(){
         return id;
     }

    public void setId(String id) {
        this.id=id;
    }

	/** {@inheritDoc} */

	  public Integer getDuration() {
	    return duration;
	  }

	  /** {@inheritDoc} */
	  public void setDuration(Integer duration) {
	    this.duration = duration;

	  }

	  /** {@inheritDoc} */

	  public Integer getHeight() {
	    return height;
	  }

	  /** {@inheritDoc} */
	  public void setHeight(Integer height) {
	    this.height = height;

	  }

	  /** {@inheritDoc} */

	  public String getUrl() {
	    return url;
	  }

	  /** {@inheritDoc} */
	  public void setUrl(String url) {
	    this.url = url;

	  }

	  /** {@inheritDoc} */

	  public Integer getWidth() {
	    return width;
	  }

	  /** {@inheritDoc} */
	  public void setWidth(Integer width) {
	    this.width = width;

	  }

    /** {@inheritDoc} */

    public Map getOpenSocial() {
        return openSocial;
    }

    /** {@inheritDoc} */
    public void setOpenSocial(Map openSocial) {
        this.openSocial = openSocial;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityStreamsMediaLinkImpl that = (ActivityStreamsMediaLinkImpl) o;

        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (height != null ? !height.equals(that.height) : that.height != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (openSocial != null ? !openSocial.equals(that.openSocial) : that.openSocial != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = duration != null ? duration.hashCode() : 0;
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (openSocial != null ? openSocial.hashCode() : 0);
        return result;
    }
}
