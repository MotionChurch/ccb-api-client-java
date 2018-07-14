package com.p4square.ccbapi.model;

import java.time.LocalDate;

/**
 * GetGroupProfilesRequest is the set of options for retrieving group profiles.
 *
 * If {@link #withGroupId(int)} is used, this request will return only the
 * requested group.
 *
 * Otherwise, this request allows you to pass in a given date and have all
 * groups created or modified since that date returned to you. If a date is
 * not provided, all groups in the system will be returned.
 *
 * The image link in the image element will expire, and should be cached.
 * Including it will significantly increase the runtime of the service and may
 * cause a timeout. Please consider using {@link #withPerPage(int)} and
 * {@link #withPage(int)} if you want to get the images from your groups.
 */
public class GetGroupProfilesRequest {

    // Used with group_profile_from_id
    private int id;

    // Used with group_profiles
    private LocalDate modifiedSince;
    private int page;
    private int perPage;
    private Boolean includeParticipants;

    // Used with any.
    private Boolean includeImageLink;

    public int getId() {
        return id;
    }

    /**
     * Request the {@link GroupProfile} for the given group id.
     *
     * @param id The id.
     * @return this.
     */
    public GetGroupProfilesRequest withGroupId(final int id) {
        this.id = id;
        return this;
    }

    public Boolean getIncludeImageUrl() {
        return includeImageLink;
    }

    /**
     * Include the image URL in the result.
     *
     * Note: The image link in the image element will expire, and should be cached.
     *
     * @param value True if the image link should be included.
     * @return this.
     */
    public GetGroupProfilesRequest withIncludeImageUrl(final boolean value) {
        this.includeImageLink = value;
        return this;
    }

    public LocalDate getModifiedSince() {
        return modifiedSince;
    }

    /**
     * Retrieve all groups modified since the given time.
     *
     * Note: This cannot be used with {@link #withGroupId(int)}.
     *
     * @param modifiedSince The given time.
     * @return this.
     */
    public GetGroupProfilesRequest withModifiedSince(final LocalDate modifiedSince) {
        this.modifiedSince = modifiedSince;
        return this;
    }

    public int getPage() {
        return page;
    }


    /**
     * Select the page of results when paginating results.
     *
     * Note: This cannot be used with {@link #withGroupId(int)}.
     *
     * Defaults to 1 if {@link #withPerPage(int)} is specified on the request.
     *
     * @param page The starting page number.
     * @return this.
     */
    public GetGroupProfilesRequest withPage(final int page) {
        this.page = page;
        return this;
    }

    public int getPerPage() {
        return perPage;
    }

    /**
     * Limit the number of groups returned.
     *
     * Note: This cannot be used with {@link #withGroupId(int)}.
     *
     * Defaults to 25 if {@link #withPage(int)} is specified on the request.
     *
     * @param perPage The maximum number to return.
     * @return this.
     */
    public GetGroupProfilesRequest withPerPage(final int perPage) {
        this.perPage = perPage;
        return this;
    }

    public Boolean getIncludeParticipants() {
        return includeParticipants;
    }

    /**
     * Include all participants from the group in the response.
     *
     * {@link GroupProfile#getMainLeader()} and {@link GroupProfile#getLeaders()}
     * are always populated, regardless of the value.
     *
     * Note: This cannot be used with {@link #withGroupId(int)}.
     *
     * Defaults to True.
     *
     * @see {@link GroupProfile#getParticipants()}
     *
     * @param includeParticipants
     * @return this.
     */
    public GetGroupProfilesRequest withIncludeParticipants(final boolean includeParticipants) {
        this.includeParticipants = includeParticipants;
        return this;
    }
}
