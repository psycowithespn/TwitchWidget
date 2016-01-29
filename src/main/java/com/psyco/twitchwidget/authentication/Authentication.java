package com.psyco.twitchwidget.authentication;

import com.psyco.twitchwidget.authentication.cookies.PersistentCookies;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.WebPage;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authentication {

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
    private ChangeListener<AuthState> currentListener;

    public Authentication() {
        stage = new Stage();
        webView = new WebView();
        authLoadListenerClient = new AuthLoadListenerClient();

        if (PersistentCookies.loadCookies()) {
            System.out.println("Cookies loaded.");
        } else {
            System.out.println("No cookies loaded.");
        }

        try {
            Field page = WebEngine.class.getDeclaredField("page");
            page.setAccessible(true);
            Field loadListenerClients = WebPage.class.getDeclaredField("loadListenerClients");
            loadListenerClients.setAccessible(true);
            Object webPage = page.get(webView.getEngine());
            Object loadList = loadListenerClients.get(webPage);
            @SuppressWarnings("unchecked")
            List<LoadListenerClient> listLoadList = (List<LoadListenerClient>) loadList;
            listLoadList.add(authLoadListenerClient);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(webView);
        stage.setScene(scene);

        stage.setOnHidden(e -> {
            if (authenticating) {
                authLoadListenerClient.authState.setValue(AuthState.DENIED);
            }
        });
    }

    public boolean authenticate(boolean forceVerify, Consumer<AuthState> successCallback, Consumer<AuthState> errorCallback) {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Must be called from FX Application thread");
        }

        if (authenticating) {
            return false;
        }

        authenticating = true;

        if (currentListener != null) {
            authLoadListenerClient.authState.removeListener(currentListener);
        }

        currentListener = (observable, oldValue, newValue) -> {
            System.out.println(newValue);
            switch (newValue) {
                case REQUIRES_MANUAL_AUTH:
                    stage.show();
                    break;
                case MANUAL_AUTH_SUCCEEDED:
                case RENEWED_AUTH:
                    authenticating = false;
                    stage.close();
                    successCallback.accept(newValue);
                    if (PersistentCookies.saveCookies()) {
                        System.out.println("Saved cookies for re-authentication.");
                    } else {
                        System.out.println("Unable to save re-authentication cookies.");
                    }
                    break;
                case ERROR:
                case DENIED:
                    authenticating = false;
                    stage.close();
                    errorCallback.accept(newValue);
                    break;
            }
        };

        authLoadListenerClient.authState.addListener(currentListener);

        if (forceVerify) {
            PersistentCookies.wipeCookies();
        }

        webView.getEngine().load(AUTH_URL);
        return true;
    }

    private void hideSignUp() {
        Element element = this.webView.getEngine().getDocument().getElementById("signup_tab");
        if (element != null) {
            element.getParentNode().removeChild(element);
        }
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
            if (!authenticating) {
                return;
            }

            if (errorCode != 0) {
                System.out.println("Error: Authentication failure: " + errorCode + ": " + url);
                authState.setValue(AuthState.ERROR);
                return;
            }

            switch (state) {
                case PAGE_FINISHED:
                    if (url.contains(TWITCH_ID)) {
                        authState.setValue(AuthState.REQUIRES_MANUAL_AUTH);
                        lastPageTwitch = true;
                        hideSignUp();
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
        public void dispatchResourceLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
        }
    }
}
