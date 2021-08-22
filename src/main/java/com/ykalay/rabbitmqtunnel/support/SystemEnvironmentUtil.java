package com.ykalay.rabbitmqtunnel.support;

import java.util.Objects;

/**
 * Environment variable operations util
 *
 * @author ykalay
 *
 * @since 1.0
 */
public class SystemEnvironmentUtil {

    /**
     * Private constructor of {@link SystemEnvironmentUtil}
     */
    private SystemEnvironmentUtil() {}

    /**
     * Finds the environment variable if exists and parse it to {@link Integer}
     * @param key
     *          Environment variable key
     * @param def
     *          If the value doesn't exists, return the default value
     *
     * @return the environment value as {@link Integer}
     *
     */
    public static int getInt(String key, int def) {
        String value = get(key);
        if(Objects.isNull(value)) {
            // Value not found from env variables return the default one
            return def;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Finds the environment variable if exists and parse it to {@link Boolean}
     *
     * @param key
     *          Environment variable key
     *
     * @return the environment value as {@link Boolean}
     */
    public static Boolean getBool(String key) {
        String value = get(key);
        if(Objects.isNull(value)) {
            // Value not found from env variables return the default one
            return null;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    /**
     * Finds the environment variable
     * @param key
     *          Environment variable key
     * @return If the value is exist from the environment variables, It returns the environment value as String
     *         If the value isn't exist from the environment variables, It returns null
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     *
     * @param key
     *          Environment variable key
     * @param def
     *          If the value isn't exist from the environment variables, It returns this default value
     *
     * @return If the value is exist from the environment variables, It returns the environment value as String
     *         If the value isn't exist from the environment variables, It returns @param def
     */
    public static String get(String key, String def) {
        if(key == null) {
            throw new NullPointerException("key");
        }
        if(key.isEmpty()) {
            throw new IllegalArgumentException("Key must be not empty");
        }
        String value = System.getenv(key);
        if(Objects.isNull(value)) {
            return def;
        } else {
            return value;
        }
    }
}
