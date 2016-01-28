package com.psyco.twitchwidget.twitchapi.api.stream;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.PageableAPICall;

import java.util.List;

public class StreamQueryResponse extends PageableAPICall<StreamResponse> {

    @SerializedName("streams")  private List<StreamResponse> streams;

    @Override
    public List<StreamResponse> getContents() {
        return streams;
    }

    public List<StreamResponse> getStreams() {
        return streams;
    }
}
