package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.user.UserResponse;

public class UserFollowResponse extends APIResponse {

    @SerializedName("created_at")       private String createdAt;
    @SerializedName("notifications")    private boolean notifications;
    @SerializedName("user")             private UserResponse user;

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean doesNotify() {
        return notifications;
    }

    public UserResponse getUser() {
        return user;
    }
}
