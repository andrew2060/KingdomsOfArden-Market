package net.kingdomsofarden.andrew2060.market.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import net.kingdomsofarden.andrew2060.market.MarketPlugin;
import net.kingdomsofarden.andrew2060.market.types.Order;
import net.kingdomsofarden.andrew2060.market.types.OrderType;

public class OrderBrowser {

    private static ItemStack orderPriceAsc;
    private static ItemStack orderPriceDesc;
    private static ItemStack orderOwnerAsc;
    private static ItemStack orderOwnerDesc;
    private static ItemStack orderQuantityAsc;
    private static ItemStack orderQuantityDesc;
    
    private static ItemStack nextPage;
    private static ItemStack previousPage;
    
    static {
        orderPriceAsc = new ItemStack(Material.WOOL);
        ItemMeta orderPriceAscMeta = orderPriceAsc.getItemMeta();
        orderPriceAscMeta.setDisplayName("Order by Price (Lowest First)");
        orderPriceDesc = new ItemStack(Material.WOOL);
        ItemMeta orderPriceDescMeta = orderPriceDesc.getItemMeta();
        orderPriceDescMeta.setDisplayName("Order by Price (Highest First)");
        orderOwnerAsc = new ItemStack(Material.WOOL);
        ItemMeta orderOwnerAscMeta = orderOwnerAsc.getItemMeta();
        orderOwnerAscMeta.setDisplayName("Order by Owner (Alphabetical A-Z)");
        orderOwnerDesc = new ItemStack(Material.WOOL);
        ItemMeta orderOwnerDescMeta = orderOwnerDesc.getItemMeta();
        orderOwnerDescMeta.setDisplayName("Order by Owner (Alphabetical Z-A)");
        orderQuantityAsc = new ItemStack(Material.WOOL);
        ItemMeta orderQuantityAscMeta = orderQuantityAsc.getItemMeta();
        orderQuantityAscMeta.setDisplayName("Order by Quantity (Lowest First)");
        orderQuantityDesc = new ItemStack(Material.WOOL);
        ItemMeta orderQuantityDescMeta = orderQuantityDesc.getItemMeta();
        orderQuantityDescMeta.setDisplayName("Order by Quantity (Highest first)");
        nextPage = new ItemStack(Material.WOOL);
        ((Wool) nextPage.getData()).setColor(DyeColor.GREEN);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName("Next Page");
        previousPage = new ItemStack(Material.WOOL);
        ((Wool) previousPage.getData()).setColor(DyeColor.RED);
        ItemMeta previousPageMeta = previousPage.getItemMeta();
        previousPageMeta.setDisplayName("Previous Page");
        orderPriceAsc.setItemMeta(orderPriceAscMeta);
        orderPriceDesc.setItemMeta(orderPriceDescMeta);
        orderOwnerAsc.setItemMeta(orderOwnerAscMeta);
        orderOwnerDesc.setItemMeta(orderOwnerDescMeta);
        orderQuantityAsc.setItemMeta(orderQuantityAscMeta);
        orderQuantityDesc.setItemMeta(orderQuantityDescMeta);
        nextPage.setItemMeta(nextPageMeta);
        previousPage.setItemMeta(previousPageMeta);

    }
    
    public static Inventory getOrders(OrderType orderType, Material type, int page, int mode) throws SQLException {
        //Define menu bar items
        ItemStack modeSelector = new ItemStack(Material.WOOL);
        List<String> modeSelectorLore = new ArrayList<String>(3);
        modeSelectorLore.add(ChatColor.GRAY + "Red: Browsing Buy Orders");
        modeSelectorLore.add(ChatColor.GRAY + "Green: Browsing Sell Orders");
        modeSelectorLore.add(ChatColor.GRAY + "Blue: Browsing Auctions");
        ItemMeta modeSelectorMeta = modeSelector.getItemMeta();
        modeSelectorMeta.setLore(modeSelectorLore);
        modeSelectorMeta.setDisplayName("Order Mode Selector");
        modeSelector.setItemMeta(modeSelectorMeta);
        switch(orderType) {
        
        case AUCTION:
            ((Wool)modeSelector.getData()).setColor(DyeColor.BLUE);
            break;
        case BUY:
            ((Wool)modeSelector.getData()).setColor(DyeColor.RED);
            break;
        case SELL:
            ((Wool)modeSelector.getData()).setColor(DyeColor.GREEN);
            break;
        
        }
        ItemStack priceAsc = orderPriceAsc.clone();
        ItemStack priceDesc = orderPriceDesc.clone();
        ItemStack ownerAsc = orderOwnerAsc.clone();
        ItemStack ownerDesc = orderOwnerDesc.clone();
        ItemStack quantityAsc = orderQuantityAsc.clone();
        ItemStack quantityDesc = orderQuantityDesc.clone();
        
        //StringBuilder for Menu Name
        StringBuilder menuName = new StringBuilder();
        menuName.append("Market Order Browser (Page ");
        menuName.append(page);
        menuName.append(") - Browsing ");
        menuName.append(orderType.toString());
        menuName.append(" Orders");
        
        //Build MySQL Query
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `orders` WHERE `ordertype`='" + orderType.toString() + "' AND `type`='" + type.name() + " ");
        switch(mode) {
        
        case 0: { //Sort results from lowest price to highest
            query.append("ORDER BY `price` ASC");
            ((Wool)priceAsc.getData()).setColor(DyeColor.ORANGE);;
            break;
        }
        case 1: { //Sort results from highest price to lowest
            query.append("ORDER BY `price` DESC");
            ((Wool)priceDesc.getData()).setColor(DyeColor.ORANGE);
            break;
        }
        case 2: { //Sort results by owner alphabetical A-Z
            query.append("ORDER BY `owner` ASC");
            ((Wool)ownerAsc.getData()).setColor(DyeColor.ORANGE);
            break;
        }
        case 3: { //Sort results by owner alphabetical Z-A
            query.append("ORDER BY `owner` DESC");
            ((Wool)ownerDesc.getData()).setColor(DyeColor.ORANGE);
            break;
        }
        case 4: { //Sort results by quantity lowest to highest
            query.append("ORDER BY `quantity` ASC");
            ((Wool)quantityAsc.getData()).setColor(DyeColor.ORANGE);
            break;
        }
        case 5: { //Sort results by quantity highest to lowest
            query.append("ORDER BY `quantity` DESC");
            ((Wool)quantityDesc.getData()).setColor(DyeColor.ORANGE);
            break;
        }
        
        }
        query.append("LIMIT 46 OFFSET ");   //46 allows us to check 1 ahead to see if a next page is necessary
        query.append((page - 1) * 45);
        query.append(";");

        //Run and process query, insert results into inventory
        ResultSet results = MarketPlugin.instance.executeQuery(query.toString(), false);
        List<Order> orders = Order.fromMySqlResults(results);
        Inventory inv = Bukkit.createInventory(new OrderBrowserInventoryHolder(), 54, menuName.toString());
        for(int i = 0 ; i < orders.size() && i < 45 ; i++) {
            inv.setItem(i+1, orders.get(i).getOrderIcon());
        }
        
        //Using results of query, create menu bar
        inv.setItem(46, modeSelector);
        inv.setItem(47, priceAsc);
        inv.setItem(48, priceDesc);
        inv.setItem(49, ownerAsc);
        inv.setItem(50, ownerDesc);
        inv.setItem(51, quantityAsc);
        inv.setItem(52, quantityDesc);
        
        if(page > 1) {
            inv.setItem(53, previousPage);
        }
        if(orders.size() == 46) {
            inv.setItem(54, nextPage);
        }
        return inv;
    }
    
    public static class OrderBrowserInventoryHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
        
    }
    
}
