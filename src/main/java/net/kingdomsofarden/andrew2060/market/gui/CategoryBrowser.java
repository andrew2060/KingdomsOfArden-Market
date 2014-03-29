package net.kingdomsofarden.andrew2060.market.gui;

import java.util.ArrayList;
import java.util.HashSet;

import net.kingdomsofarden.andrew2060.market.util.ItemCategory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

public class CategoryBrowser {
        
    private static ArrayList<Material> blocks = new ArrayList<Material>();
    private static ArrayList<Material> ores = new ArrayList<Material>();
    private static ArrayList<Material> plants = new ArrayList<Material>();
    private static ArrayList<Material> redstone = new ArrayList<Material>();
    private static ArrayList<Material> food = new ArrayList<Material>();
    private static ArrayList<Material> tools = new ArrayList<Material>();
    private static ArrayList<Material> armor = new ArrayList<Material>();
    private static ArrayList<Material> ammo = new ArrayList<Material>();
    private static ArrayList<Material> alchemy = new ArrayList<Material>();
    private static ArrayList<Material> magic = new ArrayList<Material>();
    private static ArrayList<Material> blacksmith = new ArrayList<Material>();
    private static ArrayList<Material> travel = new ArrayList<Material>();
    private static ArrayList<Material> spawnEgg = new ArrayList<Material>();
    private static ArrayList<Material> misc = new ArrayList<Material>();
    private static HashSet<Material> specialCase = new HashSet<Material>();
    
    private static ItemStack nextPage;
    private static ItemStack previousPage;
    
    static {
        for(Material m : Material.values()) {
            switch(ItemCategory.findCategory(m)) {
            
            case ALCHEMY:
                alchemy.add(m);
                break;
            case AMMO:
                ammo.add(m);
                break;
            case ARMOR:
                armor.add(m);
                break;
            case BLACKSMITHING:
                blacksmith.add(m);
                break;
            case BLOCKS:
                blocks.add(m);
                break;
            case FOOD:
                food.add(m);
                break;
            case MAGIC:
                magic.add(m);
                break;
            case MISC:
                misc.add(m);
                break;
            case NONE:
                break;
            case ORES:
                ores.add(m);
                break;
            case PLANTS:
                plants.add(m);
                break;
            case REDSTONE:
                redstone.add(m);
                break;
            case SPAWNEGG:
                spawnEgg.add(m);
                break;
            case TOOLS:
                tools.add(m);
                break;
            case TRAVEL:
                travel.add(m);
                break;
            }
        }
        //Special case
        specialCase.add(Material.EMERALD);
        specialCase.add(Material.DIAMOND_HOE);
        specialCase.add(Material.GOLD_HOE);
        specialCase.add(Material.STONE_HOE);
        specialCase.add(Material.WOOD_HOE);
        
        magic.add(Material.EMERALD);
        
        
        nextPage = new ItemStack(Material.WOOL);
        ((Wool) nextPage.getData()).setColor(DyeColor.GREEN);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        nextPageMeta.setDisplayName("Next Page");
        previousPage = new ItemStack(Material.WOOL);
        ((Wool) previousPage.getData()).setColor(DyeColor.RED);
        ItemMeta previousPageMeta = previousPage.getItemMeta();
        previousPageMeta.setDisplayName("Previous Page");
        nextPage.setItemMeta(nextPageMeta);
        previousPage.setItemMeta(previousPageMeta);
    }
    
    public static Inventory getCategoryInventory(ItemCategory category, int page) {
        ArrayList<Material> items = null;
        switch(category) {
        case ALCHEMY:
            items = alchemy;
            break;
        case AMMO:
            items = ammo;
            break;
        case ARMOR:
            items = armor;
            break;
        case BLACKSMITHING:
            items = blacksmith;
            break;
        case BLOCKS:
            items = blocks;
            break;
        case FOOD:
            items = food;
            break;
        case MAGIC:
            items = magic;
            break;
        case MISC:
            items = misc;
            break;
        case NONE:
            return null;
        case ORES:
            items = ores;
            break;
        case PLANTS:
            items = plants;
            break;
        case REDSTONE:
            items = redstone;
            break;
        case SPAWNEGG:
            items = spawnEgg;
            break;
        case TOOLS:
            items = tools;
            break;
        case TRAVEL:
            items = travel;
            break;
        }
        
        int startpoint = (page - 1) * 52;
        
        Inventory inv = Bukkit.createInventory(new CategoryBrowserInventoryHolder(), 54, "Category Browser (Page " + page + ")");
        
        for(int i = 0, j = startpoint; j < items.size() && i < 53; i++, j++) {
            ItemStack item = new ItemStack(items.get(j),1);
            if(specialCase.contains(items.get(j))) {
                switch(items.get(j)) {
                
                case EMERALD: {
                    if(category.equals(ItemCategory.MAGIC)) {
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.RESET + "Soul Gems");
                        item.setItemMeta(meta);
                    }
                    break;
                }
                case DIAMOND_HOE: {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + "Diamond Scythe");
                    item.setItemMeta(meta);
                    break;
                }
                case IRON_HOE: {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + "Iron Scythe");
                    item.setItemMeta(meta);
                    break;
                }
                case GOLD_HOE: {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + "Gold Scythe");
                    item.setItemMeta(meta);
                    break;
                }
                case STONE_HOE: {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + "Stone Scythe");
                    item.setItemMeta(meta);
                    break;
                }
                case WOOD_HOE: {
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RESET + "Wood Scythe");
                    item.setItemMeta(meta);
                    break;
                }
                default:
                    break;
   
                }
            }
            inv.setItem(i + 1, item);
        }
        
        int nextPageSlot = page * 52;
        if(nextPageSlot <= items.size()) {
            inv.setItem(54, nextPage);
        }
        if(page > 1) {
            inv.setItem(53, previousPage);
        }
        
        return inv;
    }
    public static class CategoryBrowserInventoryHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
        
    }
}
