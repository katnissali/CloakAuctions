package me.katnissali.cloakauctions.Managers;

import me.katnissali.cloakauctions.AuctionHouse.Category;
import me.katnissali.cloakauctions.AuctionHouse.Item;
import me.katnissali.cloakauctions.AuctionHouse.Page;
import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CategoryManager {

    private Inventory inventory;
    private HashMap<String, Category> categories = new HashMap();

    public CategoryManager(){

        for(String path : Util.getConfigManager().getCategoriesConfig().getConfigurationSection("Categories.").getKeys(false)){
            String name = path;
            path = "Categories." + path + ".";
            ItemStack displayItem = Util.getConfigManager().getCategoriesConfig().getItemStack(path + "display-item");
            List<String> materials = Util.getConfigManager().getCategoriesConfig().getStringList(path + "items");
            newCategory(name, displayItem, materials);
        }

        inventory = getTemplate();
        int index = 9;
        for(Category category : categories.values()){
            inventory.setItem(index, category.getDisplayItem());
            index++;
        }
    }

    public void updateInventory(){
        inventory = getTemplate();
        int index = 9;
        for(Category category : categories.values()){
            inventory.setItem(index, category.getDisplayItem());
            index++;
        }
    }

    public void saveConfig(){
        Util.testPrint("BEFORE SAVECONFIG");
        //Util.getCategoryManager().saveConfig();
        for(Category category : Util.getCategoryManager().getCategories()){
            for(Page page : category.getManager().getPages()){
                for(Item item : page.getItems()){
                    System.out.println("Item = " + item);
                }
            }
        }

        List<ItemStack> items = new ArrayList();
        for(Category category : Util.getCategoryManager().getCategories()) {
            for (Page page : category.getManager().getPages()) {
                for (Item item : page.getItems()) {
                    System.out.println("item = " + item);
                    items.add(item.getDisplayItem());
                }
            }
        }
        System.out.println("list = " + items);
        Util.getConfigManager().getItemConfig().set("Items", items);

        System.out.println("Item config name = " + Util.getConfigManager().getItemConfig().getName());
        System.out.println("Item config abailablr sections = " + Util.getConfigManager().getItemConfig().getKeys(false));
        System.out.println("Item section = " + Util.getConfigManager().getItemConfig().getConfigurationSection("Items"));
        System.out.println("item values = " + Util.getConfigManager().getItemConfig().getConfigurationSection("Items").getValues(false));

        Util.getConfigManager().saveFiles();


        Util.testPrint("AFTER SAVECONFIG");
        for(Category category : Util.getCategoryManager().getCategories()){
            for(Page page : category.getManager().getPages()){
                for(Item item : page.getItems()){
                    System.out.println("Item = " + item);
                }
            }
        }

    }
    public void resetConfig(){
        Util.getConfigManager().getItemConfig().set("Items", new ArrayList());
        Util.getConfigManager().saveFiles();
    }
    public Category getCategory(Page page){
        for(Category category : categories.values()){
            if(category.getManager().getPages().contains(page)){
                return category;
            }
        }
        return null;
    }
    public Collection<Category> getCategories(){ return categories.values(); }
    public void sellItem(String playerName, ItemStack item, int price){
        Category category = getCategory(item);
        category.getManager().sellItem(playerName, item, price);
    }
    public void sellItem(String playerName, String time, ItemStack item, int price){
        Category category = getCategory(item);
        category.getManager().sellItem(playerName, time, item, price);
    }
    public Inventory getInventory(){ return inventory; }
    public Category getCategory(String str){
        return categories.get(str);
    }
    public Category getCategory(ItemStack item){
        for(Category category : categories.values()){
            if(category.getDisplayItem().isSimilar(item)){
                return category;
            }
        }
        Category category = null;
        for(Category c : categories.values()){
            category = c;
        }
        return category;
    }
    public Category getCategory(Item item){
        for(Category category : categories.values()){
            for(Page page : category.getManager().getPages()){
                if(page.getItems().contains(item)){
                    return category;
                }
            }
        }
        return null;
    }
    public void newCategory(String name, ItemStack displayItem, List<String> items){
        Category c = new Category(name, displayItem, items);
        categories.put(name, c);
    }
    private ItemStack setDisplayName(ItemStack item, String str){
        ItemMeta meta = item.getItemMeta();
        if(str != null) {
            meta.setDisplayName(str);
        } else {
            meta.setLore(new ArrayList());
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        return item;
    }
    public Inventory getTemplate(){

        //  BORDER
        Inventory inventory = Bukkit.createInventory(null, 54, "Auction House");
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(" ");
        paneMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(paneMeta);

        ItemStack bottle = new ItemStack(Material.GLASS_BOTTLE);

        for(int i = 0; i <= 8; i++){
            inventory.setItem(i, pane);
        }
        for(int i = 45; i <= 53; i++){
            inventory.setItem(i, pane);
        }
        inventory.setItem(46, bottle);

        //  ITEMS
        ItemStack compass = setDisplayName((new ItemStack(Material.COMPASS)), ("" + ChatColor.WHITE + ChatColor.BOLD + "Categories"));
        inventory.setItem(4, compass);

        return inventory;
    }
}
