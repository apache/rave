package org.apache.rave.rest.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PageUser", propOrder = {
        "editor", "personId"
})
@XmlRootElement(name = "PageUser")
public class PageUser {
    @XmlElement(name="editor")
    boolean editor;
    @XmlElement(name="personId")
    String personId;

    public PageUser() { }

    public PageUser(org.apache.rave.model.PageUser member) {
        this.editor = member.isEditor();
        this.personId = member.getUserId();
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
}
