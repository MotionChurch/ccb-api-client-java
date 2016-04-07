package com.p4square.ccbapi;

import com.p4square.ccbapi.model.*;

import java.io.Closeable;
import java.io.IOException;

/**
 * CCBAPI is a Java interface for using the Church Community Builder API.
 */
public interface CCBAPI extends Closeable {
    /**
     * Retrieve the set of custom (user-defined) fields and the associated labels.
     *
     * @return A GetCustomFieldLabelsResponse containing the fields.
     * @throws IOException on failure.
     */
    GetCustomFieldLabelsResponse getCustomFieldLabels() throws IOException;

    /**
     * Retrieve the list of items in a particular lookup table.
     *
     * Lookup tables provide the list of options for various pulldown fields.
     *
     * @return A GetLookupTableResponse containing the item ids and labels.
     * @throws IOException on failure.
     */
    GetLookupTableResponse getLookupTable(GetLookupTableRequest request) throws IOException;

    /**
     * Retrieve one or more IndividualProfiles.
     *
     * If any of the following properties are set on the request,
     * this method will return the matching individual, if one exists.
     *
     * <ul>
     *     <li>Individual ID</li>
     *     <li>Login and Password</li>
     *     <li>MICR</li>
     * </ul>
     *
     * If more than one property is included only the first, in the order listed above, will be used.
     * If none of the options are included, all individuals will be returned.
     *
     * The appropriate CCB API will be selected based on the options used.
     *
     * @param request A GetIndividualProfilesRequest.
     * @return A GetIndividualProfilesResponse object on success, including when no individuals match.
     * @throws IOException on failure.
     */
    GetIndividualProfilesResponse getIndividualProfiles(GetIndividualProfilesRequest request) throws IOException;

    /**
     * Update an IndividualProfile.
     *
     * @param request An UpdateIndividualProfileRequest including the fields to modify.
     * @return An UpdateIndividualProfileResponse, including the updated IndividualProfile.
     * @throws IOException on failure.
     */
    UpdateIndividualProfileResponse updateIndividualProfile(UpdateIndividualProfileRequest request) throws IOException;
}
