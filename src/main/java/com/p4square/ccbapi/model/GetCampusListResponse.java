package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A GetCampusListResponse contains a list of campuses.
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class GetCampusListResponse extends CCBAPIResponse {

    @XmlElementWrapper(name = "campuses")
    @XmlElement(name="campus")
    private List<Campus> campuses;

    public GetCampusListResponse() {
        campuses = new ArrayList<>();
    }

    /**
     * @return The list of campuses.
     */
    public List<Campus> getCampuses() {
        return campuses;
    }

    /**
     * Set the list of campuses.
     *
     * @param campuses  The list of campuses.
     */
    public void setCampuses(final List<Campus> campuses) {
        this.campuses = campuses;
    }
}