package org.apache.rave.portal.model;

import org.apache.rave.persistence.BasicEntity;

import javax.persistence.*;

@Entity
@Table(name = "person_property")
public class PersonProperty implements BasicEntity {

    @Id
    @Column(name = "entity_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "personPropertyIdGenerator")
    @TableGenerator(name = "personPropertyIdGenerator", table = "RAVE_PORTAL_SEQUENCES", pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_COUNT", pkColumnValue = "person_property", allocationSize = 1, initialValue = 1)
    private Long entityId;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "value")
    private String value;

    @Basic
    @Column(name = "qualifier")
    private String qualifier;

    @Basic
    @Column(name = "primary_value")
    private Boolean primary;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}