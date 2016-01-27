package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;

public class StreamOnlineResponse {

    @SerializedName("stream")   private StreamResponse streamResponse;

    public StreamResponse getStreamResponse() {
        return streamResponse;
    }

    public boolean isStreaming() {
        return streamResponse != null;
    }
}
