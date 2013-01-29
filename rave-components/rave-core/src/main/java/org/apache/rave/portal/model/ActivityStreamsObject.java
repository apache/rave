package org.apache.rave.portal.model;

import java.util.Date;
import java.util.List;


public interface ActivityStreamsObject extends ActivityStreamsItem {


    public List<? extends ActivityStreamsObject> getAttachments() ;

    public void setAttachments(List<? extends ActivityStreamsObject> attachments) ;

    public ActivityStreamsObject getAuthor() ;


    public void setAuthor(ActivityStreamsObject author) ;


    public String getContent() ;


    public void setContent(String content) ;


    public String getDisplayName() ;


    public void setDisplayName(String displayName) ;


    public List<String> getDownstreamDuplicates() ;


    public void setDownstreamDuplicates(List<String> downstreamDuplicates) ;


    public ActivityStreamsMediaLink getImage() ;


    public void setImage(ActivityStreamsMediaLink image) ;


    public String getSummary() ;

    public void setSummary(String summary) ;


    public List<String> getUpstreamDuplicates() ;


    public void setUpstreamDuplicates(List<String> upstreamDuplicates) ;

    public String getAlias() ;

    public void setAlias(String alias) ;

    public String getAttendedBy() ;

    public void setAttendedBy(String attendedBy) ;

    public String getAttending() ;

    public void setAttending(String attending) ;

    public String getDc() ;

    public void setDc(String dc) ;

    public Date getEndTime() ;

    public void setEndTime(Date endTime) ;

    public String getFollowers() ;

    public void setFollowers(String followers) ;

    public String getFollowing() ;

    public void setFollowing(String following) ;

    public String getFriend_requests() ;

    public void setFriend_requests(String friend_requests) ;

    public String getFriends() ;

    public void setFriends(String friends) ;

    public String getGeojson() ;

    public void setGeojson(String geojson) ;

    public String getInvited() ;

    public void setInvited(String invited) ;

    public String getLikes() ;

    public void setLikes(String likes) ;

    public String getLd() ;

    public void setLd(String ld) ;

    public String getLinks() ;

    public void setLinks(String links) ;

    public String getLocation() ;

    public void setLocation(String location) ;

    public String getMaybeAttending() ;

    public void setMaybeAttending(String maybeAttending) ;

    public String getMembers() ;

    public void setMembers(String members) ;

    public String getNotAttendedBy() ;

    public void setNotAttendedBy(String notAttendedBy) ;

    public String getMood() ;

    public void setMood(String mood) ;

    public String getNotAttending() ;

    public void setNotAttending(String notAttending) ;

    public String getOdata() ;

    public void setOdata(String odata) ;

    public String getOpengraph() ;

    public void setOpengraph(String opengraph) ;

    public String getRating() ;

    public void setRating(String rating) ;

    public String getReplies() ;

    public void setReplies(String replies) ;

    public String getReviews() ;

    public void setReviews(String reviews) ;

    public String getSaves() ;

    public void setSaves(String saves) ;

    public String getSchema_org() ;

    public void setSchema_org(String schema_org) ;

    public String getShares() ;

    public void setShares(String shares) ;

    public String getSource() ;

    public void setSource(String source) ;

    public Date getStartTime() ;

    public void setStartTime(Date startTime) ;

}
