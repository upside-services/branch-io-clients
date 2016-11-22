package com.upside.branch_io.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/**
 * Created by bsiemon on 11/8/16.
 */
@AutoValue
public abstract class CreateLinkResponse {

    @JsonCreator
    public static CreateLinkResponse create(@JsonProperty("url") String uri) {
        return new AutoValue_CreateLinkResponse(uri);
    }

    public abstract String getUri();
}
