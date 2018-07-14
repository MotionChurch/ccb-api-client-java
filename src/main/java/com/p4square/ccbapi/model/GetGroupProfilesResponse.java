package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * GetGroupProfilesResponse models the response of a variety of APIs which return one or more {@link GroupProfile}s.
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.NONE)
public class GetGroupProfilesResponse extends CCBAPIResponse {

    @XmlElementWrapper(name = "groups")
    @XmlElement(name="group")
    private List<GroupProfile> groups;

    /**
     * @return The list of groups retrieved from CCB.
     */
    public List<GroupProfile> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupProfile> groups) {
        this.groups = groups;
    }
}
