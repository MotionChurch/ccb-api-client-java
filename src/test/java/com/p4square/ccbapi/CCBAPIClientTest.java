package com.p4square.ccbapi;

import com.p4square.ccbapi.exception.CCBErrorResponseException;
import com.p4square.ccbapi.exception.CCBRetryableErrorException;
import com.p4square.ccbapi.model.*;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDate;

import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the CCBAPIClient.
 */
public class CCBAPIClientTest {

    private HTTPInterface mockHttpClient;
    private CCBAPIClient client;

    @Before
    public void setUp() throws Exception {
        mockHttpClient = EasyMock.mock(HTTPInterface.class);
        client = new TestCCBAPIClient(new URI("https://localhost:8080/api.php"), mockHttpClient);
    }

    @Test
    public void testClose() throws Exception {
        // Set expectation.
        mockHttpClient.close();
        EasyMock.replay(mockHttpClient);

        // Test close.
        client.close();

        // Verify results.
        EasyMock.verify(mockHttpClient);
    }

    @Test
    public void testGetCustomFieldLabelsSuccess() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=custom_field_labels");
        InputStream is = getClass().getResourceAsStream("model/ccb_custom_field_labels_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test custom_field_labels.
        GetCustomFieldLabelsResponse response = client.getCustomFieldLabels();

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(27, response.getCustomFields().size());
        assertEquals("udf_ind_text_1", response.getCustomFields().get(0).getName());
        assertEquals("Favorite Movie", response.getCustomFields().get(0).getLabel());
        assertEquals(false, response.getCustomFields().get(0).isAdminOnly());
    }

    @Test
    public void testGetCustomFieldLabelsError() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=custom_field_labels");
        InputStream is = getClass().getResourceAsStream("model/ccb_error_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        try {
            // Call getCustomFieldLabels() and expect an exception.
            client.getCustomFieldLabels();
            fail("No exception thrown.");

        } catch (CCBErrorResponseException e) {
            // Assert error was received.
            EasyMock.verify(mockHttpClient);
            assertEquals(1, e.getErrors().size());
            assertEquals(2, e.getErrors().get(0).getNumber());
        }
    }

    @Test(expected = CCBRetryableErrorException.class)
    public void testGetCustomFieldLabelsTimeout() throws Exception {
        // Setup mocks.
        EasyMock.expect(mockHttpClient.sendPostRequest(EasyMock.anyObject(URI.class), EasyMock.anyObject()))
                .andThrow(new CCBRetryableErrorException("Retryable error"));
        EasyMock.replay(mockHttpClient);

        // Call getCustomFieldLabels() and expect an exception.
        client.getCustomFieldLabels();
    }

    @Test(expected = IOException.class)
    public void testGetCustomFieldLabelsException() throws Exception {
        // Setup mocks.
        EasyMock.expect(mockHttpClient.sendPostRequest(EasyMock.anyObject(URI.class), EasyMock.anyObject()))
                .andThrow(new IOException());
        EasyMock.replay(mockHttpClient);

        // Call getCustomFieldLabels() and expect an exception.
        client.getCustomFieldLabels();
    }

    @Test
    public void testGetLookupTableListSuccess() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=significant_event_list");
        InputStream is = getClass().getResourceAsStream("model/ccb_lookup_table_list_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test significant_event_list.
        GetLookupTableResponse response = client.getLookupTable(
                new GetLookupTableRequest().withType(LookupTableType.SIGNIFICANT_EVENT));

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(5, response.getItems().size());
        assertEquals(1, response.getItems().get(0).getId());
        assertEquals(1, response.getItems().get(0).getOrder());
        assertEquals("High School Graduation", response.getItems().get(0).getName());
    }

    @Test
    public void testGetCampusList() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=campus_list");
        InputStream is = getClass().getResourceAsStream("model/ccb_campus_list_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test significant_event_list.
        GetCampusListResponse response = client.getCampusList();

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getCampuses().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLookupTableListWithNullType() throws Exception {
        // This should throw an IllegalArgumentException.
        client.getLookupTable(new GetLookupTableRequest());
    }

    @Test
    public void testGetIndividualProfilesById() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=individual_profile_from_id&individual_id=48");
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test individual_profile_from_id.
        GetIndividualProfilesRequest request = new GetIndividualProfilesRequest().withIndividualId(48);
        GetIndividualProfilesResponse response = client.getIndividualProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    @Test
    public void testGetIndividualProfilesByLogin() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=individual_profile_from_login_password");
        byte[] expectedForm = "login=user&password=pass".getBytes();
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(EasyMock.eq(expectedURI), EasyMock.aryEq(expectedForm)))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test individual_profile_from_login_password.
        GetIndividualProfilesRequest request = new GetIndividualProfilesRequest()
                .withLoginPassword("user", "pass".toCharArray());
        GetIndividualProfilesResponse response = client.getIndividualProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    @Test
    public void testGetIndividualProfilesByRoutingAndAccount() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?"
                + "srv=individual_profile_from_micr&account_number=4567&routing_number=1234");
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test individual_profile_from_micr.
        GetIndividualProfilesRequest request = new GetIndividualProfilesRequest().withMICR("1234", "4567");
        GetIndividualProfilesResponse response = client.getIndividualProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    @Test
    public void testGetAllIndividualProfiles() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=individual_profiles");
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test individual_profiles without any options.
        GetIndividualProfilesRequest request = new GetIndividualProfilesRequest();
        GetIndividualProfilesResponse response = client.getIndividualProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    @Test
    public void testGetAllIndividualProfilesWithOptions() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=individual_profiles"
                + "&per_page=15&include_inactive=true&modified_since=2016-03-19&page=5");
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test individual_profiles with all the options.
        GetIndividualProfilesRequest request = new GetIndividualProfilesRequest()
                .withModifiedSince(LocalDate.parse("2016-03-19"))
                .withIncludeInactive(true)
                .withPage(5)
                .withPerPage(15);
        GetIndividualProfilesResponse response = client.getIndividualProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    @Test
    public void testUpdateIndividual() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=update_individual&individual_id=48");
        InputStream is = getClass().getResourceAsStream("model/ccb_individual_profile_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(EasyMock.eq(expectedURI),
                    EasyMock.aryEq("legal_first_name=Larabar".getBytes("UTF-8"))))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        UpdateIndividualProfileRequest request = new UpdateIndividualProfileRequest()
                .withIndividualId(48)
                .withLegalFirstName("Larabar");

        // Test update_individual.
        UpdateIndividualProfileResponse response = client.updateIndividualProfile(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getIndividuals().size());
        assertEquals(48, response.getIndividuals().get(0).getId());
    }

    /**
     * This test ensures {@link CCBAPIClient#updateIndividualProfile(UpdateIndividualProfileRequest)}
     * throws an exception if the individualId property isn't set on the request.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateIndividualWithoutIndividualId() throws Exception {
        // Set expectation.
        UpdateIndividualProfileRequest request = new UpdateIndividualProfileRequest()
                .withLegalFirstName("Larabar");

        // Test update_individual.
        UpdateIndividualProfileResponse response = client.updateIndividualProfile(request);

       // Expect exception.
    }

    @Test
    public void testGetGroupProfilesById() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=group_profile_from_id&id=750");
        InputStream is = getClass().getResourceAsStream("model/ccb_group_profile_from_id_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test group_profile_from_id.
        GetGroupProfilesRequest request = new GetGroupProfilesRequest().withGroupId(750);
        GetGroupProfilesResponse response = client.getGroupProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getGroups().size());
        assertEquals(750, response.getGroups().get(0).getId());
    }

    @Test
    public void testGetAllGroupProfiles() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=group_profiles");
        InputStream is = getClass().getResourceAsStream("model/ccb_group_profile_from_id_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test group_profile_from_id.
        GetGroupProfilesRequest request = new GetGroupProfilesRequest();
        GetGroupProfilesResponse response = client.getGroupProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getGroups().size());
        assertEquals(750, response.getGroups().get(0).getId());
    }

    @Test
    public void testGetAllGroupProfilesWithOptions() throws Exception {
        // Set expectation.
        URI expectedURI = new URI("https://localhost:8080/api.php?srv=group_profiles&per_page=2&modified_since=2018-07-08&page=1&include_participants=false&include_image_link=true");
        InputStream is = getClass().getResourceAsStream("model/ccb_group_profile_from_id_response.xml");
        EasyMock.expect(mockHttpClient.sendPostRequest(expectedURI, null))
                .andReturn(is);
        EasyMock.replay(mockHttpClient);

        // Test group_profile_from_id.
        GetGroupProfilesRequest request = new GetGroupProfilesRequest()
                .withIncludeParticipants(false)
                .withIncludeImageUrl(true)
                .withModifiedSince(LocalDate.parse("2018-07-08"))
                .withPage(1)
                .withPerPage(2);
        GetGroupProfilesResponse response = client.getGroupProfiles(request);

        // Verify results.
        EasyMock.verify(mockHttpClient);
        assertNull(response.getErrors());
        assertEquals(1, response.getGroups().size());
        assertEquals(750, response.getGroups().get(0).getId());
    }

    /**
     * Simple extension of CCBAPIClient to swap out the HTTPInterface with a mock.
     */
    private final class TestCCBAPIClient extends CCBAPIClient {
        public TestCCBAPIClient(final URI apiUri, final HTTPInterface mockHttpClient) {
            super(apiUri, mockHttpClient);
        }
    }
}