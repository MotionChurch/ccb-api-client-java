package com.p4square.ccbapi.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for parsing GetGroupProfilesResponse.
 */
public class GetGroupProfilesResponseTest extends XmlBinderTestBase {

    /**
     * Assert that all of the fields bind appropriately for a single profile response.
     */
    @Test
    public void testGetGroupProfilesResponse() throws Exception {
        final GetGroupProfilesResponse response = parseFile("ccb_group_profile_from_id_response.xml",
                GetGroupProfilesResponse.class);

        assertNull("Response should not have errors", response.getErrors());
        assertNotNull(response.getGroups());
        assertEquals(1, response.getGroups().size());

        final GroupProfile group = response.getGroups().get(0);

        // IDs
        assertEquals(750, group.getId());

        assertEquals("Adamant by Lisa Bevere Book Study", group.getName());
        assertTrue(group.getDescription().startsWith("What is the truth?"));
        assertTrue(group.getImageUrl().isEmpty());
        assertEquals("webcal://example.ccbchurch.com/group_calendar.ics?id=750&tk=764EFA883DDA1E11DB47671C4A3BBD9E",
                group.getCalendarFeedUrl());

        // Main Leader
        assertEquals(26102, group.getMainLeader().getId());
        assertEquals("Jane", group.getMainLeader().getFirstName());
        assertEquals("Doe", group.getMainLeader().getLastName());
        assertEquals("Jane Doe", group.getMainLeader().getFullName());
        assertEquals("jane.doe@example.com", group.getMainLeader().getEmail());
        assertEquals(Phone.Type.CONTACT, group.getMainLeader().getPhones().get(0).getType());
        assertEquals("+12068675309", group.getMainLeader().getPhones().get(0).getNumber());

        // Coach
        assertEquals(29, group.getCoach().getId());
        assertEquals("John", group.getCoach().getFirstName());
        assertEquals("Doe", group.getCoach().getLastName());
        assertEquals("John Doe", group.getCoach().getFullName());
        assertEquals("john.doe@example.com", group.getCoach().getEmail());
        assertEquals("", group.getCoach().getPhones().get(0).getNumber());

        // Director
        assertEquals(33082, group.getDirector().getId());
        assertEquals("Jeff", group.getDirector().getFirstName());
        assertEquals("Doe", group.getDirector().getLastName());
        assertEquals("Jeff Doe", group.getDirector().getFullName());
        assertEquals("jeff.doe@example.com", group.getDirector().getEmail());
        assertEquals("", group.getDirector().getPhones().get(0).getNumber());

        // Membership Capacity
        assertEquals(0, group.getLeaders().size());
        assertEquals(0, group.getParticipants().size());
        assertEquals(1, group.getCurrentMembers());
        assertNull(group.getGroupCapacity());
        assertTrue(group.isGroupCapacityUnlimited());

        // Address
        assertEquals(Address.Type.MEETING, group.getAddresses().get(0).getType());
        assertEquals("1234 Example St", group.getAddresses().get(0).getStreetAddress());
        assertEquals("Puyallup", group.getAddresses().get(0).getCity());
        assertEquals("WA", group.getAddresses().get(0).getState());
        assertEquals("", group.getAddresses().get(0).getZip());
        assertEquals("-122.000000", group.getAddresses().get(0).getLongitude());
        assertEquals("47.000000", group.getAddresses().get(0).getLatitude());
        assertEquals("1234 Example St", group.getAddresses().get(0).getLine_1());
        assertEquals("Puyallup, WA", group.getAddresses().get(0).getLine_2());

        // Attributes
        assertEquals(false, group.isChildcareProvided());
        assertEquals(true, group.isListed());
        assertEquals(true, group.isPublicSearchListed());
        assertEquals(true, group.isActive());
        assertEquals(true, group.isNotification());
        assertEquals(InteractionType.MEMBERS_INTERACT, group.getInteractionType());
        assertEquals(MembershipType.MODERATED, group.getMembershipType());

        // User Defined Fields
        assertEquals("udf_1", group.getCustomPulldownFields().getByLabel("Gender").getName());
        assertEquals("Gender", group.getCustomPulldownFields().getByLabel("Gender").getLabel());
        assertEquals(2, group.getCustomPulldownFields().getByLabel("Gender").getSelection().getId());
        assertEquals("Female only", group.getCustomPulldownFields().getByLabel("Gender").getSelection().getLabel());
        assertEquals(false, group.getCustomPulldownFields().getByLabel("Gender").isAdminOnly());

        // Reference Attributes
        assertReferenceEquals(4, "Tuesday", group.getMeetingDay());
        assertReferenceEquals(33, "7:00 pm", group.getMeetingTime());
        assertReferenceEquals(4, "Community", group.getGroupType());
        assertReferenceEquals(13, "Adults", group.getDepartment());
        assertReferenceEquals(18, "Puyallup", group.getArea());
    }

    private void assertReferenceEquals(int id, String name, Reference ref) {
        assertEquals(id, ref.getId());
        assertEquals(name, ref.getName());
    }
}