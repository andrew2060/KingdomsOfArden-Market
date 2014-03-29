package net.kingdomsofarden.andrew2060.market.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.kingdomsofarden.andrew2060.market.MarketPlugin;
import net.kingdomsofarden.andrew2060.market.util.thirdparty.drichter.JsonReader;
import net.kingdomsofarden.andrew2060.market.util.thirdparty.drichter.JsonWriter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Order {
    
    private int id;
    private Material type;
    private String name;
    private Map<Enchantment, Integer> enchants;
    private List<String> lore;
    private int quantity;
    private double pricePerUnit;
    private OrderType orderType;
    private DecimalFormat dF;
    private Player owner;
    private boolean isNPC;
    private Location loc;
    private ItemStack item;
    
    public Order(int id, ItemStack item, int quantity, double pricePerUnit, OrderType orderType, Player owner, boolean isNPC, Location loc) {
        this.id = id;
        this.type = item.getType();
        this.name = item.getItemMeta().getDisplayName();
        this.enchants = item.getEnchantments();
        this.lore = item.getItemMeta().getLore();
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.orderType = orderType;
        this.owner = owner;
        this.dF = new DecimalFormat("##.##");
        this.isNPC = isNPC;
        this.loc = loc;
        this.item = item;
    }
    
    
    public ItemStack getOrderIcon() {
        ItemStack item = new ItemStack(this.type,1);
        ItemMeta meta = item.getItemMeta();
        if(name != null) {
            meta.setDisplayName(name);
        }
        List<String> orderLore = new LinkedList<String>();
        if(lore != null) {
            orderLore.addAll(lore);
        }
        
        orderLore.add(ChatColor.AQUA + "======Order Details=====");
        orderLore.add(ChatColor.GOLD + "Type: " + ChatColor.GRAY + this.orderType);
        orderLore.add(ChatColor.GOLD + "Quantity: " + ChatColor.GRAY + (this.isNPC ? 999 : this.quantity));
        orderLore.add(ChatColor.GOLD + "Price: "  + ChatColor.GRAY + this.dF.format(pricePerUnit) + " Aurums/Unit");  
        if(!isNPC) {
            orderLore.add(ChatColor.GOLD + "Owner: " + ChatColor.GRAY + this.owner.getName());
        }
        orderLore.add("Order ID: #" + this.id);
        meta.setLore(orderLore);
        item.setItemMeta(meta);
        if(this.enchants != null) {
            for(Enchantment ench : this.enchants.keySet()) {
                item.addEnchantment(ench,this.enchants.get(ench));
            }
        }
        return item; 
    }
    
    public double getTransactionPriceNoTax(int quantity) {
        double price = quantity * pricePerUnit;
        return price;
    }
    
    public double getShippingCost(Location to) {
        double distanceSquared = 0;
        if(!to.getWorld().equals(loc.getWorld())) {
            distanceSquared += 100;
        }
        Vector from = new Vector(loc.getX(), loc.getY(), loc.getZ());
        Vector dest = new Vector(to.getX(), to.getY(), to.getZ());
        distanceSquared += from.distanceSquared(dest);
        return distanceSquared *= 0.0001;
    }
    
    public double getTax(int quantity) {
        double tax = quantity * pricePerUnit * 0.015;
        return tax; 
    }
    
    public double getSubTotalWithTax(int quantity) {
        double price = quantity * pricePerUnit * 1.015;
        return price;
    }
    
    public double getTotal(int quantity, Location to) {
        return quantity * pricePerUnit * 1.015 + getShippingCost(to);
    }
    
    public long getOrderId() {
        return this.id;
    }
    
    /**
     * Inserts order into MySQL using a new ID
     * 
     * @return -1 for normal operation<br> 0 for SQL Error
     */
    public int createNewOrder() {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO `orders` (`item`,`quantity`,`type`,`price`,`ordertype`,`npc`,`owner`,`location`) VALUES ('");
        //Append item here
        JsonWriter writer = new JsonWriter();
        writer.writeItem(this.item);
        String written = writer.toString();
        writer.close();
        query.append(written).append("', '");
        query.append(this.quantity);
        query.append("', '");
        query.append(item.getType().name());
        query.append("', '");
        query.append(this.pricePerUnit);
        query.append("', '");
        query.append(this.orderType.toString());
        query.append("', '");
        query.append(this.isNPC ? 1 : 0);
        if(this.isNPC) {
            query.append("', NULL, '");
        } else {
            query.append("', '");
            query.append(owner.getName());
            query.append("', '");
        }
        query.append(this.loc.getWorld().getName());
        query.append(":");
        query.append(this.loc.getBlockX());
        query.append(":");
        query.append(this.loc.getBlockY());
        query.append(":");
        query.append(this.loc.getBlockZ());
        query.append("');");
        try {
            MarketPlugin.instance.executeQuery(query.toString(), true);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return -1;
    }
    
    /**
     * Processes an order and updates its remaining quantity in MySQL, as well as within the object
     * 
     * @param quantity
     * @return -1 for normal operation<br>
     *  0 if requested quantity is greater than the order's remaining quantity<br>
     *  1 for SQL error
     */
    public int processOrder(int quantity) {
        if(quantity > this.quantity) {
            return 0;
        }
        String query = "UPDATE `orders` SET `quantity`='" + (this.quantity - quantity) + "' WHERE id='" + this.id + "'";
        try {
            MarketPlugin.instance.executeQuery(query,true);
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
        this.quantity -= quantity;
        return -1;
    }
    
    
    
    public static Order fromMySqlId(int id) throws SQLException {
        String query = "SELECT * FROM `orders` WHERE id = '" + id + "'";
        ResultSet set = MarketPlugin.instance.executeQuery(query,false);
        if(!set.first()) {
            return null;
        } else {
            JsonReader reader = new JsonReader(set.getString("item"));
            ItemStack item = reader.readItem();
            reader.close();
            int quantity = set.getInt("quantity");
            if(quantity == 0) {
                return null;
            }
            double price = set.getDouble("price");
            OrderType orderType = OrderType.fromString(set.getString("ordertype"));
            boolean isNPC = set.getInt("npc") == 1 ? true : false;
            Player owner = null;
            if(!isNPC) {
                owner = Bukkit.getPlayerExact(set.getString("owner"));
            }
            String loc = set.getString("location");
            String[] locData = loc.split(":");
            Location constructLoc = new Location(Bukkit.getWorld(locData[0]), Double.parseDouble(locData[1]), Double.parseDouble(locData[2]), Double.parseDouble(locData[3]));
            return new Order(id, item, quantity, price, orderType, owner, isNPC, constructLoc);
        }
    }
    
    public static List<Order> fromMySqlResults(ResultSet set) throws SQLException {
        List<Order> resultOrders = new LinkedList<Order>();
        while(set.next()) {
            int id = set.getInt("id");
            int quantity = set.getInt("quantity");
            if(quantity == 0) {
                continue;
            }
            JsonReader reader = new JsonReader(set.getString("item"));
            ItemStack item = reader.readItem();
            reader.close();
            double price = set.getDouble("price");
            OrderType orderType = OrderType.fromString(set.getString("ordertype"));
            boolean isNPC = set.getInt("npc") == 1 ? true : false;
            Player owner = null;
            if(!isNPC) {
                owner = Bukkit.getPlayerExact(set.getString("owner"));
            }
            String loc = set.getString("location");
            String[] locData = loc.split(":");
            Location constructLoc = new Location(Bukkit.getWorld(locData[0]), Double.parseDouble(locData[1]), Double.parseDouble(locData[2]), Double.parseDouble(locData[3]));
            Order order = new Order(id, item, quantity, price, orderType, owner, isNPC, constructLoc);
            resultOrders.add(order);
        }
        return resultOrders;
    }


    public double getPricePerUnit() {
        return this.pricePerUnit;
    }
    
    
}
