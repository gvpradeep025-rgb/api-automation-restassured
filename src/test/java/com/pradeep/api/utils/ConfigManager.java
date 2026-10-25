package com.pradeep.api.utils;

import java.io.InputStream;
import java.util.Properties;

/** Reads config.properties. A -D system property overrides the file, e.g. -DbaseUri=http://localhost:3001 */
public final class ConfigManager {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    private ConfigManager() { }

    public static String get(String key) {
        return System.getProperty(key, PROPS.getProperty(key));
    }

    public static long getLong(String key) {
        return Long.parseLong(get(key));
    }
}
