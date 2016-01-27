package com.psyco.twitchwidget.twitchapi.api.user;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;

import java.time.Instant;

public class UserResponse extends ValidResponse {

    @SerializedName("type")         private String type;
    @SerializedName("name")         private String name;
    @SerializedName("created_at")   private String createdAt;
    @SerializedName("updated_at")   private String updatedAt;
    @SerializedName("logo")         private String logoURL;
    @SerializedName("_id")          private long id;
    @SerializedName("display_name") private String displayName;
    @SerializedName("bio")          private String bio;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBio() {
        return bio;
    }
}
