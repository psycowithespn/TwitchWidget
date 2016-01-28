package com.psyco.twitchwidget.twitchapi.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class PageableAPICall<T> extends APIResponse {

    // This is set to MAX VALUE to compensate for the lack of total on UserBlockResponse.
    // The pager will be forced to read 1 blank list instead of counting the total.
    @SerializedName("_total")   private int total = Integer.MAX_VALUE;

    public String getNextURL() {
        return getLink("next");
    }

    public int getTotal() {
        return total;
    }

    public abstract List<T> getContents();
}
