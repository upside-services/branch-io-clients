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
        factory.setClientType(BranchClientFactory.CLIENT_TYPE.REAL);
        factory.setApiKey("foo");
        factory.setApiSecret("secret");
        assertTrue("Expected a real client", factory.build() instanceof RetrofitBranchClient);
    }

    @Test
    public void testMockClientCreation() {
        BranchClientFactory factory = new BranchClientFactory();
        factory.setClientType(BranchClientFactory.CLIENT_TYPE.MOCK);
        assertTrue("Expected a mock client", factory.build() instanceof MockBranchClient);
    }
}
