package com.upside.branch_io.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.upside.branch_io.client.BranchClientFactory.ClientType.MOCK;
import static com.upside.branch_io.client.BranchClientFactory.ClientType.REAL;

/**
 * <p>Dropwizard-style Factory that can map from a JSON/YAML configuration file and supports creation of a real client
 * or a mock client.</p>
 */
public class BranchClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClientFactory.class);

    private static final String API_KEY = "apiKey";
    private static final String API_SECRET = "apiSecret";
    private static final String CLIENT_TYPE = "clientType";
    private static final String RETRY_ATTEMPTS = "retryAttempts";

    @JsonProperty(API_KEY)
    private String apiKey = "";

    @JsonProperty(API_SECRET)
    private String apiSecret = "";

    @JsonProperty(CLIENT_TYPE)
    private ClientType clientType = MOCK;

    @JsonProperty(RETRY_ATTEMPTS)
    private int retryAttempts = 0;

    public static enum ClientType {
        REAL,
        MOCK
    }

    public BranchClient build() {
        BranchClient client;
        if (MOCK == this.clientType) {
            client = new MockBranchClient();
        }
        else if(Strings.isNullOrEmpty(this.apiKey) && Strings.isNullOrEmpty(this.apiSecret)) {
            // Assume that if the user hasn't provided an apiKey or apiSecret that we should
            // fail gracefully and provide a Mock client.
            LOGGER.warn("Because Branch.IO apiKey and apiSecret are undefined, defaulting to MockBranchClient.");
            this.clientType = MOCK;
            client = new MockBranchClient();
        }
        else {
            client = RetrofitBranchClient.create(BranchCredentials.create(this.apiKey, this.apiSecret));
        }

        // Any non-zero retryAttempts value means we should wrap our actual client with a retrying client
        if (this.retryAttempts > 0) {
            client = new RetryingBranchClient(client, this.retryAttempts);
        }

        LOGGER.info("Factory returning client of type '{}'", client.getClass());
        return client;
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

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        try {
            this.clientType = ClientType.valueOf(clientType);
        }
        catch (Exception e) {
            LOGGER.error("Unknown Branch.ClientType '{}', defaulting to a MOCK client", clientType);
            this.clientType = MOCK;
        }
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }
}
