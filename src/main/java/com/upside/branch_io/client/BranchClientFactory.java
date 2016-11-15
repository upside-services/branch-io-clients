package com.upside.branch_io.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.upside.branch_io.client.BranchClientFactory.CLIENT_TYPE.MOCK;

/**
 * <p>Dropwizard-style Factory that can map from a JSON/YAML configuration file and supports creation of a real client
 * or a mock client.</p>
 */
public class BranchClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClientFactory.class);

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
            LOGGER.info("Returning MockBranchClient");
            return new MockBranchClient();
        }
        else if(Strings.isNullOrEmpty(this.apiKey) && Strings.isNullOrEmpty(this.apiSecret)) {
            // Assume that if the user hasn't provided an apiKey or apiSecret that we should fail gracefully and provide a
            // Mock client.
            LOGGER.warn("Because Branch.IO apiKey and apiSecret are undefined, defaulting to MockBranchClient.");
            return new MockBranchClient();
        }
        else {
            LOGGER.debug("Constructing real RetrofitBranchClient");
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
