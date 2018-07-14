package com.p4square.ccbapi;

import com.p4square.ccbapi.exception.CCBErrorResponseException;
import com.p4square.ccbapi.model.*;
import com.p4square.ccbapi.serializer.FormBuilder;
import com.p4square.ccbapi.serializer.IndividualProfileSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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

    private static final IndividualProfileSerializer INDIVIDUAL_PROFILE_SERIALIZER = new IndividualProfileSerializer();

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
        String form = null;

        if (request.getId() != 0) {
            // Use individual_profile_from_id (individual_id)
            serviceName = "individual_profile_from_id";
            params.put("individual_id", String.valueOf(request.getId()));

        } else if (request.getLogin() != null && request.getPassword() != null) {
            // Use individual_profile_from_login_password (login, password)
            serviceName = "individual_profile_from_login_password";

            FormBuilder loginform = new FormBuilder();
            loginform.appendField("login", request.getLogin());
            /*
                TODO: Don't convert password char[] to String.
                The whole purpose behind keeping the password in a char[] is
                so that it can be zeroed out in the heap when its no longer
                needed.
                Originally, Church Community Builder decided to send the
                user's password, among other sensitive fields, as a query
                parameter. Since the query string had to be a String, I
                converted the password to String here.
                CCB has since switched to POST. But there was no grace period
                to ease the transition. In the interest of fixing the site
                quickly, I'm leaving this TODO incomplete for now.
             */
            loginform.appendField("password", new String(request.getPassword()));
            form = loginform.build();

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
        return makeRequest(serviceName, params, form, GetIndividualProfilesResponse.class);
    }

    @Override
    public GetCustomFieldLabelsResponse getCustomFieldLabels() throws IOException {
        return makeRequest("custom_field_labels", EMPTY_MAP, null, GetCustomFieldLabelsResponse.class);
    }

    @Override
    public GetLookupTableResponse getLookupTable(final GetLookupTableRequest request) throws IOException {

        if (request.getType() == null) {
            throw new IllegalArgumentException("LookupTableType must not be null.");
        }

        final String service = request.getType().getIdentifier() + "_list";

        return makeRequest(service, EMPTY_MAP, null, GetLookupTableResponse.class);
    }

    @Override
    public UpdateIndividualProfileResponse updateIndividualProfile(UpdateIndividualProfileRequest request)
            throws IOException {

        if (request.getIndividualId() == 0) {
            throw new IllegalArgumentException("individualId must be set on the request.");
        }

        final Map<String, String> params = Collections.singletonMap("individual_id",
                                                                    String.valueOf(request.getIndividualId()));
        final String form = INDIVIDUAL_PROFILE_SERIALIZER.encode(request);

        return makeRequest("update_individual", params, form, UpdateIndividualProfileResponse.class);
    }

    @Override
    public GetGroupProfilesResponse getGroupProfiles(GetGroupProfilesRequest request) throws IOException {
        // Prepare the request.
        String serviceName;
        final Map<String, String> params = new HashMap<>();

        if (request.getId() != 0) {
            // Use group_profile_from_id (id)
            serviceName = "group_profile_from_id";
            params.put("id", String.valueOf(request.getId()));

        } else {
            // Use group_profiles
            serviceName = "group_profiles";
            if (request.getModifiedSince() != null) {
                params.put("modified_since", request.getModifiedSince().toString());
            }
            if (request.getPage() != 0) {
                params.put("page", String.valueOf(request.getPage()));
            }
            if (request.getPerPage() != 0) {
                params.put("per_page", String.valueOf(request.getPerPage()));
            }
            if (request.getIncludeParticipants() != null) {
                params.put("include_participants", request.getIncludeParticipants() ? "true" : "false");
            }
        }

        // This option applies to all request types.
        if (request.getIncludeImageUrl() != null) {
            params.put("include_image_link", request.getIncludeImageUrl() ? "true" : "false");
        }

        // Send the request and parse the response.
        return makeRequest(serviceName, params, null, GetGroupProfilesResponse.class);
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
                                                     final String form, final Class<T> clazz)
            throws IOException {

        byte[] payload = null;
        if (form != null) {
            payload = form.getBytes(StandardCharsets.UTF_8);
        }

        final InputStream entity = httpClient.sendPostRequest(makeURI(api, params), payload);
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
