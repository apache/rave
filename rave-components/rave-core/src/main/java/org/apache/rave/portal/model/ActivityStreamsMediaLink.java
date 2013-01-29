package org.apache.rave.portal.model;


import java.util.HashMap;

public interface ActivityStreamsMediaLink {


    public Integer getDuration() ;

    public void setDuration(Integer duration) ;

    public Integer getHeight() ;

    public void setHeight(Integer height);

    public String getId();

    public Integer getWidth() ;

    public void setWidth(Integer width) ;

    public void setUrl(String url) ;

    public String getUrl() ;


    public void setOpenSocial(HashMap social);
    public HashMap getOpenSocial();

}
