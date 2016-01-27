package com.psyco.twitchwidget.authentication;

import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.WebPage;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authentication {

    //https://api.twitch.tv/kraken/oauth2/authorize?response_type=token&client_id=q9ons4lq6jzz0qxeyqiwh4h7dglpt4q&redirect_uri=http://localhost&scope=user_read
    private static final Pattern AUTH_TOKEN_PATTERN = Pattern.compile("#access_token=(.+?)&");
    public static final String AUTH_URL = "https://api.twitch.tv/kraken/oauth2/authorize?response_type=token" +
            "&client_id=q9ons4lq6jzz0qxeyqiwh4h7dglpt4q" +
            "&redirect_uri=http://localhost" +
            "&scope=user_read";
    public static final String FORCE_AUTH_URL = AUTH_URL + "&force_verify=true";

    private final Stage stage;
    private final WebView webView;
    private final AuthLoadListenerClient authLoadListenerClient;

    private String authToken;
    private boolean authenticating = false;

    public Authentication() {
        stage = new Stage();
        webView = new WebView();
        authLoadListenerClient = new AuthLoadListenerClient();

        try {
            Field page = WebEngine.class.getDeclaredField("page");
            page.setAccessible(true);
            Field loadListenerClients = WebPage.class.getDeclaredField("loadListenerClients");
            loadListenerClients.setAccessible(true);
            Object webPage = page.get(webView.getEngine());
            Object loadList = loadListenerClients.get(webPage);
            List<LoadListenerClient> listLoadList = (List<LoadListenerClient>) loadList;
            listLoadList.add(authLoadListenerClient);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(webView);
        stage.setScene(scene);
    }

    public boolean authenticate(boolean forceVerify, Consumer<AuthState> successCallback, Consumer<AuthState> errorCallback) {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Must be called from FX Application thread");
        }

        if (authenticating) {
            return false;
        }

        authenticating = true;

        authLoadListenerClient.authState.addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            switch (newValue) {
                case REQUIRES_MANUAL_AUTH:
                    stage.show();
                    break;
                case MANUAL_AUTH_SUCCEEDED:
                case RENEWED_AUTH:
                    stage.close();
                    authenticating = false;
                    successCallback.accept(newValue);
                    break;
                case ERROR:
                case DENIED:
                    stage.close();
                    authenticating = false;
                    errorCallback.accept(newValue);
                    break;
            }
        });
        if (forceVerify) {
            webView.getEngine().load(FORCE_AUTH_URL);
        } else {
            webView.getEngine().load(AUTH_URL);
        }
        return true;
    }

    public boolean isAuthenticated() {
        return authToken != null;
    }

    public String getAuthToken() {
        return authToken;
    }

    private class AuthLoadListenerClient implements LoadListenerClient {

        private static final String TWITCH_ID = "api.twitch.tv/kraken";
        private static final String LOCAL_ID = "http://localhost/";
        private ObjectProperty<AuthState> authState = new SimpleObjectProperty<>(AuthState.UNKNOWN);
        private boolean lastPageTwitch;

        @Override
        public void dispatchLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
            if (errorCode != 0) {
                System.out.println("Error: Authentication failure: " + errorCode + ": " + url);
                authState.setValue(AuthState.ERROR);
                return;
            }

            if (state == PAGE_STARTED || state == PAGE_REDIRECTED || state == PAGE_FINISHED) {
                System.out.println(state + ": " + url);
            }

            switch (state) {
                case PAGE_FINISHED:
                    if (url.contains(TWITCH_ID)) {
                        authState.setValue(AuthState.REQUIRES_MANUAL_AUTH);
                        lastPageTwitch = true;
                    }
                    break;
                case PAGE_REDIRECTED:
                    if (url.contains(LOCAL_ID)) {
                        Matcher matcher = AUTH_TOKEN_PATTERN.matcher(url);
                        if (matcher.find()) {
                            authToken = matcher.group(1);
                            if (lastPageTwitch) {
                                authState.setValue(AuthState.MANUAL_AUTH_SUCCEEDED);
                            } else {
                                authState.setValue(AuthState.RENEWED_AUTH);
                            }
                        } else {
                            authState.setValue(AuthState.DENIED);
                        }
                        lastPageTwitch = false;
                        authState = new SimpleObjectProperty<>(AuthState.UNKNOWN);
                    } else if (url.contains(TWITCH_ID)) {
                        lastPageTwitch = true;
                    }
                    break;
            }
        }

        @Override
        public void dispatchResourceLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) { }
    }
}
