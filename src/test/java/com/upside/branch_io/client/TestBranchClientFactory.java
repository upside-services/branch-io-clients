package com.upside.branch_io.client;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * <p>Tests our factory creates the expected kinds of clients</p>
 */
public class TestBranchClientFactory {

    @Test
    public void testRealClientCreation() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType("REAL");
        factory.setApiKey("foo");
        factory.setApiSecret("secret");
        assertTrue("Expected a real client", factory.build() instanceof RetrofitBranchClient);
    }


    @Test
    public void testRetryingClientCreation() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType("REAL");
        factory.setApiKey("foo");
        factory.setApiSecret("secret");
        factory.setRetryAttempts(1);
        assertTrue("Expected a real retrying client", factory.build() instanceof RetryingBranchClient);
    }

    @Test
    public void testMockClientCreation() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType("MOCK");
        assertTrue("Expected a mock client", factory.build() instanceof MockBranchClient);
    }

    @Test
    public void testRetryingMockClientCreation() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType("MOCK");
        factory.setRetryAttempts(2);
        assertTrue("Expected a mock client", factory.build() instanceof RetryingBranchClient);
    }

    @Test
    public void testUnsetVariablesResultsInMockClient() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType("");
        assertTrue("Expected a mock client", factory.build() instanceof MockBranchClient);
    }
}
