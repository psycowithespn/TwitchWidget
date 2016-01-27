package com.psyco.twitchwidget.twitchapi.api.root;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;

public class RootResponse extends ValidResponse {

    @SerializedName("token") private Token token;

    public Token getToken() {
        return token;
    }
}
