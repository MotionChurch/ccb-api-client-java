package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.*;

/**
 * Representation of a United States postal address.
 */
@XmlRootElement(name="address")
@XmlAccessorType(XmlAccessType.NONE)
public class Address {
    @XmlType(namespace="Address")
    public enum Type {
        @XmlEnumValue("mailing") MAILING,
        @XmlEnumValue("home") HOME,
        @XmlEnumValue("work") WORK,
        @XmlEnumValue("other") OTHER,
        @XmlEnumValue("meeting") MEETING;
    }

    @XmlAttribute(name="type")
    private Type type;

    @XmlElement(name="street_address")
    private String streetAddress;

    @XmlElement(name="city")
    private String city;

    @XmlElement(name="state")
    private String state;

    @XmlElement(name="zip")
    private String zip;

    @XmlElement(name="country")
    private Country country;

    @XmlElement(name="line_1")
    private String line_1;

    @XmlElement(name="line_2")
    private String line_2;

    @XmlElement(name="latitude")
    private String latitude;

    @XmlElement(name="longitude")
    private String longitude;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        updateAddressLines();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        updateAddressLines();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if (state.length() < 2 || state.length() > 3) {
            throw new IllegalArgumentException("Invalid state code.");
        }
        this.state = state.toUpperCase();
        updateAddressLines();
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
        updateAddressLines();
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
        updateAddressLines();
    }

    public String getLine_1() {
        return line_1;
    }

    public String getLine_2() {
        return line_2;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    private void updateAddressLines() {
        this.line_1 = streetAddress;
        this.line_2 = String.format("%s, %s %s", city, state, zip);
    }
}
