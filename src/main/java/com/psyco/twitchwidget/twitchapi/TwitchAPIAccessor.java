package com.psyco.twitchwidget.twitchapi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.follow.FollowAccessor;
import com.psyco.twitchwidget.twitchapi.api.root.RootResponse;
import com.psyco.twitchwidget.util.CallableCallback;
import com.psyco.twitchwidget.util.CallableCallbackFactory;
import com.psyco.twitchwidget.util.RunnableException;
import javafx.application.Platform;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TwitchAPIAccessor {

    public static final String ROOT_URL = "https://api.twitch.tv/kraken";
    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();
    private static final String MIME_TYPE = "application/vnd.twitchtv.3+json";

    private final String clientID = "q9ons4lq6jzz0qxeyqiwh4h7dglpt4q";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CallableCallbackFactory callbackFactory;
    private final FollowAccessor followAccessor;

    private String oauthToken;

    public TwitchAPIAccessor() {
        this(null);
    }

    public TwitchAPIAccessor(String oauthToken) {
        this.oauthToken = oauthToken;
        this.callbackFactory = new CallableCallbackFactory(Platform::runLater, executor);
        this.followAccessor = new FollowAccessor(this);
    }

    public void dispose() {
        executor.shutdown();
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = "OAuth " + oauthToken;
    }

    public void isOAuthValid(Runnable validCallback, Runnable notValidCallback) {
       submitTask(() -> get(ROOT_URL, RootResponse.class))
                .withOnSucceeded(resp -> isOAuthValid(resp, validCallback, notValidCallback))
                .withOnException(e -> notValidCallback.run())
                .start();
    }

    private void isOAuthValid(RootResponse response, Runnable validCallback, Runnable notValidCallback) {
        if (!response.isError() && response.getToken().isValid()) {
            validCallback.run();
        } else {
            notValidCallback.run();
        }
    }

    public FollowAccessor getFollowAccessor() {
        return followAccessor;
    }

    public <T extends APIResponse> T get(String url, Class<T> responseType) throws Exception {
        HttpsURLConnection conn = null;
        InputStreamReader reader = null;
        System.out.println("Staring API Request: " + url);
        try {
            conn = (HttpsURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", MIME_TYPE);
            conn.setRequestProperty("Client-ID", clientID);
            if (oauthToken != null) {
                conn.setRequestProperty("Authorization", oauthToken);
            }

            conn.connect();
            if (conn.getResponseCode() == 204) {
                System.out.println("API call successful");
                return responseType.newInstance();
            } else if (conn.getResponseCode() / 100 == 2) {
                reader = new InputStreamReader(conn.getInputStream());
            } else {
                reader = new InputStreamReader(conn.getErrorStream());
            }
            JsonElement jsonElement = JSON_PARSER.parse(reader);
            JsonObject object = jsonElement.getAsJsonObject();

            T response = GSON.fromJson(jsonElement, responseType);

            if (object.has("error")) {
                System.err.printf("Error on API Call: %s", url);
                System.err.printf("- %d %s: %s",
                        object.get("status").getAsInt(),
                        object.get("error").getAsString(),
                        object.get("message").getAsString());
                // Workaround for ChannelResponse having a different-type field called status.
                response.setStatusCode(object.get("status").getAsInt());
            } else {
                System.out.println("API call successful");
            }

            return response;
        } catch (Exception e) {
            System.err.println("API call resulted in an exception: ");
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            if (reader != null) {
                reader.close();
            }
        }
    }

    public <T> CallableCallback<T> submitTask(Callable<T> callable) {
        return callbackFactory.submit(callable);
    }

    public CallableCallback<Void> submitTask(RunnableException runnable) {
        return callbackFactory.submit(() -> {
            runnable.run();
            return null;
        });
    }
}
