package com.psyco.twitchwidget.twitchapi.api.blocks;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.user.UserResponse;

public class BlockResponse extends APIResponse {

    @SerializedName("updated_at")   private String updatedAt;
    @SerializedName("user")         private UserResponse user;
    @SerializedName("_id")          private long id;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public UserResponse getUser() {
        return user;
    }

    public long getId() {
        return id;
    }
}
