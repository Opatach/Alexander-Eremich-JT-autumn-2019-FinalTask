/*
 * Configuration Manager
 *
 * Version 1.0
 *
 * Date 30 Jan 2020
 */

package com.epam.bets.managers;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * A class that manages application configurations.
 *
 * @version 1.0 30 Jan 2020
 * @author Alexander Eremich
 */
public class ConfigurationManager {
    /**
     * Bundle name.
     */
    private static final String BUNDLE_NAME =
            "com.epam.bets.resources.configuration";
    /**
     * Logger.
     */
    private static Logger log = LogManager.getLogger(ConnectionManager.class);
    /**
     * Configuration manager instance.
     */
    private static ConfigurationManager instance;

    /**
     * Resource bundle.
     */
    private ResourceBundle resourceBundle;

    /**
     * Creates a new configuration manager.
     */
    private ConfigurationManager() {
        log.info("Configuration manager instance created.");
    }

    /**
     * Creates a new configuration manager instance or returns an existing one.
     * @return configuration manager instance.
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
            instance.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
        }

        return instance;
    }

    /**
     * Returns property value.
     * @param key property name.
     * @return property value.
     */
    public String getProperty(String key) {
        return (String)resourceBundle.getObject(key);
    }
}
