package com.lebelw.Tickets;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @description Handles enabling plugins
 * @author Tagette
 */
public class TPluginListener extends ServerListener {

    private final Tickets plugin;

    public TPluginListener(Tickets instance) {
        plugin = instance;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() != plugin) {
            // Try to load again!
            TPermissions.onEnable(event.getPlugin());
        }
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
    }
}
