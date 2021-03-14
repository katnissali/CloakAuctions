package me.katnissali.cloakauctions.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompleterListener implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(args.length == 1){
            List<String> list = new ArrayList();

            if(sender.hasPermission("CloakAuction.help")){
                list.add("help");
            }
            if(sender.hasPermission("CloakAuction.sell")){
                list.add("sell");
            }
            if(sender.hasPermission("CloakAuction.reload")){
                list.add("reload");
            }
            if(sender.hasPermission("CloakAuction.clear")){
                list.add("clear");
            }

            return list.stream().filter(a -> a.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());

        }

        return null;

    }
}
