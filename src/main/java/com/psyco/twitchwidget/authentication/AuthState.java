package com.psyco.twitchwidget.authentication;

public enum AuthState {
    UNKNOWN,
    ERROR,
    DENIED,
    REQUIRES_MANUAL_AUTH,
    MANUAL_AUTH_SUCCEEDED,
    RENEWED_AUTH;
}
