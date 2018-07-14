package com.p4square.ccbapi.model;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a Group Profile.
 */
@XmlRootElement(name="individual")
@XmlAccessorType(XmlAccessType.NONE)
public class GroupProfile {

    @XmlAttribute(name="id")
    private int id;

    @XmlElement(name="name")
    private String name;

    @XmlElement(name="description")
    private String description;

    @XmlElement(name="image")
    private String imageUrl;

    @XmlElement(name="calendar_feed")
    private String calendarFeedUrl;

    @XmlElement(name="main_leader")
    private IndividualProfile mainLeader;

    @XmlElement(name="coach")
    private IndividualProfile coach;

    @XmlElement(name="director")
    private IndividualProfile director;

    @XmlElementWrapper(name="leaders")
    @XmlElement(name="leader")
    private List<IndividualProfile> leaders;

    @XmlElementWrapper(name="participants")
    @XmlElement(name="participant")
    private List<IndividualProfile> participants;

    @XmlElement(name="current_members")
    private int currentMembers;

    @XmlElement(name="group_capacity")
    private String groupCapacity;

    @XmlElementWrapper(name="addresses")
    @XmlElement(name="address")
    private List<Address> addresses;

    @XmlElement(name="childcare_provided")
    private boolean childcareProvided;

    @XmlElement(name="listed")
    private boolean listed;

    @XmlElement(name="public_search_listed")
    private boolean publicSearchListed;

    @XmlElement(name="inactive")
    private boolean inactive;

    @XmlElement(name="notification")
    private boolean notification;

    @XmlElement(name="interaction_type", defaultValue = "Announcement Only")
    private InteractionType interactionType;

    @XmlElement(name="membership_type", defaultValue = "Invitation")
    private MembershipType membershipType;

    @XmlElementWrapper(name="user_defined_fields")
    @XmlElement(name="user_defined_field")
    private CustomFieldCollection<CustomPulldownFieldValue> customPulldownFields;

    @XmlElement(name="campus")
    private Reference campus;

    @XmlElement(name="group_type")
    private Reference groupType;

    @XmlElement(name="department")
    private Reference department;

    @XmlElement(name="area")
    private Reference area;

    @XmlElement(name="meeting_day")
    private Reference meetingDay;

    @XmlElement(name="meeting_time")
    private Reference meetingTime;

    @XmlElement(name="creator")
    private IndividualReference createdBy;

    @XmlElement(name="created")
    private LocalDateTime createdTime;

    @XmlElement(name="modifier")
    private IndividualReference modifiedBy;

    @XmlElement(name="modified")
    private LocalDateTime modifiedTime;

    public GroupProfile() {
        leaders = new ArrayList<>();
        participants = new ArrayList<>();
        addresses = new ArrayList<>();
        customPulldownFields = new CustomFieldCollection<>();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCalendarFeedUrl() {
        return calendarFeedUrl;
    }

    public void setCalendarFeedUrl(String calendarFeedUrl) {
        this.calendarFeedUrl = calendarFeedUrl;
    }

    public IndividualProfile getMainLeader() {
        return mainLeader;
    }

    public void setMainLeader(IndividualProfile mainLeader) {
        this.mainLeader = mainLeader;
    }

    public IndividualProfile getCoach() {
        return coach;
    }

    public void setCoach(IndividualProfile coach) {
        this.coach = coach;
    }

    public IndividualProfile getDirector() {
        return director;
    }

    public void setDirector(IndividualProfile director) {
        this.director = director;
    }

    public List<IndividualProfile> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<IndividualProfile> leaders) {
        this.leaders = leaders;
    }

    public List<IndividualProfile> getParticipants() {
        return participants;
    }

    public void setParticipants(List<IndividualProfile> participants) {
        this.participants = participants;
    }

    public int getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(int currentMembers) {
        this.currentMembers = currentMembers;
    }

    public Integer getGroupCapacity() {
        if (isGroupCapacityUnlimited()) {
            return null;
        } else {
            return Integer.valueOf(this.groupCapacity);
        }
    }

    public boolean isGroupCapacityUnlimited() {
        return this.groupCapacity == null || "Unlimited".equals(this.groupCapacity);
    }

    public void setGroupCapacity(Integer groupCapacity) {
        if (groupCapacity == null) {
            this.groupCapacity = "Unlimited";
        } else {
            this.groupCapacity = groupCapacity.toString();
        }
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public boolean isChildcareProvided() {
        return childcareProvided;
    }

    public void setChildcareProvided(boolean childcareProvided) {
        this.childcareProvided = childcareProvided;
    }

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    public boolean isPublicSearchListed() {
        return publicSearchListed;
    }

    public void setPublicSearchListed(boolean publicSearchListed) {
        this.publicSearchListed = publicSearchListed;
    }

    public boolean isActive() {
        return !inactive;
    }

    public void setActive(boolean active) {
        this.inactive = !inactive;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionType interactionType) {
        this.interactionType = interactionType;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public CustomFieldCollection<CustomPulldownFieldValue> getCustomPulldownFields() {
        return customPulldownFields;
    }

    public void setCustomPulldownFields(CustomFieldCollection<CustomPulldownFieldValue> customPulldownFields) {
        this.customPulldownFields = customPulldownFields;
    }

    public Reference getCampus() {
        return campus;
    }

    public void setCampus(Reference campus) {
        this.campus = campus;
    }

    public Reference getGroupType() {
        return groupType;
    }

    public void setGroupType(Reference groupType) {
        this.groupType = groupType;
    }

    public Reference getDepartment() {
        return department;
    }

    public void setDepartment(Reference department) {
        this.department = department;
    }

    public Reference getArea() {
        return area;
    }

    public void setArea(Reference area) {
        this.area = area;
    }

    public Reference getMeetingDay() {
        return meetingDay;
    }

    public void setMeetingDay(Reference meetingDay) {
        this.meetingDay = meetingDay;
    }

    public Reference getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Reference meetingTime) {
        this.meetingTime = meetingTime;
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

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
