package org.kokapuk.mcplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class McPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
    }
}
