package com.lebelw.Tickets.extras;

import com.alta189.sqlLibrary.SQL.SQLCore;
import com.alta189.sqlLibrary.SQL.SQLCore.SQLMode;
import java.sql.ResultSet;
import com.lebelw.Tickets.TLogger;
import com.lebelw.Tickets.Tickets;

/**
 * @description Handles database connections
 * @author Tagette
 */
public class DataManager {

    private Tickets plugin;
    private SQLCore dbCore;

    /*
     * Initializes the DataManager class.
     * 
     * @param instance  An instance of the plugin's main class.
     */
    public DataManager(Tickets instance, SQLMode mode) {
        plugin = instance;
        if (mode == SQLMode.SQLite) {
            dbCore = new SQLCore(TLogger.getLog(), TLogger.getPrefix(),
                    plugin.getDataFolder().getPath() + "/Data", Tickets.name);
        }
        if (dbCore.initialize()) {
            TLogger.info("Database connection established.");
        }
    }

    /*
     * Used for more advanced database interactions.
     */
    public SQLCore getDbCore() {
        return dbCore;
    }

    /*
     * Used to create a table in the database.
     */
    public boolean createTable(String query) {
        boolean wasCreated = false;
            wasCreated = execute(query);
        return wasCreated;
    }

    /*
     * Deletes a table from the database.
     */
    public boolean deleteTable(String tableName) {
        boolean wasDeleted = false;
        if (!tableName.isEmpty()) {
            String query = "DROP TABLE '" + tableName + "'";
            wasDeleted = update(query);
        } else {
            TLogger.error("Database.DeleteTable: Could not delete table because table name was empty.");
        }
        return wasDeleted;
    }

    public boolean execute(String query) {
        if (plugin.inDebugMode()) {
            TLogger.info("Database.execute Query: \"" + query + "\"");
        }
        return dbCore.createTable(query);
    }

    public boolean update(String query) {
        if (plugin.inDebugMode()) {
            TLogger.info("Database.update Query: \"" + query + "\"");
        }
        return dbCore.updateQuery(query);
    }

    public boolean insert(String query) {
        if (plugin.inDebugMode()) {
            TLogger.info("Database.insert Query: \"" + query + "\"");
        }
        return dbCore.insertQuery(query);
    }

    public ResultSet query(String query) {
        if (plugin.inDebugMode()) {
            TLogger.info("Database.query Query: \"" + query + "\"");
        }
        return dbCore.sqlQuery(query);
    }

    public boolean tableExists(String tableName) {
        return dbCore.checkTable(tableName);
    }

    public boolean fieldExists(String tableName, String fieldName) {
        return dbCore.checkField(tableName, fieldName);
    }
}
