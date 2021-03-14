package me.katnissali.cloakauctions.AuctionHouse;

import me.katnissali.cloakauctions.Core.Dependencies;
import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Page {

    private String categoryName;
    private int pageNumber;
    private List<Item> items = new ArrayList();
    private Inventory inventory;

    private ItemStack lastArrow;
    private ItemStack nextArrow;

    private boolean isFull;

    public Page(String categoryName, int pageNumber){
        this.categoryName = categoryName;
        this.pageNumber = pageNumber;
        this.inventory = getTemplate(this.categoryName);
        updateIsFull();
    }

    //  GETTERS
    public boolean isEmpty(){
        for(ItemStack item : inventory.getContents()){
            if(item != null || !item.getType().equals(Material.AIR)){
                return false;
            }
        }
        return true;
    }
    public Inventory getInventory(){
        return inventory;
    }
    public int getPageNumber(){
        return pageNumber;
    }
    public List<Item> getItems(){
        return items;
    }
    public boolean hasItem(ItemStack item){
        for(Item i : items){
            if(item.isSimilar(i.getItem())){
                return true;
            }
        }
        return false;
    }
    public boolean hasItem(Item item){
        return items.contains(item);
    }
    public boolean isFull(){
        return isFull;
    }
    public ItemStack getNextArrow(){ return nextArrow; }
    public ItemStack getLastArrow(){ return lastArrow; }
    private Inventory getTemplate(String categoryName){

        //  BORDER
        Inventory inventory = Bukkit.createInventory(null, 54, "Category: " + categoryName);
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        ItemMeta paneMeta = pane.getItemMeta();
        paneMeta.setDisplayName(" ");
        paneMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        pane.setItemMeta(paneMeta);
        for(int i = 0; i <= 8; i++){
            inventory.setItem(i, pane);
        }
        for(int i = 45; i <= 53; i++){
            inventory.setItem(i, pane);
        }

        //  ITEMS
        ItemStack page = setDisplayName(new ItemStack(Material.PAPER, pageNumber), " ");
        ItemStack emerald = setDisplayName((new ItemStack(Material.EMERALD_BLOCK)), ("" + ChatColor.GREEN + ChatColor.BOLD + "Next Page >> "));
        ItemStack redstone = setDisplayName((new ItemStack(Material.REDSTONE_BLOCK)), ("" + ChatColor.RED + ChatColor.BOLD + "<< Previous Page"));
        ItemStack compass = setDisplayName((new ItemStack(Material.COMPASS)), " ");

        inventory.setItem(49, page);
        //inventory.setItem(1, compass);

        try {
            if (Util.getCategoryManager().getCategory(categoryName).getManager().getFirstPage() != this) {
                inventory.setItem(47, redstone);
            }
        } catch (NullPointerException e){}

        return inventory;
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

    //  SETTERS
    public void updateIsFull(){
        for(int i = 9; i <= 45; i++){
            if(inventory.getItem(i) == null
               || inventory.getItem(i).getType().equals(Material.AIR)){
                isFull = false;
                return;
            }
        }
        isFull = true;
    }
    public void setInventory(Inventory inventory){
        this.inventory = inventory;
        updateIsFull();
    }
    public void setItems(List<Item> items){
        this.items = items;
        updateIsFull();
    }
    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
        updateIsFull();
    }
    private void addItemToInventory(Item item){
        if(isFull){
            Util.getCategoryManager().getCategory(this).getManager().newPage();
        }
        for(int i = 0; i <= 53; i++){
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)){
                inventory.setItem(i, item.getDisplayItem());
                item.setSlot(i);
                updateIsFull();
                return;
            }
            updateIsFull();
        }

    }
    public void removeItemFromInventory(int slot){
        inventory.clear(slot);
        for(int i = (slot + 1); i < 45; i++){
            Item item = Util.getCategoryManager().getCategory(this).getManager().getItem(i);
            if(item == null){
                return;
            }
            item.setSlot(i-1);
            inventory.setItem((i-1), inventory.getItem(i));
        }
        updateIsFull();

    }
    public void buyItem(Player player, Item item){
        // removeItemFromInventory(item.getSlot());
      //  Dependencies.getEconomy().withdrawPlayer(player, item.getPrice());
      //  Dependencies.getEconomy().depositPlayer(item.getSeller(), item.getPrice());
        //  TODO uncomment
        items.remove(item);
        updateIsFull();
    }
    public void buyItem(OfflinePlayer player, Item item){
        // removeItemFromInventory(item.getSlot());
        //  TODO uncomment eco money
      //  Dependencies.getEconomy().withdrawPlayer(player, item.getPrice());
    //    Dependencies.getEconomy().depositPlayer(item.getSeller(), item.getPrice());
       // items.remove(item);
        updateIsFull();
    }
    public void sellItem(Player player, int price){
        Item item = new Item(player, price);
        addItemToInventory(item);
        items.add(item);
        updateIsFull();
    }
    public void sellItem(String player, ItemStack item, int price){
        Item i = new Item(player, item, price);
        addItemToInventory(i);
        items.add(i);
        updateIsFull();
    }
    public void sellItem(String player, String time, ItemStack item, int price){
        Item i = new Item(player, time, item, price);
        items.add(i);
        if(i.hasTimeLeft()) {
            addItemToInventory(i);
            updateIsFull();
        } else {
            Util.getExpiredItemsManager().addItem(i);
        }
    }
}
