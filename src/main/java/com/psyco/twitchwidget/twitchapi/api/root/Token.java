package com.psyco.twitchwidget.twitchapi.api.root;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("authorization")    private Authorization authorization;
    @SerializedName("user_name")        private String username;
    @SerializedName("valid")            private boolean valid;

    public Authorization getAuthorization() {
        return authorization;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValid() {
        return valid;
    }
}
