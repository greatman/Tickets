package com.lebelw.Tickets;

import com.alta189.sqlLibrary.SQL.SQLCore;
import com.alta189.sqlLibrary.SQL.SQLCore.SQLMode;
import com.lebelw.Tickets.extras.DataManager;
import com.lebelw.Tickets.TConfig;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @description Handles SQL database connection
 * @author Tagette
 * @author Greatman
 */
public class TDatabase {
    
    private static Tickets plugin;
    public static DataManager dbm;
    
    /*
     * Initializes the plugins database connection.
     * 
     * @param instance  An instance of the plugin's main class.
     */
    public static void initialize(Tickets instance){
        TDatabase.plugin = instance;
        SQLMode dataMode;
        String tableQuery;
        
        if (TConfig.convert){
        	TLogger.info("Converting database to MySQL");
        	DataManager sqlite = new DataManager(plugin,SQLMode.SQLite);
        	DataManager mysql = new DataManager(plugin,SQLMode.MySQL);
        	tableQuery = "CREATE TABLE `players` ("
					+ "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
					+ "`name` VARCHAR( 30 ) NOT NULL ,"
					+ "`ticket` BIGINT NOT NULL DEFAULT '0'"
					+") ENGINE = MYISAM ;";
        	if(!mysql.tableExists("players") && mysql.createTable(tableQuery))
                TLogger.info("Table created. (players)");
        	ResultSet responsesqlite = sqlite.query("SELECT * FROM players");
        	if (responsesqlite != null  ){
        		try {
					while(responsesqlite.next()) {
						String query = "INSERT INTO players VALUES("+ responsesqlite.getInt("id") + ",'" + responsesqlite.getString("name")+"','" + responsesqlite.getString("ticket") + "')";
						mysql.insert(query);
					}
				} catch (SQLException e) {
					TLogger.error(e.getMessage());
				}
			}
        	TLogger.info("Convert finished");
        	TConfig TConfig = new TConfig(TDatabase.plugin);
        	TConfig.resetconvert();
        }
        if(TConfig.type.equals("mysql")){
        	dataMode = SQLMode.MySQL;
        }
        else
            dataMode = SQLMode.SQLite;
        dbm = new DataManager(plugin, dataMode);
        
        // Create database here
        
        // -- Example --
        // This will create a table with the name "players".
        if (dataMode == SQLMode.MySQL){
        	tableQuery = "CREATE TABLE `players` ("
        						+ "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
        						+ "`name` VARCHAR( 30 ) NOT NULL"
        						+") ENGINE = MYISAM ;";
        }else {
        	tableQuery = "CREATE TABLE players ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name VARCHAR(30))";
        }
        
        if(!dbm.tableExists("players") && dbm.createTable(tableQuery))
            TLogger.info("Table created. (players)");
        if (dataMode == SQLMode.MySQL){
        	tableQuery = "CREATE TABLE `tickets` ("
        						+ "`user_id` INT NOT NULL ,"
        						+ "`tickets` BIGINT NOT NULL ,"
        						+ "`business_id` INT NOT NULL"
        						+") ENGINE = MYISAM ;";
        }else {
        	tableQuery = "CREATE TABLE tickets ("
                    + "user_id INTEGER,"
                    + "tickets BIGINT,"
                    + "business_id BIGINT)";
        }
        if (!dbm.tableExists("tickets") && dbm.createTable(tableQuery)){
        	TLogger.info("Table created. (tickets)");
        }
        if (dataMode == SQLMode.MySQL){
        	tableQuery = "CREATE TABLE `logblock`.`business` ("
						+"`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,"
						+"`name` VARCHAR( 20 ) NOT NULL ,"
						+"`lottery` INT NOT NULL ,"
						+"`lotterychance` INT NOT NULL ,"
						+"`lotteryitem` INT NOT NULL ,"
						+"`lotterycost` INT NOT NULL ,"
						+"`owner_id` INT NOT NULL"
						+") ENGINE = MYISAM ;";
        }else {
        	tableQuery = "CREATE TABLE business ("
						+"id  INTEGER,"
						+"name  TEXT(20),"
						+"lottery  INTEGER,"
						+"lotterychance  INTEGER,"
						+"lotteryitem  INTEGER,"
						+"lotterycost  INTEGER,"
						+"owner_id  INTEGER,"
						+"PRIMARY KEY (id ASC)"
						+");";
        }
        if (!dbm.tableExists("business") && dbm.createTable(tableQuery)){
        	TLogger.info("Table created. (business)");
        }
        // An example on how to get and set data in the database
        //   using the DataManager is in the TemplateCmd Class
    }
    
    /*
     * Closes the connection to the database.
     */
    public static void disable(){
        dbm.getDbCore().close();
    }
    
    /*
     * Gets the Database core.
     * Used for more advanced databasing. :)
     */
    public static SQLCore getCore() {
        return dbm.getDbCore();
    }
}
