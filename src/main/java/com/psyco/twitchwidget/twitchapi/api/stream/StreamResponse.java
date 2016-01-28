package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.channel.ChannelResponse;

public class StreamResponse extends APIResponse {

    @SerializedName("game")         private String game;
    @SerializedName("viewers")      private int viewers;
    @SerializedName("average_fps")  private double averageFPS;
    @SerializedName("delay")        private int delay;
    @SerializedName("video_height") private int videoHeight;
    @SerializedName("is_playlist")  private boolean playlist;
    @SerializedName("created_at")   private String createdAt;
    @SerializedName("_id")          private long id;
    @SerializedName("channel")      private ChannelResponse channel;
    @SerializedName("preview")      private Preview preview;

    public String getGame() {
        return game;
    }

    public int getViewers() {
        return viewers;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public int getDelay() {
        return delay;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public boolean isPlaylist() {
        return playlist;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getId() {
        return id;
    }

    public ChannelResponse getChannel() {
        return channel;
    }

    public Preview getPreview() {
        return preview;
    }
}
