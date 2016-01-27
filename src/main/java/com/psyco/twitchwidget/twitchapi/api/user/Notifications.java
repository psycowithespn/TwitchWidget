package com.psyco.twitchwidget.twitchapi.api.user;

import com.google.gson.annotations.SerializedName;

public class Notifications {

    @SerializedName("email")    private boolean email;
    @SerializedName("push")     private boolean push;

    public boolean isEmail() {
        return email;
    }

    public boolean isPush() {
        return push;
    }
}
