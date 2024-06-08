package com.lautbiru.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class CommonUtils {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private static final String PROPERTIES_FILE = "application.properties";

    public static Properties props;

    static {
        try {
            props = new Properties();
            props.load(ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE));
        } catch (Exception e) {
            logger.info("****** Application Properties is empty **************");
        }
    }
}
