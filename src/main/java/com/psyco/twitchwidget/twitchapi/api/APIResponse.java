package com.psyco.twitchwidget.twitchapi.api;

public abstract class APIResponse {

    public boolean isError() {
        return this instanceof ErrorResponse;
    }
}
