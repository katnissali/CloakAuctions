package me.katnissali.cloakauctions;

import me.katnissali.cloakauctions.AuctionHouse.Category;
import me.katnissali.cloakauctions.AuctionHouse.Item;
import me.katnissali.cloakauctions.AuctionHouse.Page;
import me.katnissali.cloakauctions.Commands.Ah;
import me.katnissali.cloakauctions.Core.Dependencies;
import me.katnissali.cloakauctions.Core.Util;
import me.katnissali.cloakauctions.Listeners.InteractListener;
import me.katnissali.cloakauctions.Listeners.TabCompleterListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CloakAuctions extends JavaPlugin {

    @Override
    public void onEnable() {

        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();
        Util.print(/*Util.getPrefix() + */"Loading CloakAuctions...");
        loadCore();
        registerEvents();
        registerCommands();

        Util.print(Util.getPrefix() + "Loading config files");

        loadConfig();

        Util.print(Util.getPrefix() + "Configs loaded.");

        Util.print(Util.getPrefix() + "CloakAuctions loaded.");

        //  EVERY MINUTE
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable(){
            public void run(){

                for(Category category : Util.getCategoryManager().getCategories()){
                    for(Page page : category.getManager().getPages()){
                        for(Item item : page.getItems()){
                            item.updateTime();
                        }
                    }
                }


            }
        }, 20L, 1200);

    }

    public void loadConfig(){

        try {
            for (ItemStack item : (List<ItemStack>) Util.getConfigManager().getItemConfig().getList("Items")) {

                System.out.println("item = " + item);
                ItemMeta meta = item.getItemMeta();
                List<String> oldLore = meta.getLore();
                List<String> newLore = new ArrayList();
                int i = 1;
                for (String str : oldLore) {
                    if (i > 6) {
                        newLore.add(ChatColor.translateAlternateColorCodes('&', str));
                    }
                    i++;
                }


                meta.setLore(newLore);
                item.setItemMeta(meta);


                String playerName = ChatColor.stripColor(oldLore.get(0));
                playerName = playerName.substring(playerName.indexOf(" ") + 1);
                String price = ChatColor.stripColor(oldLore.get(3));
                price = price.substring(price.indexOf("$") + 1);
                String time = ChatColor.stripColor(oldLore.get(5));
                time = time.substring(time.indexOf(": ") + 2);

                Util.getCategoryManager().sellItem(playerName, time, item, Integer.valueOf(price));
            }
        } catch (NullPointerException e){

        }

        for(Category category : Util.getCategoryManager().getCategories()) {
            for (Page page : category.getManager().getPages()) {
                for (Item i : page.getItems()) {
                    System.out.println("1: " + i.getItem().getType());
                }
            }
        }
    }
    private void loadCore(){
        Util.setup(this);
        Dependencies.setup();
    }
    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
    }
    private void registerCommands(){
        getCommand("ah").setExecutor(new Ah());
        getCommand("ah").setTabCompleter(new TabCompleterListener());
    }

    @Override
    public void onDisable() {
        Util.print(Util.getPrefix() + "Saving configs...");

        Util.getCategoryManager().saveConfig();
        Util.print(Util.getPrefix() + "Configs saved!");



    }
}
