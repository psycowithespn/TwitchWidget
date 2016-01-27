package com.psyco.twitchwidget.twitchapi.api.user;

import com.google.gson.annotations.SerializedName;

public class AuthedUserResponse extends UserResponse {

    @SerializedName("email")            private String email;
    @SerializedName("partnered")        private boolean partnered;
    @SerializedName("notifications")    private Notifications notifications;

    public String getEmail() {
        return email;
    }

    public boolean isPartnered() {
        return partnered;
    }

    public Notifications getNotifications() {
        return notifications;
    }
}
