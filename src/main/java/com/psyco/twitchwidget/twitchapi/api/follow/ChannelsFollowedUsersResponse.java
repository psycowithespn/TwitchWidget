package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.PageableAPICall;

import java.util.List;

public class ChannelsFollowedUsersResponse extends PageableAPICall<UserFollowResponse> {

    @SerializedName("_cursor")  private String cursor;
    @SerializedName("follows")  private List<UserFollowResponse> followers;

    public String getCursor() {
        return cursor;
    }

    @Override
    public List<UserFollowResponse> getContents() {
        return followers;
    }

    public List<UserFollowResponse> getFollowers() {
        return followers;
    }
}
