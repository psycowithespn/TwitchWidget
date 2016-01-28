package com.psyco.twitchwidget.twitchapi.api.root;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;

public class RootResponse extends APIResponse {

    @SerializedName("token") private Token token;

    public Token getToken() {
        return token;
    }
}
