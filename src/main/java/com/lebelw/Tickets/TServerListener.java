package com.lebelw.Tickets;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @description Handles enabling plugins
 * @author Tagette
 */
public class TServerListener extends ServerListener {

    private final Tickets plugin;

    public TServerListener(Tickets instance) {
        plugin = instance;
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        if (plugin.iConomy == null) {
            Plugin iConomy = plugin.getServer().getPluginManager().getPlugin("iConomy");

            if (iConomy != null) {
                if (iConomy.isEnabled() && iConomy.getClass().getName().equals("com.iConomy.iConomy")) {
                    plugin.iConomy = (iConomy)iConomy;
                    System.out.println("[MyPlugin] hooked into iConomy.");
                }
            }
        }
    }


    @Override
    public void onPluginDisable(PluginDisableEvent event) {
    }
}
