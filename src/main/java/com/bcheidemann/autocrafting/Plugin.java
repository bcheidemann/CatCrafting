package com.bcheidemann.autocrafting;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 */
public final class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("CatCrafting enabled.");
    }
    @Override
    public void onDisable() {
        getLogger().info("CatCrafting disabled.");
    }
}
