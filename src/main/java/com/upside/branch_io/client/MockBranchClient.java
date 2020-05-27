package com.upside.branch_io.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful stub client that doesn't attempt to connect to Branch.io and just returns a formulaic link
 * based on its input params
 */
public class MockBranchClient implements BranchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClient.class);

    @Override
    public URI createLink(String alias, String campaign, String channel, Map<String, String> data) {
        try {
            LOGGER.warn("Creating a link with a Mock client.");
            return new URI("http://foo.com/" + alias);
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Couldn't create a fake link from MockBranchClient", ex);
        }
    }
}
