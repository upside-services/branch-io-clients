package com.upside.branch_io.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/** Request model for creating a new link. */
@AutoValue
public abstract class CreateLinkRequest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    public static final String BRANCH_KEY = "branch_key";
    public static final String DATA = "data";
    public static final String TAGS = "tags";
    public static final String CHANNEL = "channel";
    public static final String CAMPAIGN = "campaign";
    public static final String ALIAS = "alias";
    public static final String LINK_TYPE = "type";

    @JsonCreator
    public static CreateLinkRequest create(
            @JsonProperty(BRANCH_KEY) String branchKey,
            @JsonProperty(ALIAS) String alias,
            @JsonProperty(LINK_TYPE) int linkType,
            @JsonProperty(CHANNEL) String channel,
            @JsonProperty(CAMPAIGN) String campaign,
            @JsonProperty(TAGS) List<String> tags,
            @JsonProperty(DATA) String data) {
        return new AutoValue_CreateLinkRequest(
                branchKey, alias, linkType, channel, campaign, tags, convertData(data));
    }

    @JsonProperty(BRANCH_KEY)
    public abstract String getBranchKey();

    @JsonProperty(ALIAS)
    public abstract String getAlias();

    @JsonProperty(LINK_TYPE)
    public abstract int getLinkType();

    @JsonProperty(CHANNEL)
    public abstract String getChannel();

    @JsonProperty(CAMPAIGN)
    public abstract String getCampaign();

    @JsonProperty(TAGS)
    public abstract List<String> getTags();

    @JsonProperty(DATA)
    public String getRawData() {
        return convertData(getData());
    }

    @JsonIgnore
    public abstract Map<String, String> getData();

    public static String convertData(Map<String, String> data) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> convertData(String data) {
        try {
            return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
