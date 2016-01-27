package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;

public class Preview {

    @SerializedName("small")    private String smallURL;
    @SerializedName("medium")   private String mediumURL;
    @SerializedName("large")    private String largeURL;
    @SerializedName("template") private String templateURL;

    public String getSmallURL() {
        return smallURL;
    }

    public String getMediumURL() {
        return mediumURL;
    }

    public String getLargeURL() {
        return largeURL;
    }

    public String getTemplateURL() {
        return templateURL;
    }
}
