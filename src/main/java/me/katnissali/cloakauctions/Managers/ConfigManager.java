package me.katnissali.cloakauctions.Managers;

import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {

    private YamlConfiguration itemsConfigYaml;
    private File itemsConfigFile;
    private YamlConfiguration categoriesConfigYaml;
    private File categoriesConfigFile;

    public ConfigManager(){
        loadItemsConfig();
        loadCategoriesConfig();
    }

    private File loadItemsConfig() {
        String fileName = "items.yml";
        Util.print(Util.getPrefix() + "Loading file: " + fileName);

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("CloakAuctions").getDataFolder(), fileName);
        itemsConfigFile = file;

        boolean newFile = false;

        if(!file.exists()){
            try {
                file.createNewFile();
                newFile = true;
            } catch (IOException e) {
                Util.print(Util.getPrefix() + ChatColor.RED + "Error loading " + fileName + "!");
                e.printStackTrace();
            }
        }
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
        if (newFile) {
            yamlConfig.createSection("Items");
            Util.print(Util.getPrefix() + "Loading default items.yml config.");
        }
        try {
            yamlConfig.save(file);
        } catch (IOException e){
            Util.print(Util.getPrefix() + ChatColor.RED + "Error saving " + fileName + "!");
            e.printStackTrace();
            return null;
        }

        Util.print(Util.getPrefix() + "File loaded.");
        itemsConfigYaml = yamlConfig;
        return file;
    }
    private File loadCategoriesConfig() {
        String fileName = "categories.yml";
        Util.print(Util.getPrefix() + "Loading file: " + fileName);

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("CloakAuctions").getDataFolder(), fileName);
        categoriesConfigFile = file;

        boolean newFile = false;

        if(!file.exists()){
            try {
                file.createNewFile();
                newFile = true;
            } catch (IOException e) {
                Util.print(Util.getPrefix() + ChatColor.RED + "Error loading " + fileName + "!");
                e.printStackTrace();
            }
        }
        YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
        if (newFile) {
            yamlConfig.createSection("Categories");

            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = sword.getItemMeta();
            meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Default Category"));
            meta.setDisplayName("" + ChatColor.LIGHT_PURPLE + "Default Category");
            sword.setItemMeta(meta);

            yamlConfig.set(("Categories.default.display-item"), sword);
            List<String> list = new ArrayList();
            list.add("DIAMOND_SWORD");
            list.add("DIAMOND_AXE");
            list.add("DIAMOND_PICKAXE");
            yamlConfig.set(("Categories.default.items"), list);

            Util.print(Util.getPrefix() + "Loading default categories.yml config.");
        }
        try {
            yamlConfig.save(file);
        } catch (IOException e){
            Util.print(Util.getPrefix() + ChatColor.RED + "Error saving " + fileName + "!");
            e.printStackTrace();
            return null;
        }

        Util.print(Util.getPrefix() + "File loaded.");
        categoriesConfigYaml = yamlConfig;
        return file;
    }
    public void saveFiles(){
        try {
            itemsConfigYaml.save(itemsConfigFile);
        } catch(IOException e){
            Util.print(Util.getPrefix() + "Error! Cannot save pages.yml!");
            e.printStackTrace();
        }
    }

    public YamlConfiguration getItemConfig(){ return itemsConfigYaml; }
    public YamlConfiguration getCategoriesConfig(){ return categoriesConfigYaml; }

}
