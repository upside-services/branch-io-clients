package com.upside.branch_io.client;

import java.net.URL;
import java.util.Map;

/**
 * <p>Interface for any implementing client to the branch.io service</p>
 */
public interface BranchClient {

    URL createLink(String alias, String campaign, String channel, Map<String, String> data);
}
