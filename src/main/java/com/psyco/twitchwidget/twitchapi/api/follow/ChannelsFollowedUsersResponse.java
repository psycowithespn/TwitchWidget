package com.psyco.twitchwidget.twitchapi.api.follow;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;

import java.util.List;

public class ChannelsFollowedUsersResponse extends ValidResponse {

    @SerializedName("_total")   private int total;
    @SerializedName("_cursor")  private String cursor;
    @SerializedName("follows")  private List<UserFollowResponse> followers;

    public int getTotal() {
        return total;
    }

    public String getCursor() {
        return cursor;
    }

    public List<UserFollowResponse> getFollowers() {
        return followers;
    }

    public String getNextURL() {
        return getLink("next");
    }
}
