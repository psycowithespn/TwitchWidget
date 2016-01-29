package com.psyco.twitchwidget.authentication.cookies;

import com.google.gson.*;
import com.psyco.twitchwidget.OSCompat;
import com.sun.webkit.network.CookieManager;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersistentCookies {

    private static final String FILENAME = "cookies.json";
    private static final PersistentCookies INSTANCE;

    private final Gson gson = new Gson();
    private final JsonParser parser = new JsonParser();
    private final File file;
    private Class<?> cookieClass;
    private Field cookieStoreField;
    private Field bucketsField;
    private Field totalCountField;

    static {
        PersistentCookies result = null;
        try {
            result = new PersistentCookies();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error in loading persisted cookies. Cookie persistence will not work.");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find cookies.json. Saved cookies will not be loaded.");
            e.printStackTrace();
        }

        INSTANCE = result;
    }

    private PersistentCookies() throws ClassNotFoundException, NoSuchFieldException, FileNotFoundException, IllegalAccessException {
        Class<?> cookieStoreClass = Class.forName("com.sun.webkit.network.CookieStore");
        cookieClass = Class.forName("com.sun.webkit.network.Cookie");
        bucketsField = cookieStoreClass.getDeclaredField("buckets");
        bucketsField.setAccessible(true);
        totalCountField = cookieStoreClass.getDeclaredField("totalCount");
        totalCountField.setAccessible(true);
        cookieStoreField = CookieManager.class.getDeclaredField("store");
        cookieStoreField.setAccessible(true);

        Path path = OSCompat.getDataDir().resolve(FILENAME);
        file = path.toFile();

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Error creating cookies.json file. Cookies will not be saved.");
                e.printStackTrace();
            }
        }
    }

    private <T> boolean load(Class<T> cookieClass) throws IllegalAccessException, FileNotFoundException {
        if (!file.exists()) {
            return false;
        }
        // Load in the JSON file.
        FileReader reader = new FileReader(file);
        // Parse the JSON and extract the totalCount int.
        JsonElement element = parser.parse(reader);
        if (element.isJsonNull()) {
            return false;
        }

        JsonObject root = element.getAsJsonObject();

        int totalCount = root.get("totalCount").getAsInt();

        // Get the current CookieStore object from the default CookieManager.
        Object cookieStore = cookieStoreField.get(CookieManager.getDefault());
        totalCountField.setInt(cookieStore, totalCount);
        // Use Gson to populate the internal map for the cookies.
        Map<String, Map<T, T>> buckets = new HashMap<>();
        JsonObject cookies = root.get("buckets").getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : cookies.entrySet()) {
            Map<T, T> innerMap = buckets.get(entry.getKey());
            if (innerMap == null) {
                innerMap = new LinkedHashMap<T,T>(20);
                buckets.put(entry.getKey(), innerMap);
            }
            for (JsonElement jsonElement : entry.getValue().getAsJsonArray()) {
                T cookie = gson.fromJson(jsonElement, cookieClass);
                innerMap.put(cookie, cookie);
            }
        }
        bucketsField.set(cookieStore, buckets);

        return true;
    }

    private <T> void save(Class<T> cookieClass) throws IllegalAccessException, IOException {
        Object cookieStore = cookieStoreField.get(CookieManager.getDefault());
        int totalCount = totalCountField.getInt(cookieStore);

        Map<String, Map<T, T>> buckets = (Map<String, Map<T, T>>) bucketsField.get(cookieStore);
        JsonObject bucketsJson = new JsonObject();
        for (Map.Entry<String, Map<T, T>> entry : buckets.entrySet()) {
            JsonArray array = new JsonArray();
            for (T cookie : entry.getValue().values()) {
                array.add(gson.toJsonTree(cookie));
            }
            bucketsJson.add(entry.getKey(), array);
        }

        JsonObject root = new JsonObject();
        root.addProperty("totalCount", totalCount);
        root.add("buckets", bucketsJson);

        FileWriter writer = new FileWriter(file, false);
        gson.toJson(root, writer);
        writer.flush();
        writer.close();
    }

    private <T> void wipe(Class<T> cookieClass) throws IllegalAccessException {
        Object cookieStore = cookieStoreField.get(CookieManager.getDefault());
        Map<String, Map<T, T>> buckets = new HashMap<>();
        bucketsField.set(cookieStore, buckets);
        totalCountField.setInt(cookieStore, 0);
        saveCookies();
    }

    public static boolean loadCookies() {
        if (INSTANCE != null) {
            try {
                return INSTANCE.load(INSTANCE.cookieClass);
            } catch (FileNotFoundException | IllegalAccessException e) {
                System.err.println("Unable to load cookie persistence.");
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean saveCookies () {
        if (INSTANCE != null) {
            try {
                INSTANCE.save(INSTANCE.cookieClass);
                return true;
            } catch (IllegalAccessException | IOException e) {
                System.err.println("Unable to save cookies.");
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void wipeCookies() {
        if (INSTANCE != null) {
            try {
                INSTANCE.wipe(INSTANCE.cookieClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
