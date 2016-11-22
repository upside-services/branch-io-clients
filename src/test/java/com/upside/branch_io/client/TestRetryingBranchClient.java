package com.upside.branch_io.client;

import com.google.common.collect.ImmutableMap;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.easymock.EasyMock.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * <p>Test our backing-off client</p>
 */
public class TestRetryingBranchClient {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private final static String CODE = "myCode";
    private final static String PROGRAM = "myProgram";
    private final static Map<String, String> DATA_MAP = ImmutableMap.of("upsidePromotionCode", CODE,
                                                                        "programId", PROGRAM);
    private final static String CAMPAIGN = "campaign";
    private final static String CHANNEL = "channel";


    @Test
    public void testWrapperDoesNotInterfereWithHappyPath() throws URISyntaxException {
        BranchClient client = new MockBranchClient();
        BranchClient retryingClient = new RetryingBranchClient(client, 1);

        URI link = retryingClient.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP);
        assertEquals(new URI("http://foo.com/myCode"), link);
    }


    @Test
    public void testOneRetry() throws URISyntaxException {
        BranchClient client = createNiceMock(BranchClient.class);
        expect(client.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP)).andThrow(new RuntimeException("first time fails"));
        expect(client.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP)).andReturn(new URI("http://foo.com/myCode"));
        replay(client);

        BranchClient retryingClient = new RetryingBranchClient(client, 1);
        URI link = retryingClient.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP);
        assertEquals(new URI("http://foo.com/myCode"), link);

        verify(client);
    }

    @Test
    public void testTooManyFailures() throws URISyntaxException {
        BranchClient client = createNiceMock(BranchClient.class);
        expect(client.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP)).andThrow(new RuntimeException("fails")).anyTimes();
        replay(client);

        BranchClient retryingClient = new RetryingBranchClient(client, 1);
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Unable to generate a branch IO link");
        retryingClient.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP);
    }

    @Test
    public void testMaxAttempts() throws URISyntaxException {
        BranchClient client = createNiceMock(BranchClient.class);
        expect(client.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP)).andThrow(new RuntimeException("fails")).times(6);
        expect(client.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP)).andReturn(new URI("http://foo.com/myCode"));
        replay(client);

        BranchClient retryingClient = new RetryingBranchClient(client, 7);
        URI link = retryingClient.createLink(CODE, CAMPAIGN, CHANNEL, DATA_MAP);
        assertEquals(new URI("http://foo.com/myCode"), link);

        verify(client);
    }

    @Test
    public void testMaxAttemptsOutOfBounds() throws URISyntaxException {
        BranchClient client = new MockBranchClient();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("This retrying wrapper only allows backing off '7' times");
        BranchClient retryingClient = new RetryingBranchClient(client, 8);
    }
}
