package com.upside.branch_io.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import static com.upside.branch_io.client.BranchClientFactory.CLIENT_TYPE.MOCK;

/**
 * <p>Dropwizard-style Factory that can map from a JSON/YAML configuration file and supports creation of a real client
 * or a mock client.</p>
 */
public class BranchClientFactory {

    private static final String API_KEY = "apiKey";
    private static final String API_SECRET = "apiSecret";
    private static final String CLIENT_TYPE = "clientType";

    @JsonProperty(API_KEY)
    private String apiKey;

    @JsonProperty(API_SECRET)
    private String apiSecret;

    @JsonProperty(CLIENT_TYPE)
    private CLIENT_TYPE clientType;

    public static enum CLIENT_TYPE {
        REAL,
        MOCK
    }

    public BranchClient build() {
        if (MOCK == this.clientType) {
            return new MockBranchClient();
        }
        else {
            return RetrofitBranchClient.create(BranchCredentials.create(this.apiKey, this.apiSecret));
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public CLIENT_TYPE getClientType() {
        return clientType;
    }

    public void setClientType(CLIENT_TYPE clientType) {
        this.clientType = clientType;
    }

    
}
