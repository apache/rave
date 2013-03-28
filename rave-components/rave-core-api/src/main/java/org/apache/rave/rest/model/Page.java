package org.apache.rave.rest.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Page", propOrder = {
        "id", "name", "ownerId", "pageType", "pageLayoutCode", "subPages", "regions", "members"
})
@XmlRootElement(name = "Page")
public class Page {

    @XmlAttribute(name="id")
    private String id;
    @XmlElement(name="name")
    private String name;
    @XmlElement(name="ownerId")
    private String ownerId;
    @XmlElement(name="pageType")
    private String pageType;
    @XmlElement(name="pageLayoutCode")
    private String pageLayoutCode;
    @XmlElementWrapper(name = "subPages")
    @XmlElement(name="Page")
    private List<Page> subPages;
    @XmlElementWrapper(name = "regions")
    @XmlElement(name="Region")
    private List<Region> regions;
    @XmlElementWrapper(name = "members")
    @XmlElement(name="PageUser")
    private List<PageUser> members;

    public Page() { }

    public Page(org.apache.rave.model.Page source) {
        this.id = source.getId();
        this.name = source.getName();
        this.ownerId = source.getOwnerId();
        this.pageType = source.getPageType().toString();
        this.pageLayoutCode = source.getPageLayout().getCode();
        this.subPages = createSubPages(source);
        this.regions = createRegions(source);
        this.members = createPageUsers(source);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageLayoutCode() {
        return pageLayoutCode;
    }

    public void setPageLayoutCode(String pageLayoutCode) {
        this.pageLayoutCode = pageLayoutCode;
    }

    public List<Page> getSubPages() {
        return subPages;
    }

    public void setSubPages(List<Page> subPages) {
        this.subPages = subPages;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<PageUser> getMembers() {
        return members;
    }

    public void setMembers(List<PageUser> members) {
        this.members = members;
    }

    private List<Page> createSubPages(org.apache.rave.model.Page source) {
        List<Page> created = null;
        List<org.apache.rave.model.Page> subPages = source.getSubPages();
        if(subPages != null) {
            created = new ArrayList<Page>();
            for(org.apache.rave.model.Page subPage : subPages) {
                created.add(new Page(subPage));
            }
        }
        return created;
    }

    private List<Region> createRegions(org.apache.rave.model.Page source) {
        List<Region> created = null;
        List<org.apache.rave.model.Region> regions = source.getRegions();
        if(regions != null) {
            created = new ArrayList<Region>();
            for(org.apache.rave.model.Region region : regions) {
                created.add(new Region(region));
            }
        }
        return created;
    }

    private List<PageUser> createPageUsers(org.apache.rave.model.Page source) {
        List<PageUser> created =null;
        List<org.apache.rave.model.PageUser> members = source.getMembers();
        if(members != null) {
            created = new ArrayList<PageUser>();
            for(org.apache.rave.model.PageUser member : members) {
                created.add(new PageUser(member));
            }
        }
        return created;
    }
}
