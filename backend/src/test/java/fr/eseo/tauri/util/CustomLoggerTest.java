package fr.eseo.tauri.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class CustomLoggerTest {

    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        CustomLogger.setLogger(logger); // Set the mock logger
    }

    @Test
    void logInfo_logsInfoMessage() {
        CustomLogger.logInfo("Info message");
        verify(logger, times(1)).info("Info message");
    }

    @Test
    void logWarn_logsWarnMessage() {
        CustomLogger.logWarn("Warn message");
        verify(logger, times(1)).warn("Warn message");
    }

    @Test
    void logError_logsErrorMessageWithThrowable() {
        Throwable throwable = new Throwable("Test throwable");
        CustomLogger.logError("Error message", throwable);
        verify(logger, times(1)).error("Error message", throwable);
    }

    @Test
    void logError_logsErrorMessage() {
        CustomLogger.logError("Error message");
        verify(logger, times(1)).error("Error message");
    }
}