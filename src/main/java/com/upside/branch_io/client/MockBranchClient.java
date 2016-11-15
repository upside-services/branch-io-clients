package com.upside.branch_io.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Useful stub client that doesn't attempt to connect to Branch.io and just returns a formulaic link based on its input
 * params</p>
 */
public class MockBranchClient implements BranchClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClient.class);

    @Override
    public URL createLink(String alias, String campaign, String channel, Map<String, String> data) {
        try {
            LOGGER.warn("Creating a link with a Mock client.");
            return new URL("http://foo.com/" + alias);
        }
        catch (MalformedURLException ex) {
            throw new RuntimeException("Couldn't create a fake link from MockBranchClient", ex);
        }
    }
}
