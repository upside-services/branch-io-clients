package com.upside.branch_io.client;

import com.google.auto.value.AutoValue;

/** Created by bsiemon on 11/8/16. */
@AutoValue
public abstract class BranchCredentials {

    public static BranchCredentials create(String apiKey, String apiSecret) {
        return new AutoValue_BranchCredentials(apiKey, apiSecret);
    }

    public abstract String getAPIKey();

    public abstract String getAPISecret();
}
