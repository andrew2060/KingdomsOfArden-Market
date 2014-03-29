package net.kingdomsofarden.andrew2060.market.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.kingdomsofarden.andrew2060.market.types.Order;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class ProcessOrderSell {
    
    private static ItemStack one;
    private static ItemStack ten;
    private static ItemStack hundred;
    private static ItemStack thousand;
    private static ItemStack confirm;
    private static ItemStack cancel;
    
    private static DecimalFormat dF;
    
    static {      
        one = new ItemStack(Material.WOOD);
        ten = new ItemStack(Material.REDSTONE_BLOCK);
        hundred = new ItemStack(Material.IRON_BLOCK);
        thousand = new ItemStack(Material.DIAMOND_BLOCK);
        
        confirm = new ItemStack(Material.WOOL);
        cancel = new ItemStack(Material.WOOL);
        
        ((Wool)confirm.getData()).setColor(DyeColor.GREEN);
        ((Wool)cancel.getData()).setColor(DyeColor.RED);
        
        ItemMeta confirmMeta = confirm.getItemMeta();
        ItemMeta cancelMeta = cancel.getItemMeta();
        
        confirmMeta.setDisplayName("Confirm");
        cancelMeta.setDisplayName("Cancel");
        
        confirm.setItemMeta(confirmMeta);
        cancel.setItemMeta(cancelMeta);
        
        List<String> oneLore = new ArrayList<String>(2);
        oneLore.add(0,ChatColor.GRAY + "Left Click: Add 1 to current order");
        oneLore.add(1,ChatColor.GRAY + "Right Click: Remove 1 from current order");
        List<String> tenLore = new ArrayList<String>(2);
        tenLore.add(0,ChatColor.GRAY + "Left Click: Add 10 to current order");
        tenLore.add(1,ChatColor.GRAY + "Right Click: Remove 10 from current order");
        List<String> hundredLore = new ArrayList<String>(2);
        hundredLore.add(0,ChatColor.GRAY + "Left Click: Add 100 to current order");
        hundredLore.add(1,ChatColor.GRAY + "Right Click: Remove 100 from current order");
        List<String> thousandLore = new ArrayList<String>(2);
        thousandLore.add(0,ChatColor.GRAY + "Left Click: Add 1000 to current order");
        thousandLore.add(1,ChatColor.GRAY + "Right Click: Remove 1000 from current order");
        
        ItemMeta oneMeta = one.getItemMeta();
        ItemMeta tenMeta = ten.getItemMeta();
        ItemMeta hundredMeta = hundred.getItemMeta();
        ItemMeta thousandMeta = thousand.getItemMeta();
        
        oneMeta.setLore(oneLore);
        tenMeta.setLore(tenLore);
        hundredMeta.setLore(hundredLore);
        thousandMeta.setLore(thousandLore);
        
        oneMeta.setDisplayName("Modify Order Quantity");
        tenMeta.setDisplayName("Modify Order Quantity");
        hundredMeta.setDisplayName("Modify Order Quantity");
        thousandMeta.setDisplayName("Modify Order Quantity");
        
        one.setItemMeta(oneMeta);
        ten.setItemMeta(tenMeta);
        hundred.setItemMeta(hundredMeta);
        thousand.setItemMeta(thousandMeta);
        
        dF = new DecimalFormat("##.##");
    }
    
    public static Inventory createGUI(Order order, int amount, Location loc) {
        ItemStack item = order.getOrderIcon();
        ItemStack currOrder = new ItemStack(Material.PAPER);
        ArrayList<String> currentOrderLore = new ArrayList<String>(8);
        currentOrderLore.add(0, ChatColor.GRAY + "Price: " + dF.format(order.getPricePerUnit()) + " Aurums/Unit");
        currentOrderLore.add(1, ChatColor.GRAY + "Total Purchased: " + amount);
        currentOrderLore.add(2, ChatColor.WHITE + "====================================");
        currentOrderLore.add(3, ChatColor.GRAY + "Sub-Total: " + dF.format(order.getTransactionPriceNoTax(amount)) + " Aurums");
        currentOrderLore.add(5, ChatColor.GRAY + "Sales Tax (1.5%): " + dF.format(order.getTax(amount)) + " Aurums");
        currentOrderLore.add(6, ChatColor.WHITE + "====================================");
        currentOrderLore.add(7, ChatColor.GRAY + "Sub-Total w/ Tax: " + dF.format(order.getSubTotalWithTax(amount)) + " Aurums");
        currentOrderLore.add(8, ChatColor.GRAY + "Shipping Costs: " + dF.format(order.getShippingCost(loc)) + " Aurums");
        currentOrderLore.add(9, ChatColor.GRAY + "Item Location: " + loc.getWorld().getName() + " - X:" + loc.getBlockX() + " Y: " + loc.getBlockY() + "Z: " + loc.getBlockZ());
        ItemMeta currOrderMeta = currOrder.getItemMeta();        
        currOrderMeta.setLore(currentOrderLore);
        currOrderMeta.setDisplayName("Current Order");
        Inventory inv = Bukkit.createInventory(new ProcessOrderBuyInventoryHolder(), 18, "Complete Purchase");
        inv.setItem(1, item);
        inv.setItem(9, currOrder);
        inv.setItem(10, one);
        inv.setItem(11, ten);
        inv.setItem(12, hundred);
        inv.setItem(13, thousand);
        inv.setItem(17, cancel);
        inv.setItem(18, confirm);
        return inv;
    }
    
    public static class ProcessOrderBuyInventoryHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
        
    }
    
}
