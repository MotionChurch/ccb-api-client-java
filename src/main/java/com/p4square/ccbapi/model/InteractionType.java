package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Enumeration of the supported values for the interaction_type field of a GroupProfile.
 */
public enum InteractionType {
    @XmlEnumValue("Announcement Only") ANNOUNCEMENT_ONLY,
    @XmlEnumValue("Members Interact") MEMBERS_INTERACT,
    @XmlEnumValue("Administrative") ADMINISTRATIVE;
}
