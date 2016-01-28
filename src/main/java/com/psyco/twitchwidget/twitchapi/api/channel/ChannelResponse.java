package com.psyco.twitchwidget.twitchapi.api.channel;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;

public class ChannelResponse extends APIResponse {

    @SerializedName("mature")                           private boolean mature;
    @SerializedName("status")                           private String status;
    @SerializedName("broadcaster_language")             private String broadcasterLanguage;
    @SerializedName("display_name")                     private String displayName;
    @SerializedName("game")                             private String game;
    @SerializedName("delay")                            private Integer delay;
    @SerializedName("language")                         private String language;
    @SerializedName("_id")                              private long id;
    @SerializedName("name")                             private String name;
    @SerializedName("created_at")                       private String createdAt;
    @SerializedName("updated_at")                       private String updatedAt;
    @SerializedName("logo")                             private String logoURL;
    @SerializedName("banner")                           private String bannerURL;
    @SerializedName("video_banner")                     private String videoBannerURL;
    @SerializedName("background")                       private String backgroundURL;
    @SerializedName("profile_banner")                   private String profileBannerURL;
    @SerializedName("profile_banner_background_color")  private String profileBannerBackgroundColor;
    @SerializedName("partner")                          private boolean partner;
    @SerializedName("url")                              private String channelURL;
    @SerializedName("views")                            private long views;
    @SerializedName("followers")                        private long followers;

    public boolean isMature() {
        return mature;
    }

    public String getChannelStatus() {
        return status;
    }

    public String getBroadcasterLanguage() {
        return broadcasterLanguage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGame() {
        return game;
    }

    public Integer getDelay() {
        return delay;
    }

    public String getLanguage() {
        return language;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public String getBannerURL() {
        return bannerURL;
    }

    public String getVideoBannerURL() {
        return videoBannerURL;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public String getProfileBannerURL() {
        return profileBannerURL;
    }

    public String getProfileBannerBackgroundColor() {
        return profileBannerBackgroundColor;
    }

    public boolean isPartner() {
        return partner;
    }

    public String getChannelURL() {
        return channelURL;
    }

    public long getViews() {
        return views;
    }

    public long getFollowers() {
        return followers;
    }
}
