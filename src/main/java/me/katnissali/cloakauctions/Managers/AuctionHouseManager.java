package me.katnissali.cloakauctions.Managers;

import me.katnissali.cloakauctions.AuctionHouse.Category;
import me.katnissali.cloakauctions.AuctionHouse.Item;
import me.katnissali.cloakauctions.AuctionHouse.Page;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuctionHouseManager {

  //  private Inventory inventory;
  //  private List<Item> items = new ArrayList();

    private HashMap<Player, Page> players = new HashMap();

    private List<Page> pages = new ArrayList();
    private String categoryName;

    public AuctionHouseManager(String categoryName){
        this.categoryName = categoryName;
        updateInventories();
    }
    public Page getNextPage(Page page){
        try {
            return pages.get(page.getPageNumber());
        } catch (IndexOutOfBoundsException e){
            newPage();
            return pages.get(page.getPageNumber());
        }
    }
    public Page getNextPageDontCreate(Page page){
        try{
            return pages.get(page.getPageNumber());
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }
    public Page getPreviousPage(Page page){
        Page lastPage = pages.get(page.getPageNumber()-1);
        return lastPage;
    }

    public void updateInventories(){

        for(Page page : pages){
            if(page.isEmpty()){
                pages.remove(page);
            }
        }
        for(Page page : pages){
            Inventory inventory = page.getInventory();
            ItemStack emerald = setDisplayName((new ItemStack(Material.EMERALD_BLOCK)), ("" + ChatColor.GREEN + ChatColor.BOLD + "Next Page"));
            ItemStack redstone = setDisplayName((new ItemStack(Material.REDSTONE_BLOCK)), ("" + ChatColor.RED + ChatColor.BOLD + "Previous Page"));
            page.updateIsFull();
            if(getLastPage() != page){
                inventory.setItem(51, emerald);
            } else {
                inventory.setItem(51, inventory.getItem(45));
            }
            if(getFirstPage() != page){
                inventory.setItem(47, redstone);
            } else {
                inventory.setItem(47, inventory.getItem(45));
            }
        }
    }
    public void resetPages(){
        pages = new ArrayList();
        pages.clear();
        System.out.println("Pages = " + pages);
        newPage();
    }
    public Page getPage(int pageNumber){
        for(Page page : pages){
            if(page.getPageNumber() == pageNumber){
                return page;
            }
        }
        return null;
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
    public void buyItem(Player player, Item item){

        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("1: " + i.getItem());
            }
        }
        Page p = getPage(player);
        p.buyItem(player, item);
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("2: " + i.getItem());
            }
        }
        updateInventories();
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("3: " + i.getItem());
            }
        }
        Util.getCategoryManager().saveConfig();
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("4: " + i);
            }
        }
        Util.setup(Util.getMain());
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("5: " + i);
            }
        }
        Util.getMain().reloadConfig();
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("6: " + i);
            }
        }
        Util.getMain().loadConfig();
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("7: " + i);
            }
        }
        updateAmount();
        for(Page page : pages){
            for(Item i : page.getItems()){
                System.out.println("8: " + i);
            }
        }
    }
    public void buyItem(OfflinePlayer player, Item item){

        Page p = getPage(item);
        p.buyItem(player, item);
        updateInventories();

        Util.getCategoryManager().saveConfig();
        Util.setup(Util.getMain());
        Util.getMain().reloadConfig();
        Util.getMain().loadConfig();
        updateAmount();
    }

    public Item getItem(int slot){
        for(Page page : pages) {
            for (Item item : page.getItems()) {
                if (slot == item.getSlot()) {
                    return item;
                }
            }
        }
        return null;
    }
    public Page getFirstPage(){

        for(Page page : pages){
            return page;
        }
        return null;
    }
    public void sellItem(Player player, int price){
        Page page = getLastPage();
        if(page == null){
            newPage();
        }
        if(page.isFull()){
            newPage();
        }
        page = getLastPage();
        page.sellItem(player, price);
        updateInventories();
        updateAmount();
    }
    public void sellItem(String player, String time, ItemStack item, int price){
        Page page = getLastPage();
        if(page.isFull()){
            Util.print(Util.getPrefix() + "Page full! Creating new page.");
            newPage();
        }
        page = getLastPage();
        page.sellItem(player, time, item, price);
        updateInventories();
        updateAmount();
    }
    public void sellItem(String player, ItemStack item, int price){
        Page page = getLastPage();
        if(page.isFull()){
            Util.print(Util.getPrefix() + "Page full! Creating new page.");
            newPage();
        }
        page = getLastPage();
        page.sellItem(player, item, price);
        updateInventories();
        updateAmount();
    }
    public void newPage(){
        Page page = new Page(categoryName, pages.size() + 1);
        pages.add(page);
        updateInventories();
    }
    public Page getPage(Item item){
        for(Page page : pages){
            if(page.hasItem(item)){
                return page;
            }
        }
        return null;
    }
    public void setPlayer(Player player, Page page){
        players.remove(player);
        players.put(player, page);
    }
    public Page getPage(Player player){
        return players.get(player);
    }
    public Page getLastPage(){
        Page page = getFirstPage();
        for(Page p : pages){
            page = p;
        }
        return page;
    }
    private Page getWhichPage(ItemStack item){
        for(Page page : pages){
            if(page.hasItem(item)){
                return page;
            }
        }
        return null;
    }
    private Page getWhichPage(Item item){
        for(Page page : pages){
            if(page.hasItem(item)){
                return page;
            }
        }
        return null;
    }
    public List<Page> getPages(){
        return pages;
    }

    private void updateAmount(){
        int amount = 0;
        for(Page page : pages){
            amount = page.getItems().size();
        }
        if(amount == 0){
            amount = 1;
        }
        Util.getCategoryManager().getCategory(categoryName).getDisplayItem().setAmount(amount);
        Util.getCategoryManager().updateInventory();
    }

}
