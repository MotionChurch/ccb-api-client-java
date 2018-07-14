package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Enumeration of the supported values for the membership_type field of a GroupProfile.
 */
public enum MembershipType {
    /**
     * Membership is open to anyone.
     */
    @XmlEnumValue("Open to All") OPEN,

    /**
     * Membership is moderated (Invitation or Request Required).
     */
    @XmlEnumValue("Invitation or Request Required") MODERATED;
}
