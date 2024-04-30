package fr.eseo.tauri.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        CustomLogger.info("Info message");
        verify(logger, times(1)).info("Info message");
    }

    @Test
    void logWarn_logsWarnMessage() {
        CustomLogger.warn("Warn message");
        verify(logger, times(1)).warn("Warn message");
    }

    @Test
    void logError_logsErrorMessageWithThrowable() {
        Throwable throwable = new Throwable("Test throwable");
        CustomLogger.error("Error message", throwable);
        verify(logger, times(1)).error("Error message", throwable);
    }

    @Test
    void logError_logsErrorMessage() {
        CustomLogger.error("Error message");
        verify(logger, times(1)).error("Error message");
    }

    @Test
    void constructor_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, CustomLogger::new);
    }
}