package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.PageableAPICall;

import java.util.List;

public class UsersFollowedChannelsResponse extends PageableAPICall<ChannelFollowResponse> {

    @SerializedName("follows")  private List<ChannelFollowResponse> channels;

    @Override
    public List<ChannelFollowResponse> getContents() {
        return channels;
    }

    public List<ChannelFollowResponse> getChannels() {
        return channels;
    }
}
