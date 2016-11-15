package com.upside.branch_io.client;

import com.google.common.collect.ImmutableList;
import com.upside.branch_io.client.api.BranchAPI;
import com.upside.branch_io.client.model.CreateLinkRequest;
import com.upside.branch_io.client.model.CreateLinkResponse;
import com.upside.branch_io.client.model.LinkType;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bsiemon on 11/8/16.
 */
public class RetrofitBranchClient implements BranchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClient.class);
    private final BranchCredentials branchCredentials;
    private final BranchAPI branchAPI;

    public static RetrofitBranchClient create(BranchCredentials branchCredentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.branch.io/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        LOGGER.debug("Constructed new BranchClient '{}'", retrofit);

        BranchAPI api = retrofit.create(BranchAPI.class);
        return new RetrofitBranchClient(branchCredentials, api);
    }

    private RetrofitBranchClient(BranchCredentials branchCredentials, BranchAPI branchAPI) {
        this.branchCredentials = branchCredentials;
        this.branchAPI = branchAPI;
    }


    @Override
    public URL createLink(String alias, String campaign, String channel, Map<String, String> data) {
        CreateLinkRequest createLinkRequest = CreateLinkRequest.create(
                this.branchCredentials.getAPIKey(),
                alias,
                LinkType.DEFAULT.getCode(),
                channel,
                campaign,
                ImmutableList.of(),
                CreateLinkRequest.convertData(data));
        Call<CreateLinkResponse> call = this.branchAPI.createLink(createLinkRequest);

        try {
            Response<CreateLinkResponse> response = call.execute();

            if (!response.isSuccessful()) {
                String errorMessage = String.format("status: %s Unable to create new link. error: %s",
                        response.code(), response.errorBody().string());
                LOGGER.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }

            LOGGER.debug("Response code from CreateLinkRequest = {}", response.code());
            return new URL(response.body().getUrl());
        }
        catch (IOException e) {
            LOGGER.error("Exception during BranchAPI create link call '{}'", e);
            throw new RuntimeException(e);
        }
    }
}
