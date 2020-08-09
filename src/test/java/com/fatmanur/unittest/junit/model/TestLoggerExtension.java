package com.fatmanur.unittest.junit.model;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.logging.Logger;

public class TestLoggerExtension implements BeforeAllCallback, AfterAllCallback {

    private static Logger logger = Logger.getLogger(TestLoggerExtension.class.getName());

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        logger.info(String.format("Test is ended:   %s", context.getDisplayName()));

    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        logger.info(String.format("Test is started:  %s", context.getDisplayName()));

    }
}
