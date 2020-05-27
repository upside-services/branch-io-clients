package com.upside.branch_io.client.api;

import com.upside.branch_io.client.model.CreateLinkRequest;
import com.upside.branch_io.client.model.CreateLinkResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * BranchAPI for branch.io HTTP API.
 *
 * @see <a href="https://github.com/BranchMetrics/branch-deep-linking-public-api">Branch IO HTTP
 *     API</a>
 */
public interface BranchAPI {
    /**
     * Creates a new deeplink in branch.io and returns the given URI.
     *
     * <p>Using an empty alias and link type of DEFAULT.
     *
     * @param request Initialized {@link CreateLinkRequest}
     * @return A valid {@link CreateLinkResponse}
     */
    @POST("url")
    Call<CreateLinkResponse> createLink(@Body CreateLinkRequest request);
}
