package com.p4square.ccbapi;

import com.p4square.ccbapi.exception.CCBErrorResponseException;
import com.p4square.ccbapi.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * CCBAPIClient is an implementation of CCBAPI using the Apache HttpClient.
 *
 * This implementation is built against the API documentations found here:
 * https://designccb.s3.amazonaws.com/helpdesk/files/official_docs/api.html
 *
 * This client is thread-safe.
 */
public class CCBAPIClient implements CCBAPI {

    private static final Map<String, String> EMPTY_MAP = Collections.emptyMap();

    private final URI apiBaseUri;
    private final HTTPInterface httpClient;
    private final CCBXmlBinder xmlBinder;

    /**
     * Create a new CCB API Client.
     *
     * @param church The church identifier used with CCB.
     * @param username The API username.
     * @param password The API password.
     * @throws URISyntaxException If the church parameter contains unsafe URI characters.
     */
    public CCBAPIClient(final String church, final String username, final String password) throws URISyntaxException {
        this(new URI("https://" + church + ".ccbchurch.com/api.php"), username, password);
    }

    /**
     * Create a new CCB API Client.
     *
     * @param apiUri The base URI to use when contacting CCB.
     * @param username The API username.
     * @param password The API password.
     */
    public CCBAPIClient(final URI apiUri, final String username, final String password) {
        this(apiUri, new ApacheHttpClientImpl(apiUri, username, password));
    }

    /**
     * A private constructor which allows for dependency injection.
     *
     * @param apiUri The base URI to use when contacting CCB.
     * @param httpClient The HTTP client used to send requests.
     */
    protected CCBAPIClient(final URI apiUri, final HTTPInterface httpClient) {
        this.apiBaseUri = apiUri;
        this.httpClient = httpClient;
        this.xmlBinder = new CCBXmlBinder();
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    @Override
    public GetIndividualProfilesResponse getIndividualProfiles(GetIndividualProfilesRequest request) throws IOException {
        // Prepare the request.
        String serviceName;
        final Map<String, String> params = new HashMap<>();
        if (request.getId() != 0) {
            // Use individual_profile_from_id (individual_id)
            serviceName = "individual_profile_from_id";
            params.put("individual_id", String.valueOf(request.getId()));

        } else if (request.getLogin() != null && request.getPassword() != null) {
            // Use individual_profile_from_login_password (login, password)
            serviceName = "individual_profile_from_login_password";
            params.put("login", request.getLogin());
            params.put("password", request.getPassword());

        } else if (request.getRoutingNumber() != null && request.getAccountNumber() != null) {
            // Use individual_profile_from_micr (account_number, routing_number)
            serviceName = "individual_profile_from_micr";
            params.put("routing_number", request.getRoutingNumber());
            params.put("account_number", request.getAccountNumber());

        } else {
            // Use individual_profiles
            serviceName = "individual_profiles";
            if (request.getModifiedSince() != null) {
                params.put("modified_since", request.getModifiedSince().toString());
            }
            if (request.getIncludeInactive() != null) {
                params.put("include_inactive", request.getIncludeInactive() ? "true" : "false");
            }
            if (request.getPage() != 0) {
                params.put("page", String.valueOf(request.getPage()));
            }
            if (request.getPerPage() != 0) {
                params.put("per_page", String.valueOf(request.getPerPage()));
            }
        }

        // Send the request and parse the response.
        return makeRequest(serviceName, params, EMPTY_MAP, GetIndividualProfilesResponse.class);
    }

    @Override
    public GetCustomFieldLabelsResponse getCustomFieldLabels() throws IOException {
        return makeRequest("custom_field_labels", EMPTY_MAP, EMPTY_MAP, GetCustomFieldLabelsResponse.class);
    }

    /**
     * Build the URI for a particular service call.
     *
     * @param service The CCB API service to call (i.e. the srv query parameter).
     * @param parameters A map of query parameters to include on the URI.
     * @return The apiBaseUri with the additional query parameters appended.
     */
    private URI makeURI(final String service, final Map<String, String> parameters) {
        try {
            StringBuilder queryStringBuilder = new StringBuilder();
            if (apiBaseUri.getQuery() != null) {
                queryStringBuilder.append(apiBaseUri.getQuery()).append("&");
            }
            queryStringBuilder.append("srv=").append(service);
            for (Map.Entry<String, String> entry: parameters.entrySet()) {
                queryStringBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            return new URI(apiBaseUri.getScheme(), apiBaseUri.getAuthority(), apiBaseUri.getPath(),
                    queryStringBuilder.toString(), apiBaseUri.getFragment());
        } catch (URISyntaxException e) {
            // This shouldn't happen, but needs to be caught regardless.
            throw new AssertionError("Could not construct API URI", e);
        }
    }

    /**
     * Send a request to CCB.
     *
     * @param api The CCB service name.
     * @param params The URL query params.
     * @param form The form body parameters.
     * @param clazz The response class.
     * @param <T> The type of response.
     * @return The response.
     * @throws IOException if an error occurs.
     */
    private <T extends CCBAPIResponse> T makeRequest(final String api, final Map<String, String> params,
                                                     final Map<String, String> form, final Class<T> clazz)
            throws IOException {

        final InputStream entity = httpClient.sendPostRequest(makeURI(api, params), form);
        try {
            T response = xmlBinder.bindResponseXML(entity, clazz);
            if (response.getErrors() != null && response.getErrors().size() > 0) {
                throw new CCBErrorResponseException(response.getErrors());
            }
            return response;
        } finally {
            if (entity != null) {
                entity.close();
            }
        }
    }
}
