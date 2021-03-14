package me.katnissali.cloakauctions.AuctionHouse;

import me.katnissali.cloakauctions.Core.Dependencies;
import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item {

    private boolean hasTimeLeft = true;
    private static List<Item> items = new ArrayList();
    private String playerName;
    private int price;
    private ItemStack displayItem;
    private ItemStack item;
    private int slot;


    public Item(String player, ItemStack item, int price){
        this.playerName = player;
        this.price = price;
        this.item = item.clone();
        this.displayItem = loadDisplayItem();
        items.add(this);
    }
    public Item(String player, String time, ItemStack item, int price){
        this.playerName = player;
        this.price = price;
        this.item = item.clone();
        this.displayItem = loadDisplayItem(time);
        items.add(this);
    }
    public Item(Player player, int price){
        this.playerName = player.getName();
        this.price = price;
        this.item = player.getInventory().getItemInHand().clone();
        this.displayItem = loadDisplayItem();
        items.add(this);
    }

    //  GETTERS
    public static List<Item> getItems(){
        return items;
    }
    public int getPrice(){
        return price;
    }
    public ItemStack getDisplayItem(){
        return displayItem.clone();
    }
    public ItemStack getItem(){
        return item.clone();
    }
    public int getSlot(){
        return slot;
    }
    public String toString(){
        return ("ITEM:" + item.toString() + " SELLER:" + playerName + " DISPLAYITEM:" + displayItem + " PRICE:" + price);
    }
    public String getSeller(){
        return playerName;
    }
    public boolean canBuy(Player player){
        // TODO uncomment
        return true;
        //return Dependencies.getEconomy().has(player, price);
    }

    //  SETTERS
    public void setPrice(int price){
        this.price = price;
    }
    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }
    public void setItem(ItemStack item){
        this.item = item;
    }
    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void updateTime(){
        ItemMeta meta = displayItem.getItemMeta();
        List<String> lore = meta.getLore();
        String time = lore.get(5);
        System.out.println("time = " + time);

        String[] t = time.substring(time.indexOf(": ") + 4).split(" ");
        System.out.println("t = " + Arrays.asList(t));
        for(int i = 0; i < t.length; i++){
            t[i] = t[i].substring(0, t[i].length()-1);
        }

        int days = Integer.valueOf(time.substring(time.indexOf("d ")-1, time.indexOf("d ")));
        int hours = Integer.valueOf(time.substring(time.indexOf("h ")-1, time.indexOf("h ")));
        int min = Integer.valueOf(time.substring(time.length()-2, time.length()-1));

        days = Integer.valueOf(t[0]);
        hours = Integer.valueOf(t[1]);
        min = Integer.valueOf(t[2]);

        if(min > 0){
            min--;
        } else {
            if(hours > 0){
                hours--;
                min = 59;
            } else if(days > 0){
                days--;
                hours = 23;
                min = 59;
            } else {
                setHasTime(false);
            }
        }

        lore.set(5, ChatColor.GRAY + "Time Left: " + ChatColor.GOLD + days + "d " + hours + "h " + min + "m");

        meta.setLore(lore);
        displayItem.setItemMeta(meta);
        Util.getCategoryManager().getCategory(this).getManager().getPage(this).getInventory().setItem(getSlot(), getDisplayItem());

    }
    public boolean hasTimeLeft(){
        return hasTimeLeft;
    }
    private void setHasTime(boolean hasTimeLeft){
        this.hasTimeLeft = hasTimeLeft;
        if(!hasTimeLeft){
            Util.print(Util.getPrefix() + toString() + " sale has expired!");
            Util.getCategoryManager().getCategory(this).getManager().buyItem(Bukkit.getOfflinePlayer(getSeller()), this);
            Util.getExpiredItemsManager().addItem(this);
        }
    }
    private ItemStack loadDisplayItem(String time){

        ItemStack item = this.item.clone();
        ItemMeta meta = item.getItemMeta();

        List<String> newLore = new ArrayList();

        newLore.add("");
        newLore.add(ChatColor.GRAY + "Seller: " + ChatColor.GOLD + playerName);
        newLore.add("");
        newLore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + "$" + price);
        newLore.add("");
        newLore.add("" + ChatColor.GRAY + "Time left: " + ChatColor.GOLD + time);
        newLore.add("");

        List<String> lore = meta.getLore();
        if(lore != null) {
            for (String str : lore) {
                newLore.add(str);
            }
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }
    private ItemStack loadDisplayItem(){

        ItemStack item = this.item.clone();
        ItemMeta meta = item.getItemMeta();

        List<String> newLore = new ArrayList();

        newLore.add("");
        newLore.add(ChatColor.GRAY + "Seller: " + ChatColor.GOLD + playerName);
        newLore.add("");
        newLore.add(ChatColor.GRAY + "Price: " + ChatColor.GOLD + "$" + price);
        newLore.add("");
        newLore.add("" + ChatColor.GRAY + "Time left: " + ChatColor.GOLD + "2d 0h 0m");
        newLore.add("");

        List<String> lore = meta.getLore();
        if(lore != null) {
            for (String str : lore) {
                newLore.add(str);
            }
        }
        meta.setLore(newLore);
        item.setItemMeta(meta);

        return item;
    }



}
