package com.gaetan.staffpin.config;

import com.gaetan.api.message.Message;
import com.gaetan.staffpin.StaffPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigManager {
    /**
     * Reference to the main class
     */
    private final StaffPlugin staffPlugin;

    /**
     * Cache for the config
     */
    private String enterPin, timeFinish,
            correctPin, incorrectPin, noPin, pinSet, pinPermission, pinUsage, cacheLogin;

    /**
     * Cache for the config
     */
    private boolean cacheEnabled;

    /**
     * Constructor for the ConfigManager class.
     *
     * @param staffPlugin Reference to the main class
     */
    public ConfigManager(final StaffPlugin staffPlugin) {
        this.staffPlugin = staffPlugin;

        this.load();
    }

    /**
     * Method to cache the config
     */
    private void load() {
        final FileConfiguration config = this.staffPlugin.getConfig();
        final String prefix = Message.tl(config.getString("lang.prefix"));

        this.enterPin = prefix + Message.tl(config.getString("lang.enter_ping"));
        this.timeFinish = prefix + Message.tl(config.getString("lang.time_finish"));
        this.correctPin = prefix + Message.tl(config.getString("lang.pin.correct"));
        this.incorrectPin = prefix + Message.tl(config.getString("lang.pin.incorrect"));
        this.noPin = prefix + Message.tl(config.getString("lang.pin.dont_have"));
        this.pinSet = prefix + Message.tl(config.getString("lang.pin.set"));
        this.cacheLogin = prefix + Message.tl(config.getString("lang.cache_login"));

        this.cacheEnabled = config.getBoolean("cache.enabled");

        this.pinPermission = config.getString("permission.pin");
        this.pinUsage = Message.tl(config.getString("lang.pin.usage"));
    }

    /**
     * Getter to get the message for enter the pin.
     *
     * @return The enter message
     */
    public String getEnterPin() {
        return this.enterPin;
    }

    /**
     * Getter to get the message when the cooldown get his end.
     *
     * @return The end message
     */
    public String getTimeFinish() {
        return this.timeFinish;
    }

    /**
     * Getter to get the correct pin message.
     *
     * @return The correct pin message
     */
    public String getCorrectPin() {
        return this.correctPin;
    }

    /**
     * Getter to get the incorrect pin message.
     *
     * @return The incorrect pin message
     */
    public String getIncorrectPin() {
        return this.incorrectPin;
    }

    /**
     * Getter to get the no pin message.
     *
     * @return The nopin message
     */
    public String getNoPin() {
        return this.noPin;
    }

    /**
     * Getter to get the pin set message.
     *
     * @return The pin set message
     */
    public String getPinSet() {
        return this.pinSet;
    }

    /**
     * Getter to get the pin permission.
     *
     * @return The pin permission
     */
    public String getPinPermission() {
        return this.pinPermission;
    }

    /**
     * Getter to get the pin usage message.
     *
     * @return The pin usage message
     */
    public String getPinUsage() {
        return this.pinUsage;
    }

    /**
     * Getter to get the cache login message.
     *
     * @return The cache login message.
     */
    public String getCacheLogin() {
        return this.cacheLogin;
    }

    /**
     * Getter to get the cache statut.
     *
     * @return The the cache statut.
     */
    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }
}