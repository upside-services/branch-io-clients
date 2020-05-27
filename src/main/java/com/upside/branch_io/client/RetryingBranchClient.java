package com.upside.branch_io.client;

import com.google.common.base.Preconditions;
import java.net.URI;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper around a branch client that will back off and retry some configurable number of times
 * before really giving up and throwing an exception
 */
public class RetryingBranchClient implements BranchClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryingBranchClient.class);
    private static final int[] FIBONACCI = new int[] {1, 1, 2, 3, 5, 8, 13};
    private final BranchClient client;
    private final int maxAttempts;

    /**
     * @param client The actual client to request branch.io create a link for us
     * @param maxAttempts The maximum number of attempts to make before throwing an Exception
     */
    public RetryingBranchClient(BranchClient client, int maxAttempts) {
        Preconditions.checkArgument(
                maxAttempts <= FIBONACCI.length,
                "This retrying wrapper only allows backing off '%s' " + "times",
                FIBONACCI.length);
        this.client = client;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public URI createLink(String alias, String campaign, String channel, Map<String, String> data) {
        int attempt = 0;
        URI link = null;
        do {
            try {
                link = this.client.createLink(alias, campaign, channel, data);
            } catch (Exception e) {
                LOGGER.warn("Caught exception trying to create link", e);
                doWait(attempt);

                if (attempt > this.maxAttempts) {
                    throw new RuntimeException(
                            "Unable to generate a branch IO link after "
                                    + attempt
                                    + " attempts, "
                                    + "giving up and re-throwing the most recent exception from our client",
                            e);
                }
            }
            attempt++;
        } while (link == null);

        return link;
    }

    private void doWait(int attempt) {
        try {
            Thread.sleep(FIBONACCI[attempt] * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
