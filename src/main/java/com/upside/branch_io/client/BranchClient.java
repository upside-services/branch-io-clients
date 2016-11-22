package com.upside.branch_io.client;

import java.net.URI;
import java.util.Map;

/**
 * <p>Interface for any implementing client to the branch.io service</p>
 */
public interface BranchClient {

    URI createLink(String alias, String campaign, String channel, Map<String, String> data);
}
