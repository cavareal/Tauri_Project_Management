package fr.eseo.tauri.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static Logger logger = LoggerFactory.getLogger(CustomLogger.class);

    // Private constructor to hide the implicit public one
    CustomLogger() {
        throw new IllegalStateException("Utility class");
    }

    // Method to set the logger, mainly for testing purposes
    static void setLogger(Logger logger) {
        CustomLogger.logger = logger;
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarn(String message) {
        logger.warn(message);
    }

    public static void logError(String message) {
        logger.error(message);
    }

    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}