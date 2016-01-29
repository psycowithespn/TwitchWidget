package com.psyco.twitchwidget.twitchapi.api;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public abstract class APIResponse {

    @SerializedName("_links")   private Map<String, String> links;
    private int statusCode;
    @SerializedName("error")    private String error;
    @SerializedName("message")  private String message;

    public int getStatusCode() {
        return (int) statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return this.error != null;
    }

    public String getLink(String type) {
        return links.get(type);
    }
}
