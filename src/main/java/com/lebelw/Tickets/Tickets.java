package com.lebelw.Tickets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;

import com.lebelw.Tickets.commands.TemplateCmd;
import com.lebelw.Tickets.extras.CommandManager;

public class Tickets extends JavaPlugin {
	private int debug = 0;
	public static String name;
    public static String version;
	private final TPluginListener pluginListener = new TPluginListener(this);
	private final CommandManager commandManager = new CommandManager(this);
	public void onEnable() {
		name = this.getDescription().getName();
		version = this.getDescription().getVersion();
		PluginManager pm = getServer().getPluginManager();
        // Makes sure all plugins are correctly loaded.
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Priority.Monitor, this);
		TLogger.initialize(Logger.getLogger("Minecraft"));
		TConfig TConfig = new TConfig(this);
		TConfig.configCheck();
		TDatabase.initialize(this);
		TPermissions.initialize(this);
		TLogger.info("Enabled");
		
		
		//Let's setup commands
		addCommand("ticket", new TemplateCmd(this));
	}
	public void onDisable(){
		TDatabase.disable();
		TLogger.info("Disabled");
	}
	public boolean inDebugMode(){
		if (debug == 0){
			return false;
		}
		else{
			return true;
		}
	}
	/*
     * Executes a command when a command event is received.
     * 
     * @param sender    The thing that sent the command.
     * @param cmd       The complete command object.
     * @param label     The label of the command.
     * @param args      The arguments of the command.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commandManager.dispatch(sender, cmd, label, args);
    }

    /*
     * Adds the specified command to the command manager and server.
     * 
     * @param command   The label of the command.
     * @param executor  The command class that excecutes the command.
     */
    private void addCommand(String command, CommandExecutor executor) {
        getCommand(command).setExecutor(executor);
        commandManager.addCommand(command, executor);
    }
    /**
    * Match player names.
    *
    * @param filter
    * @return
    */
        public List<Player> matchPlayerNames(String filter) {
            Player[] players = getServer().getOnlinePlayers();

            filter = filter.toLowerCase();
            
            // Allow exact name matching
            if (filter.charAt(0) == '@' && filter.length() >= 2) {
                filter = filter.substring(1);
                
                for (Player player : players) {
                    if (player.getName().equalsIgnoreCase(filter)) {
                        List<Player> list = new ArrayList<Player>();
                        list.add(player);
                        return list;
                    }
                }
                
                return new ArrayList<Player>();
            // Allow partial name matching
            } else if (filter.charAt(0) == '*' && filter.length() >= 2) {
                filter = filter.substring(1);
                
                List<Player> list = new ArrayList<Player>();
                
                for (Player player : players) {
                    if (player.getName().toLowerCase().contains(filter)) {
                        list.add(player);
                    }
                }
                
                return list;
            
            // Start with name matching
            } else {
                List<Player> list = new ArrayList<Player>();
                
                for (Player player : players) {
                    if (player.getName().toLowerCase().startsWith(filter)) {
                        list.add(player);
                    }
                }
                
                return list;
            }
        }
    /**
    * Checks to see if the sender is a player, otherwise throw an exception.
    *
    * @param sender
    * @return
    * @throws CommandException
    */
        public Player checkPlayer(CommandSender sender)
                throws CommandException {
            if (sender instanceof Player) {
                return (Player) sender;
            } else {
                throw new CommandException("A player context is required. (Specify a world or player if the command supports it.)");
            }
        }
    /**
    * Checks if the given list of players is greater than size 0, otherwise
    * throw an exception.
    *
    * @param players
    * @return
    * @throws CommandException
    */
        protected Iterable<Player> checkPlayerMatch(List<Player> players)
                throws CommandException {
            // Check to see if there were any matches
            if (players.size() == 0) {
                throw new CommandException("No players matched query.");
            }
            
            return players;
        }
    /**
    * Checks permissions and throws an exception if permission is not met.
    *
    * @param source
    * @param filter
    * @return iterator for players
    * @throws CommandException no matches found
    */
    public Iterable<Player> matchPlayers(CommandSender source, String filter)
            throws CommandException {
        
        if (getServer().getOnlinePlayers().length == 0) {
            throw new CommandException("No players matched query.");
        }
        
        if (filter.equals("*")) {
            return checkPlayerMatch(Arrays.asList(getServer().getOnlinePlayers()));
        }

        // Handle special hash tag groups
        if (filter.charAt(0) == '#') {
            // Handle #world, which matches player of the same world as the
            // calling source
            if (filter.equalsIgnoreCase("#world")) {
                List<Player> players = new ArrayList<Player>();
                Player sourcePlayer = checkPlayer(source);
                World sourceWorld = sourcePlayer.getWorld();
                
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.getWorld().equals(sourceWorld)) {
                        players.add(player);
                    }
                }

                return checkPlayerMatch(players);
            
            // Handle #near, which is for nearby players.
            } else if (filter.equalsIgnoreCase("#near")) {
                List<Player> players = new ArrayList<Player>();
                Player sourcePlayer = checkPlayer(source);
                World sourceWorld = sourcePlayer.getWorld();
                org.bukkit.util.Vector sourceVector
                        = sourcePlayer.getLocation().toVector();
                
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.getWorld().equals(sourceWorld)
                            && player.getLocation().toVector().distanceSquared(
                                    sourceVector) < 900) {
                        players.add(player);
                    }
                }

                return checkPlayerMatch(players);
            
            } else {
                throw new CommandException("Invalid group '" + filter + "'.");
            }
        }
        
        List<Player> players = matchPlayerNames(filter);
        
        return checkPlayerMatch(players);
    }
    /**
    * Match only a single player.
    *
    * @param sender
    * @param filter
    * @return
    * @throws CommandException
    */
    public Player matchSinglePlayer(CommandSender sender, String filter)
            throws CommandException {
        // This will throw an exception if there are no matches
        Iterator<Player> players = matchPlayers(sender, filter).iterator();
        
        Player match = players.next();
        
        // We don't want to match the wrong person, so fail if if multiple
        // players were found (we don't want to just pick off the first one,
        // as that may be the wrong player)
        if (players.hasNext()) {
            throw new CommandException("More than one player found! " +
             "Use @<name> for exact matching.");
        }
        
        return match;
    }
}
