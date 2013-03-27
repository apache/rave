package org.apache.rave.rest.model;


import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pages", propOrder = {
        "pages"
})
@XmlRootElement(name = "Pages")
public class PageList {

    @XmlElement(name = "Page")
    private List<Page> pages;

    public List<Page> getPages() {
        if(pages == null) pages = new ArrayList<Page>();
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
