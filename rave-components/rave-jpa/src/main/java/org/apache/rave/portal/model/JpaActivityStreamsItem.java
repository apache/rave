package org.apache.rave.portal.model;


import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;

@Entity
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "activities")
@SequenceGenerator(name="activityEntrySequence", sequenceName = "activity_entry_sequence")
@DiscriminatorValue("Item")
public class JpaActivityStreamsItem implements ActivityStreamsItem {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "activityEntrySequence")
    private String id;

    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date published;

    @Basic
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date updated;

    @Basic
    private String url;

    @Basic
    private String objectType;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private HashMap openSocial;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private HashMap extensions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public HashMap getOpenSocial() {
        return openSocial;
    }

    public void setOpenSocial(HashMap openSocial) {
        this.openSocial = openSocial;
    }

    public HashMap getExtensions() {
        return extensions;
    }

    public void setExtensions(HashMap extensions) {
        this.extensions = extensions;
    }
}
