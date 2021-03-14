package me.katnissali.cloakauctions.Managers;

import me.katnissali.cloakauctions.AuctionHouse.Item;
import me.katnissali.cloakauctions.AuctionHouse.Page;
import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpiredItemsManager {

    private HashMap<String, Inventory> playerInventories = new HashMap();

    public ExpiredItemsManager(){

    }

    public void addItem(Item item){

        Inventory inv = playerInventories.get(item.getSeller());
        if(inv == null){
            inv = getTemplate();
        }

        for(int i = 9; i <= 44; i++){
            if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)){
                inv.setItem(i, item.getDisplayItem());
                item.setSlot(i);
                break;
            }
        }

        if(playerInventories.containsKey(item.getSeller())){
            playerInventories.remove(item.getSeller());
        }

        playerInventories.put(item.getSeller(), inv);
    }
    public void removeItem(Item item){
        Util.getCategoryManager().getCategory(item).getManager().buyItem(Bukkit.getOfflinePlayer(item.getSeller()), item);
        Inventory inventory = playerInventories.get(item.getSeller());
        for(int i = item.getSlot(); i <= 44; i++){
            if(i == 44){
                inventory.clear(i);
            } else if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)){
                Item item2 = getItem(i, inventory);
                item.setSlot(i-1);
                inventory.setItem(i, inventory.getItem(i+1));

            }
        }
    }

    private Item getItem(int slot, Inventory inventory){
        ItemStack item = inventory.getItem(slot);
        for(Page page : Util.getCategoryManager().getCategory(item).getManager().getPages()){
            for(Item i : page.getItems()){
                if(i.getDisplayItem().isSimilar(item)){
                    return i;
                }
            }
        }
        return null;
    }

    public Inventory getInventory(String player){
        if(playerInventories.get(player) == null){
            playerInventories.put(player, getTemplate());
        }
        return playerInventories.get(player);
    }
    public Inventory getInventory(Player player){
        if(playerInventories.get(player.getName()) == null){
            playerInventories.put(player.getName(), getTemplate());
        }
        return playerInventories.get(player.getName());
    }

    private Inventory getTemplate(){

        //  BORDER
        Inventory inventory = Bukkit.createInventory(null, 54, "Expired Items: ");
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

        return inventory;
    }

}
