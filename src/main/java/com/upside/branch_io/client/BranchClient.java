package com.upside.branch_io.client;

import com.upside.branch_io.client.api.BranchAPI;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

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


    public URL createLink(String alias, Map<String, String> data) {
        return null;
    }
}
