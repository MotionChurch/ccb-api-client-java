package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;

/**
 * Representation of a Campus.
 */
@XmlRootElement(name="campus")
@XmlAccessorType(XmlAccessType.NONE)
public class Campus {

    @XmlAttribute(name="id")
    private int id;

    @XmlElement(name="name")
    private String name;

    @XmlElement(name="active")
    private boolean active;

    @XmlElement(name="creator")
    private IndividualReference createdBy;

    @XmlElement(name="created")
    private LocalDateTime createdTime;

    @XmlElement(name="modifier")
    private IndividualReference modifiedBy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public IndividualReference getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(IndividualReference createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public IndividualReference getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(IndividualReference modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
