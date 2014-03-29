package net.kingdomsofarden.andrew2060.market;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.ess3.api.IEssentials;

import net.kingdomsofarden.andrew2060.market.iface.MySQLConnection;

import org.bukkit.plugin.java.JavaPlugin;

public class MarketPlugin extends JavaPlugin {

    public static MarketPlugin instance;
    public MySQLConnection mysql;
    public IEssentials ess;
    
    public void onEnable() {
        MarketPlugin.instance = this;
    }

    public ResultSet executeQuery(String query, boolean write) throws SQLException {
        return mysql.executeQuery(query, write);
    }
    
}
