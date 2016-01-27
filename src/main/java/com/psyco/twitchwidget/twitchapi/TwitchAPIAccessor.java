package com.psyco.twitchwidget.twitchapi;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.psyco.twitchwidget.twitchapi.api.APIResponse;
import com.psyco.twitchwidget.twitchapi.api.ErrorResponse;
import com.psyco.twitchwidget.twitchapi.api.root.RootResponse;
import com.psyco.twitchwidget.util.CallableCallbackFactory;
import javafx.application.Platform;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

    private String oauthToken;

    public TwitchAPIAccessor() {
        this(null);
    }

    public TwitchAPIAccessor(String oauthToken) {
        this.oauthToken = oauthToken;
        callbackFactory = new CallableCallbackFactory(Platform::runLater, executor);

    }

    public void dispose() {
        executor.shutdown();
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = "OAuth " + oauthToken;
    }

    public void isOAuthValid(Runnable validCallback, Runnable notValidCallback) {
        callbackFactory.submit(() -> get(ROOT_URL, RootResponse.class))
                .withOnSucceeded(resp -> isOAuthValid(resp, validCallback, notValidCallback))
                .withOnException(e -> notValidCallback.run())
                .start();
    }

    private void isOAuthValid(APIResponse response, Runnable validCallback, Runnable notValidCallback) {
        if (!response.isError() && ((RootResponse) response).getToken().isValid()) {
            validCallback.run();
        } else {
            notValidCallback.run();
        }
    }

    protected APIResponse get(String url, Class<? extends APIResponse> responseType) throws IOException {
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
            reader = new InputStreamReader(conn.getInputStream());
            JsonElement jsonElement = JSON_PARSER.parse(reader);
            JsonObject object = jsonElement.getAsJsonObject();

            if (object.has("error")) {
                System.err.printf("Error on API Call: %s", url);
                System.err.printf("- %d %s: %s",
                        object.get("status").getAsInt(),
                        object.get("error").getAsString(),
                        object.get("message").getAsString());
                return GSON.fromJson(jsonElement, ErrorResponse.class);
            } else {
                System.out.println("API call successful");
                return GSON.fromJson(jsonElement, responseType);
            }
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
}
