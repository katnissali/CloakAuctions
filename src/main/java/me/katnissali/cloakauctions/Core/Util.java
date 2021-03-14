package me.katnissali.cloakauctions.Core;

import me.katnissali.cloakauctions.AuctionHouse.Category;
import me.katnissali.cloakauctions.Managers.AuctionHouseManager;
import me.katnissali.cloakauctions.CloakAuctions;
import me.katnissali.cloakauctions.Managers.CategoryManager;
import me.katnissali.cloakauctions.Managers.ConfigManager;
import me.katnissali.cloakauctions.Managers.ExpiredItemsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Util {
    private static CloakAuctions main;
   // private static AuctionHouseManager ahManager;
    private static ConfigManager configManager;
    private static CategoryManager categoryManager;
    private static ExpiredItemsManager expiredItemsManager;

    public static void setup(CloakAuctions ca){
        main = ca;
        configManager = new ConfigManager();
        categoryManager = new CategoryManager();
        expiredItemsManager = new ExpiredItemsManager();
    }

    public static CategoryManager getCategoryManager(){
        return categoryManager;
    }
    public static ExpiredItemsManager getExpiredItemsManager(){ return expiredItemsManager; }
    public static ConfigManager getConfigManager(){
        return configManager;
        //return null;
    }
    public static void testPrint(String msg){
        Util.print(msg);
    }
    public static void resetCategoryManager(){
        categoryManager = new CategoryManager();
    }
    public static void sendCommand(String str){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
    }
    public static void onlyPlayers(){
        print(ChatColor.RED + "Only players can do this.");
    }
    public static CloakAuctions getMain(){
        return main;
    }
    public static void print(String str){
        Bukkit.getServer().getConsoleSender().sendMessage(str);
    }
    public static String getPrefix(){
       // return ("[" + ChatColor.RED + ChatColor.BOLD + "CloakAuctions" + ChatColor.RESET + "] " + ChatColor.GRAY);
        //System.out.println("config = " + main.getConfig());
        //System.out.println("msg = " + main.getConfig().getStringList("Messages.prefix"));;
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.prefix"));
    }
    public static String coloredConfigString(String path){
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString(path));
    }
    public static void noPermission(Player player){
        player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
    }
}
