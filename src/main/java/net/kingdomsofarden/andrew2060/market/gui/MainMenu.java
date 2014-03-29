package net.kingdomsofarden.andrew2060.market.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu {
    public static class MainMenuInventoryHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
        
    }
    
    private static ItemStack blocks;
    private static ItemStack ores;
    private static ItemStack plants;
    private static ItemStack food;
    private static ItemStack redstone;
    private static ItemStack tools;
    private static ItemStack armor;
    private static ItemStack ammo;
    private static ItemStack blacksmith;
    private static ItemStack alchemy;
    private static ItemStack magic;
    private static ItemStack travel;
    private static ItemStack spawnEggs;
    private static ItemStack misc;
    
    static {
        blocks = new ItemStack(Material.GRASS);
        ItemMeta blockMeta = blocks.getItemMeta();
        blockMeta.setDisplayName("Blocks");
        blocks.setItemMeta(blockMeta);
        
        ores = new ItemStack(Material.DIAMOND);
        ItemMeta oreMeta = ores.getItemMeta();
        oreMeta.setDisplayName("Ores");
        ores.setItemMeta(oreMeta);
        
        plants = new ItemStack(Material.WHEAT);
        ItemMeta plantMeta = plants.getItemMeta();
        plantMeta.setDisplayName("Plants");
        plants.setItemMeta(plantMeta);
        
        food = new ItemStack(Material.MUSHROOM_SOUP);
        ItemMeta foodMeta = food.getItemMeta();
        foodMeta.setDisplayName("Consumables");
        food.setItemMeta(foodMeta);
        
        redstone = new ItemStack(Material.REDSTONE);
        ItemMeta redstoneMeta = redstone.getItemMeta();
        redstoneMeta.setDisplayName("Redstone");
        redstone.setItemMeta(redstoneMeta);
        
        tools = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta toolMeta = tools.getItemMeta();
        toolMeta.setDisplayName("Tools & Weapons");
        tools.setItemMeta(toolMeta);
        
        armor = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta armorMeta = armor.getItemMeta();
        armorMeta.setDisplayName("Armor");
        armor.setItemMeta(armorMeta);
        
        ammo = new ItemStack(Material.ARROW);
        ItemMeta ammoMeta = ammo.getItemMeta();
        ammoMeta.setDisplayName("Ammunition and Charges");
        ammo.setItemMeta(ammoMeta);
                
        blacksmith = new ItemStack(Material.ANVIL);
        ItemMeta blacksmithMeta = blacksmith.getItemMeta();
        blacksmithMeta.setDisplayName("Blacksmithing Equipment and Materials");
        blacksmith.setItemMeta(blacksmithMeta);
        
        alchemy = new ItemStack(Material.BREWING_STAND_ITEM);
        ItemMeta alchemyMeta = alchemy.getItemMeta();
        alchemyMeta.setDisplayName("Potions and Ingredients");
        alchemy.setItemMeta(alchemyMeta);
        
        magic = new ItemStack(Material.ENCHANTMENT_TABLE);
        ItemMeta magicMeta = magic.getItemMeta();
        magicMeta.setDisplayName("Magic");
        magic.setItemMeta(magicMeta);
        
        travel = new ItemStack(Material.MINECART);
        ItemMeta travelMeta = travel.getItemMeta();
        travelMeta.setDisplayName("Travel and Navigation");
        travel.setItemMeta(travelMeta);
        
        spawnEggs = new ItemStack(Material.MONSTER_EGG);
        ItemMeta spawnEggMeta = spawnEggs.getItemMeta();
        spawnEggMeta.setDisplayName("Spawn Eggs");
        spawnEggs.setItemMeta(spawnEggMeta);
        
        misc = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta miscMeta = misc.getItemMeta();
        miscMeta.setDisplayName("Miscellaneous");
        misc.setItemMeta(miscMeta);
    }
    
    /**
     * Constructs the inventory required for the main menu (category selection)
     * @return A category selection inventory view
     */
    public static Inventory constructMainMenuInventory() {       
        Inventory inv = Bukkit.createInventory(new MainMenuInventoryHolder(), 18, "Global Market Browser (Select a Category)");
        inv.setItem(1, blocks);
        inv.setItem(2, ores);
        inv.setItem(3, plants);
        inv.setItem(4, food);
        inv.setItem(5, redstone);
        inv.setItem(6, tools);
        inv.setItem(7, armor);
        inv.setItem(8, ammo);
        inv.setItem(9, blacksmith);
        inv.setItem(10, alchemy);
        inv.setItem(11, magic);
        inv.setItem(12, travel);
        inv.setItem(13, spawnEggs);
        inv.setItem(14, misc);
        return inv;
        
    }

}
