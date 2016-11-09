package com.upside.branch_io.client;

import com.google.common.base.Throwables;
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

/**
 * Created by bsiemon on 11/8/16.
 */
public class BranchClient {
    private final BranchCredentials branchCredentials;
    private final BranchAPI branchAPI;

    public static BranchClient create(BranchCredentials branchCredentials) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.branch.io/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        BranchAPI api = retrofit.create(BranchAPI.class);
        return new BranchClient(branchCredentials, api);
    }

    private BranchClient(BranchCredentials branchCredentials, BranchAPI branchAPI) {
        this.branchCredentials = branchCredentials;
        this.branchAPI = branchAPI;
    }


    public URL createLink(String alias, String capmaign, String channel, Map<String, String> data) {
        CreateLinkRequest createLinkRequest = CreateLinkRequest.create(
                "",
                alias,
                LinkType.DEFAULT.getCode(),
                "",
                "",
                ImmutableList.of(),
                CreateLinkRequest.convertData(data));
        Call<CreateLinkResponse> call = this.branchAPI.createLink(createLinkRequest);

        try {
            Response<CreateLinkResponse> response = call.execute();

            if (!response.isSuccessful()) {
                throw new RuntimeException(String.format(
                        "status: %s Unable to create new link. error: %s",
                        response.code(), response.errorBody().string()));
            }

            return new URL(response.body().getUrl());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
