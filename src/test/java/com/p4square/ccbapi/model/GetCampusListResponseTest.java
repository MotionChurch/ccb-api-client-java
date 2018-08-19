package com.p4square.ccbapi.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for parsing GetCampusListResponseTest.
 */
public class GetCampusListResponseTest extends XmlBinderTestBase {

    /**
     * Assert that all of the fields bind appropriately.
     */
    @Test
    public void testGetCampusListResponse() throws Exception {
        final GetCampusListResponse response = parseFile("ccb_campus_list_response.xml",
                GetCampusListResponse.class);

        assertNull("Response should not have errors", response.getErrors());
        assertNotNull(response.getCampuses());
        assertEquals(1, response.getCampuses().size());

        final Campus campus = response.getCampuses().get(0);

        assertEquals(1, campus.getId());
        assertEquals("Sample Church", campus.getName());
        assertEquals(true, campus.isActive());
    }
}