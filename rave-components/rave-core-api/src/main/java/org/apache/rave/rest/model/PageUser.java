package org.apache.rave.rest.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PageUser", propOrder = {
        "editor", "personId"
})
@XmlRootElement(name = "PageUser")
public class PageUser {
    @XmlElement(name="editor")
    private boolean editor;
    @XmlElement(name = "status")
    private String status;
    @XmlElement(name="personId")
    private String personId;

    public PageUser() { }

    public PageUser(org.apache.rave.model.PageUser member) {
        this.editor = member.isEditor();
        this.personId = member.getUserId();
        this.status = member.getPageStatus().getPageStatus();
    }

    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
