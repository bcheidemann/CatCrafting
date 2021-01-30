package com.bcheidemann.autocrafting;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 */
public final class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("CatCrafting enabled.");

        PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new RedStoneStateEventHandler(new BlockStateManager()), this);
		pluginManager.registerEvents(new AutoCraftingEventHandler(getServer(), this, getLogger()), this);
    }
    @Override
    public void onDisable() {
        getLogger().info("CatCrafting disabled.");
    }
}
