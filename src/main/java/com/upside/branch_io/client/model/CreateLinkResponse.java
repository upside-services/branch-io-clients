package com.upside.branch_io.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import static javafx.scene.input.KeyCode.J;

/**
 * Created by bsiemon on 11/8/16.
 */
@AutoValue
public abstract class CreateLinkResponse {

    @JsonCreator
    public static CreateLinkResponse create(@JsonProperty("url") String url) {
        return new AutoValue_CreateLinkResponse(url);
    }

    public abstract String getUrl();
}