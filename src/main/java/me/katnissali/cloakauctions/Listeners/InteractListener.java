package me.katnissali.cloakauctions.Listeners;

import me.katnissali.cloakauctions.AuctionHouse.Category;
import me.katnissali.cloakauctions.AuctionHouse.Item;
import me.katnissali.cloakauctions.AuctionHouse.Page;
import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(InventoryClickEvent e){

        if(e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            if (ChatColor.stripColor(player.getOpenInventory().getTitle()).equalsIgnoreCase("Auction House")) {
                e.setCancelled(true);

                if (e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
                    if (e.getSlot() > 8 && e.getSlot() < 45) {
                        if (Util.getCategoryManager().getCategory(e.getCurrentItem()) != null) {

                            Category category = Util.getCategoryManager().getCategory(e.getCurrentItem());
                            player.openInventory(category.getManager().getFirstPage().getInventory());
                            category.getManager().setPlayer(player, category.getManager().getFirstPage());

                        } else {
                            player.sendMessage(ChatColor.RED + "Error! Contact administration, this is not supposed to happen.");
                        }
                    } else {
                        if(e.getCurrentItem().getType().equals(Material.GLASS_BOTTLE)){
                            player.openInventory(Util.getExpiredItemsManager().getInventory(player));
                        }
                    }
                }
                //  TODO add permissions
            } else if (ChatColor.stripColor(player.getOpenInventory().getTitle()).contains("Category: ")) {
                e.setCancelled(true);
                Category category = Util.getCategoryManager().getCategory(player.getOpenInventory().getTitle().split(" ")[1]);

                if(e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR)) {
                    if (e.getSlot() >= 9 && e.getSlot() <= 44) {
                        player.closeInventory();

                        Item item = category.getManager().getItem(e.getSlot());
                        if(item != null) {
                            if (item.canBuy(player)) {
                                boolean isFull = true;
                                for(ItemStack i : player.getInventory().getContents()){
                                    if(i == null || i.getType().equals(Material.AIR)){
                                        isFull = false;
                                        break;
                                    }
                                }
                                if(!isFull) {
                                    player.getInventory().addItem(item.getItem());
                                    category.getManager().buyItem(player, item);
                                    player.getInventory().remove(player.getItemInHand());

                                    if (!player.getName().equals(item.getSeller())) {
                                        player.sendMessage(Util.getPrefix() + "You bought an item from " + item.getSeller() + " for " + ChatColor.GREEN + "$" + item.getPrice() + ChatColor.GRAY + ".");
                                    } else {
                                        player.sendMessage(Util.getPrefix() + "You removed your item from the Auction House.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "Please open a slot in your inventory.");
                                }

                            } else {
                                player.sendMessage(Util.getPrefix() + "You do not have enough money to buy this item.");
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "Error! Contact administration, this is not supposed to happen.");
                        }
                    } else {

                        Page currentPage = category.getManager().getPage(player);

                        switch(e.getCurrentItem().getType()){

                            case REDSTONE_BLOCK:
                                Page previousPage = category.getManager().getPage(currentPage.getPageNumber()-1);

                                category.getManager().setPlayer(player, previousPage);

                                player.openInventory(previousPage.getInventory());
                                break;
                            case EMERALD_BLOCK:
                                Page nextPage = category.getManager().getNextPage(currentPage);
                                player.openInventory(nextPage.getInventory());
                                category.getManager().setPlayer(player, nextPage);
                                break;
                            default:
                                break;
                        }
                    }
                }

            } else if (ChatColor.stripColor(player.getOpenInventory().getTitle()).contains("Expired Items: ")) {

            }
        }

    }

}
