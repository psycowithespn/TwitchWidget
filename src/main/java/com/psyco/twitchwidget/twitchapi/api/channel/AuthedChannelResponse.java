package com.psyco.twitchwidget.twitchapi.api.channel;

import com.google.gson.annotations.SerializedName;

public class AuthedChannelResponse extends ChannelResponse {

    @SerializedName("email")        private String email;
    @SerializedName("stream_key")   private String streamKey;

    public String getEmail() {
        return email;
    }

    public String getStreamKey() {
        return streamKey;
    }
}
