package com.psyco.twitchwidget.twitchapi.api.root;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

public class Authorization {

    @SerializedName("scopes")       private List<String> scopes;
    @SerializedName("created_at")   private String createdAt;
    @SerializedName("updated_at")   private String updatedAt;

    public List<String> getScopes() {
        return scopes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
