package com.lebelw.Tickets;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

import com.iConomy.*;
import cosine.boseconomy.*;
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
                    TLogger.info("Hooked into iConomy");
                }
            }
        }
        if (plugin.BOSEconomy == null) {
        	Plugin BOSEconomy = plugin.getServer().getPluginManager().getPlugin("BOSEconomy");
        	if (BOSEconomy != null)
        	{
        		if(BOSEconomy.isEnabled()){
        			plugin.BOSEconomy = (BOSEconomy)BOSEconomy;
        			TLogger.info("Hooked into BOSEconomy");
        		}
        	}
        }
    }


    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        if (plugin.iConomy != null) {
            if (event.getPlugin().getDescription().getName().equals("iConomy")) {
                plugin.iConomy = null;
                TLogger.info("un-hooked from iConomy.");
            }
        }
        if (plugin.BOSEconomy != null) {
            if (event.getPlugin().getDescription().getName().equals("BOSEconomy")) {
                plugin.BOSEconomy = null;
                TLogger.info("un-hooked from BOSEconomy.");
            }
        }
    }

}
