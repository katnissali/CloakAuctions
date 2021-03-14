package me.katnissali.cloakauctions.Core;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class Dependencies {

    private static Economy economy;

    public static boolean setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Util.getMain().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Util.print(Util.getPrefix() + ChatColor.RED + "Error hooking into vault. (ERROR 1)");
            return false;
        }
        economy = rsp.getProvider();
        if(economy == null){
            Util.print(Util.getPrefix() + "Error hooking into vault. (ERROR 2)");
            return false;
        } else {
            Util.print(Util.getPrefix() + "Succcessfully hooked into vault.");
            return true;
        }
    }




    public static Economy getEconomy(){
        return economy;
    }

}
