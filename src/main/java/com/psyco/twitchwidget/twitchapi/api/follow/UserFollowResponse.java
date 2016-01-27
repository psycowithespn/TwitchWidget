package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;
import com.psyco.twitchwidget.twitchapi.api.user.UserResponse;

import java.time.Instant;

public class UserFollowResponse extends ValidResponse {

    @SerializedName("created_at")       private String createdAt;
    @SerializedName("notifications")    private boolean notifications;
    @SerializedName("user")             private UserResponse user;

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean doesNotify() {
        return notifications;
    }

    public UserResponse getUnauthedUser() {
        return user;
    }
}
