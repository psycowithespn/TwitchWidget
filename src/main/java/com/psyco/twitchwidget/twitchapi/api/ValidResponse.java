package com.psyco.twitchwidget.twitchapi.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ValidResponse extends APIResponse {

    @SerializedName("_links") private Map<String, String> links;

    public String getLink(String type) {
        return links.get(type);
    }
}
