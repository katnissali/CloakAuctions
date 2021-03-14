package me.katnissali.cloakauctions.AuctionHouse;

import me.katnissali.cloakauctions.Core.Util;
import me.katnissali.cloakauctions.Managers.AuctionHouseManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Category {


    private String name;
    private List<Material> materials = new ArrayList();
    private ItemStack displayItem;
    private AuctionHouseManager manager;

    public Category(String name, ItemStack displayItem, List<String> items){
        this.name = name;
        this.displayItem = displayItem;
        for(String str : items){
            Material m;
            try{
                m = Material.valueOf(str.toUpperCase());
                materials.add(m);
            } catch(IllegalArgumentException e){
                Util.print(Util.getPrefix() + ChatColor.RED + "Invalid material in categories.yml: " + str);
                Util.print(Util.getPrefix() + ChatColor.RED + "StackTrace:");
                e.printStackTrace();
            }
        }
        manager = new AuctionHouseManager(name);
        manager.newPage();
    }

    //  GETTERS
    public AuctionHouseManager getManager(){
        return manager;
    }
    public String getName(){ return name; }
    public List<Material> getMaterials(){ return materials; }
    public boolean isType(ItemStack item){
        return materials.contains(item.getType());
    }
    public ItemStack getDisplayItem(){
        return displayItem;
    }


    //  SETTERS
    public void resetManager(){
        manager = new AuctionHouseManager(name);
        manager.newPage();
    }
}
