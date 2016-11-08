package com.upside.branch_io.client.model;

/**
 * Models the different types of branch.io links.
 *
 * @see <a href="https://github.com/BranchMetrics/branch-deep-linking-public-api">Branch IO HTTP API</a>
 */
public enum LinkType {
    ONE_TIME(1),
    MARKETING(2),
    DEFAULT(0);

    private final int code;

    public int getCode() {
        return this.code;
    }

    LinkType(int code) {
        this.code = code;
    }
}
