package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.ValidResponse;

import java.util.List;

public class StreamQueryResponse extends ValidResponse {

    @SerializedName("_total")   private int total;
    @SerializedName("streams")  private List<StreamResponse> streams;

    public int getTotal() {
        return total;
    }

    public List<StreamResponse> getStreams() {
        return streams;
    }
}
