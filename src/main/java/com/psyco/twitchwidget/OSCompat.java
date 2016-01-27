package com.psyco.twitchwidget;

import java.nio.file.Path;
import java.nio.file.Paths;

public class OSCompat {

    private static final String APP_NAME = "TwitchGUI";
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static Path getDataDir() {
        Path dataDir;

        if (OS.startsWith("windows")) {     // Windows
            String appData = System.getenv("appdata");
            dataDir = Paths.get(appData, APP_NAME);
        } else {
            String userDir = System.getProperty("user.home");
            if (OS.startsWith("mac")) {     // OSX
                dataDir = Paths.get(userDir, "Library", "Application Support", APP_NAME);
            } else {                        // Linux, etc
                dataDir = Paths.get(userDir, ".local", "share", APP_NAME);
            }
        }

        return dataDir;
    }
}
