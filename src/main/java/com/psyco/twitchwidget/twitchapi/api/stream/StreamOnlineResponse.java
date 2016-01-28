package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;

public class StreamOnlineResponse extends APIResponse {

    @SerializedName("stream")   private StreamResponse streamResponse;

    public StreamResponse getStreamResponse() {
        return streamResponse;
    }

    public boolean isStreaming() {
        return streamResponse != null;
    }
}
