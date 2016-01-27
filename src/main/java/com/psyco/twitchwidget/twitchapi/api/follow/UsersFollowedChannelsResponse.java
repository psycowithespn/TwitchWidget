package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;
import com.psyco.twitchwidget.twitchapi.api.channel.ChannelResponse;

import java.util.List;

public class UsersFollowedChannelsResponse extends ValidResponse {

    @SerializedName("_total")   private int total;
    @SerializedName("follows")  private List<ChannelFollowResponse> channels;

    public int getTotal() {
        return total;
    }

    public List<ChannelFollowResponse> getChannels() {
        return channels;
    }

    public String getNextURL() {
        return getLink("next");
    }
}
