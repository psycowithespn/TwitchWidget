package com.psyco.twitchwidget.twitchapi.api.blocks;

import com.google.gson.annotations.SerializedName;
import com.psyco.twitchwidget.twitchapi.api.PageableAPICall;

import java.util.List;

public class UserBlocksResponse extends PageableAPICall<BlockResponse> {

    @SerializedName("blocks")   private List<BlockResponse> blockResponses;

    @Override
    public List<BlockResponse> getContents() {
        return blockResponses;
    }

    public List<BlockResponse> getBlockResponses() {
        return blockResponses;
    }

    public String getNextURL() {
        return getLink("next");
    }
}
