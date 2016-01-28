package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.channel.ChannelResponse;

public class ChannelFollowResponse extends APIResponse {

    @SerializedName("created_at")       private String createdAt;
    @SerializedName("notifications")    private boolean notify;
    @SerializedName("channel")          private ChannelResponse channel;

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isNotify() {
        return notify;
    }

    public ChannelResponse getChannel() {
        return channel;
    }
}
