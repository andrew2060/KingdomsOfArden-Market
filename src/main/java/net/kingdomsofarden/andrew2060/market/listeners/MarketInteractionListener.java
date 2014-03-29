package net.kingdomsofarden.andrew2060.market.listeners;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kingdomsofarden.andrew2060.market.gui.CategoryBrowser;
import net.kingdomsofarden.andrew2060.market.gui.MainMenu;
import net.kingdomsofarden.andrew2060.market.gui.OrderBrowser;
import net.kingdomsofarden.andrew2060.market.gui.CategoryBrowser.CategoryBrowserInventoryHolder;
import net.kingdomsofarden.andrew2060.market.gui.MainMenu.MainMenuInventoryHolder;
import net.kingdomsofarden.andrew2060.market.gui.OrderBrowser.OrderBrowserInventoryHolder;
import net.kingdomsofarden.andrew2060.market.gui.ProcessOrderSell;
import net.kingdomsofarden.andrew2060.market.gui.ProcessOrderSell.ProcessOrderBuyInventoryHolder;
import net.kingdomsofarden.andrew2060.market.types.Order;
import net.kingdomsofarden.andrew2060.market.types.OrderType;
import net.kingdomsofarden.andrew2060.market.util.ItemCategory;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MarketInteractionListener implements Listener {

    private static class ItemInfo {
        Order order;
        int page;
        ItemCategory cat;
        OrderType type;
        int mode;
        public ItemInfo(ItemCategory cat, OrderType type, Order order, int page, int mode) {
            this.page = page;
            this.type = type;
            this.cat = cat;
            this.mode = mode;
            this.order = order;
        }

    }

    private Map<Inventory,ItemInfo> invMap;
    private Plugin plugin;

    public MarketInteractionListener() {
        invMap = new HashMap<Inventory,ItemInfo>();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true) 
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();        
        if(holder == null) {
            return;
        }
        if(holder instanceof ProcessOrderBuyInventoryHolder) {
            event.setCancelled(true);

        } else if (holder instanceof MainMenuInventoryHolder) {
            event.setCancelled(true);
            processMainMenu(event);
        } else if (holder instanceof OrderBrowserInventoryHolder) {
            event.setCancelled(true);
            processOrderBrowser(event);
        } else if (holder instanceof CategoryBrowserInventoryHolder) {
            event.setCancelled(true);
            processCategoryBrowser(event);
        } else {
            return;
        }
    }

    private void processOrderBrowser(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(slot <= 0 || slot > 54) {
            return;
        }
        ItemInfo info = invMap.remove(event.getInventory());
        if(slot < 46) {
            ItemStack item = event.getCursor();
            List<String> lore = item.getItemMeta().getLore();
            String find = null;
            for(String s : lore) {
                if(s.contains("#"));
                find = s.replaceAll("^[.0-9]", "");
            }
            int orderId = Integer.parseInt(find);
            Order order;
            try {
                order = Order.fromMySqlId(orderId);
            } catch (SQLException e) {
                System.out.println("Exception getting order ID: " + orderId);
                e.printStackTrace();
                final HumanEntity ent = event.getWhoClicked();
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        ent.closeInventory();
                    }
                 });
                return;
            }
            final HumanEntity ent = event.getWhoClicked();
            switch(info.type) {
            case BUY: {
                
            }
            case SELL: {
                final Inventory inv = ProcessOrderSell.createGUI(order, 0, event.getWhoClicked().getLocation()); 
                invMap.put(inv, new ItemInfo(info.cat, info.type, order, info.page, info.mode));
                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                   @Override
                   public void run() {
                       ent.closeInventory();
                       ent.openInventory(inv);
                   }
                });
            }
            case AUCTION: {
                
            }
                
            }
        } else {
            
        }
    }

    private void processCategoryBrowser(InventoryClickEvent event) {
        int slot = event.getSlot();
        if(slot <= 0 || slot > 54) {
            return;
        }
        if(slot > 52) {
            ItemInfo info = invMap.get(event.getInventory());
            int page = info.page;
            if(slot == 53) {
                if(page == 1) {
                    final Inventory inv = MainMenu.constructMainMenuInventory();
                    invMap.put(inv, new ItemInfo(info.cat, null, null, 0, 0));
                    final HumanEntity ent = event.getWhoClicked();
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {

                        @Override
                        public void run() {
                            ent.closeInventory();
                            ent.openInventory(inv);
                        }
                        
                    });
                } else {
                    final Inventory inv = CategoryBrowser.getCategoryInventory(info.cat, page - 1);
                    invMap.put(inv, new ItemInfo(info.cat, null, null, page - 1, 0));
                    final HumanEntity ent = event.getWhoClicked();
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {

                        @Override
                        public void run() {
                            ent.closeInventory();
                            ent.openInventory(inv);
                        }
                        
                    });
                }
            } else {
                final Inventory inv = CategoryBrowser.getCategoryInventory(info.cat, page + 1);
                invMap.put(inv, new ItemInfo(info.cat, null, null, page + 1, 0));
                final HumanEntity ent = event.getWhoClicked();
                Bukkit.getScheduler().runTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        ent.closeInventory();
                        ent.openInventory(inv);
                    }
                    
                });
            }
        } else {
            try {
                final Inventory inv = OrderBrowser.getOrders(OrderType.BUY, event.getCursor().getType(), 1, 0);
                final HumanEntity ent = event.getWhoClicked();
                Bukkit.getScheduler().runTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        ent.closeInventory();
                        ent.openInventory(inv);
                    }
                    
                });
            } catch (SQLException e) {
                ((Player)event.getWhoClicked()).sendMessage("There was a problem processing your request");
                final HumanEntity ent = event.getWhoClicked();
                Bukkit.getScheduler().runTask(plugin, new Runnable() {

                    @Override
                    public void run() {
                        ent.closeInventory();
                    }
                    
                });
            }
        }
        invMap.remove(event.getInventory());
    }

    private void processMainMenu(InventoryClickEvent event) {
        ItemCategory cat = null;
        int slot = event.getSlot();
        if(slot <= 0 || slot > 14) {
            return;
        }
        switch(slot) {
        case 1: {
            cat = ItemCategory.BLOCKS;
            break;
        }
        case 2: {
            cat = ItemCategory.ORES;
            break;
        }
        case 3: {
            cat = ItemCategory.PLANTS;
            break;
        }
        case 4:  {
            cat = ItemCategory.FOOD;
            break;
        }
        case 5: {
            cat = ItemCategory.REDSTONE;
            break;
        }
        case 6: {
            cat = ItemCategory.TOOLS;
            break;
        }
        case 7: {
            cat = ItemCategory.ARMOR;
            break;
        }
        case 8: {
            cat = ItemCategory.AMMO;
            break;
        }
        case 9: {
            cat = ItemCategory.BLACKSMITHING;
            break;
        }
        case 10: {
            cat = ItemCategory.ALCHEMY;
            break;
        }
        case 11: {
            cat = ItemCategory.MAGIC;
            break;
        }
        case 12: {
            cat = ItemCategory.TRAVEL;
            break;
        }
        case 13: {
            cat = ItemCategory.SPAWNEGG;
            break;
        }
        case 14: {
            cat = ItemCategory.MISC;
            break;
        }

        }
        final Inventory inv = CategoryBrowser.getCategoryInventory(cat, 1);
        invMap.put(inv, new ItemInfo(cat, null, null, 1, 0));
        invMap.remove(event.getInventory());
        final HumanEntity ent = event.getWhoClicked();
        Bukkit.getScheduler().runTask(plugin, new Runnable() {
            
            @Override
            public void run() {
                ent.closeInventory();
                ent.openInventory(inv);
            }
            
        });
    }

}
