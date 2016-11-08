package com.upside.branch_io.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.upside.branch_io.client.api.BranchAPI;
import com.upside.branch_io.client.model.CreateLinkRequest;
import com.upside.branch_io.client.model.CreateLinkResponse;
import com.upside.branch_io.client.model.LinkType;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by bsiemon on 11/8/16.
 */
public class TestBranchAPI {

    public static final String BRANCH_KEY = System.getProperty("branch_api_key");

    @Test
    public void testCreateLink() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.branch.io/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        BranchAPI api = retrofit.create(BranchAPI.class);

        String alias = UUID.randomUUID().toString().substring(0, 8);

        CreateLinkRequest req = CreateLinkRequest.create(
                BRANCH_KEY,
                alias,
                LinkType.DEFAULT.getCode(),
                "email",
                "beta",
                ImmutableList.of("tag1", "tag2"),
                CreateLinkRequest.convertData(ImmutableMap.of("promoKey", "tag1"))
        );

        Call<CreateLinkResponse> call = api.createLink(req);
        Response<CreateLinkResponse> res = call.execute();

        assertEquals("Wrong status code.", res.code(), 200);

        CreateLinkResponse createLinkResponse = res.body();

        assertTrue("Link malformed", createLinkResponse.getUrl().endsWith(alias));
    }

    @Test
    public void testCreateLinkWithDefaults() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.branch.io/v1/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        BranchAPI api = retrofit.create(BranchAPI.class);

        CreateLinkRequest req = CreateLinkRequest.create(
                BRANCH_KEY,
                "",
                LinkType.DEFAULT.getCode(),
                "",
                "",
                ImmutableList.of("tag1", "tag2"),
                CreateLinkRequest.convertData(ImmutableMap.of("promoKey", "tag1"))
        );

        Call<CreateLinkResponse> call = api.createLink(req);
        Response<CreateLinkResponse> res = call.execute();

        assertEquals("Wrong status code.", res.code(), 200);

        CreateLinkResponse createLinkResponse = res.body();
    }
}
