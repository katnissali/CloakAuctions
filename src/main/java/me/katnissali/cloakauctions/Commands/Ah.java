package me.katnissali.cloakauctions.Commands;

import me.katnissali.cloakauctions.Core.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ah implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;


            if(args.length == 0){
                if(player.hasPermission("CloackAuctions.open-gui")) {


                    if(!(player.getLocation().getX() <= Util.getMain().getConfig().getInt("Region.x-max")
                            && player.getLocation().getX() >= Util.getMain().getConfig().getInt("Region.x-min")
                            && player.getLocation().getZ() <= Util.getMain().getConfig().getInt("Region.z-max")
                            && player.getLocation().getZ() >= Util.getMain().getConfig().getInt("Region.z-min"))){

                        String message = Util.getMain().getConfig().getString("Messages.not-at-spawn");
                        System.out.println("message = " + message);
                        message = message.replace("%prefix%", Util.getPrefix());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        return false;
                    }

                    player.openInventory(Util.getCategoryManager().getInventory());
                } else {
                    Util.noPermission(player);
                }
            } else if (args.length == 2) {

                if (args[0].equalsIgnoreCase("sell")) {
                    if (player.hasPermission("CloakAuctions.sell")) {

                        if (player.getInventory().getItemInHand() != null && !player.getInventory().getItemInHand().getType().equals(Material.AIR)) {
                            int price;
                            try {
                                price = Integer.valueOf(args[1]);
                                if (price >= Util.getMain().getConfig().getInt("Restrictions.min-price")
                                        && price <= Util.getMain().getConfig().getInt("Restrictions.max-price")) {

                                    Util.getCategoryManager().sellItem(player.getName(), player.getItemInHand().clone(), price);
                                    String message = Util.getMain().getConfig().getString("Messages.item-sold");
                                    message = message.replace("%price%", String.valueOf(price));
                                    message = message.replace("%prefix%", Util.getPrefix());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                    System.out.println("held item = " + player.getInventory().getItemInHand().getType());
                                    player.getInventory().getItem(player.getInventory().getHeldItemSlot()).setAmount(0);

                                } else {
                                    String message = Util.getMain().getConfig().getString("Messages.price-restriction-error");
                                    message = message.replace("%min-price%", String.valueOf(Util.getMain().getConfig().getInt("Restrictions.min-price")));
                                    message = message.replace("%max-price%", String.valueOf(Util.getMain().getConfig().getInt("Restrictions.max-price")));
                                    message = message.replace("%prefix%", Util.getPrefix());
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                                   // player.sendMessage(ChatColor.RED + "The price must be between " + Util.getMain().getConfig().getInt("Restrictions.min-price") + " and " + Util.getMain().getConfig().getInt("Restrictions.max-price") + ".");
                                }
                            } catch (NumberFormatException e) {
                                /*
                                invalid-price: '&cThat is an invalid price.'
  no-item-in-hand: '&cYou must have an item in your hand.' # sent if player tries to sell air
                                 */
                                String message = Util.getMain().getConfig().getString("Messages.invalid-price");
                                message = message.replace("%prefix%", Util.getPrefix());
                                System.out.println("prefix = " + Util.getPrefix());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            }
                        } else {
                            String message = Util.getMain().getConfig().getString("Messages.no-item-in-hand");
                            message = message.replace("%prefix%", Util.getPrefix());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                            //player.sendMessage(ChatColor.RED + "You must have an item in your hand.");
                        }
                    } else {
                        Util.noPermission(player);
                    }
                }
            } else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("help")) {
                    if (player.hasPermission("CloakAuctions.help")) {
                        sendHelpMessage(sender);
                    } else {
                        Util.noPermission(player);
                    }
                } else if (args[0].equalsIgnoreCase("clear")) {
                    if (player.hasPermission("CloackAuctions.clear")) {
                        player.sendMessage(Util.getPrefix() + "Clearing...");

                        Util.getCategoryManager().resetConfig();
                        Util.setup(Util.getMain());
                        Util.getMain().reloadConfig();
                        Util.getMain().loadConfig();

                        player.sendMessage(Util.getPrefix() + "Successfully cleared.");
                    } else {
                        Util.noPermission(player);
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (player.hasPermission("CloackAuctions.reload")) {
                        player.sendMessage(Util.getPrefix() + "Reloading...");

                        Util.getCategoryManager().saveConfig();
                        Util.setup(Util.getMain());
                        Util.getMain().reloadConfig();
                        Util.getMain().loadConfig();

                        player.sendMessage(Util.getPrefix() + "Successfully reloaded...");
                    } else {
                        Util.noPermission(player);
                    }
                } else {
                    wrongUsage(player);
                }
            } else {
                wrongUsage(player);
            }


        } else {
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("sell")){
                    Util.onlyPlayers();
                } else {
                    wrongUsage();
                }
            } else if(args.length == 1){
                if(args[0].equalsIgnoreCase("help")){
                    sendHelpMessage(sender);
                } else if(args[0].equalsIgnoreCase("clear")){
                    Util.print(Util.getPrefix() + "Clearing...");

                    Util.getCategoryManager().resetConfig();
                    Util.setup(Util.getMain());
                    Util.getMain().reloadConfig();
                    Util.getMain().loadConfig();

                    Util.print(Util.getPrefix() + "Successfully cleared.");
                } else if(args[0].equalsIgnoreCase("reload")){
                    Util.print(Util.getPrefix() + "Reloading...");

                    Util.getCategoryManager().saveConfig();
                    Util.setup(Util.getMain());
                    Util.getMain().reloadConfig();
                    Util.getMain().loadConfig();

                    Util.print(Util.getPrefix() + "Successfully reloaded...");
                } else {
                    Util.onlyPlayers();
                }
            } else {
                wrongUsage();
            }
        }
        return false;
    }

    private void wrongUsage(Player player){
        player.sendMessage(ChatColor.RED + "Invalid command. Use /ah help for more information.");
    }
    private void wrongUsage(){
        Util.print(ChatColor.RED + "Invalid command. Use /ah help for more information.");
    }

    private void sendHelpMessage(CommandSender sender){
        if(sender instanceof Player){
            Player player = (Player) sender;

            player.sendMessage("");
            player.sendMessage("" +  ChatColor.WHITE + ChatColor.UNDERLINE + "___________________" + ChatColor.WHITE + " [" + ChatColor.RED + ChatColor.BOLD + "MagicRods Help" + ChatColor.RESET + "] " + ChatColor.UNDERLINE + "___________________");
            player.sendMessage("");

            player.sendMessage(ChatColor.GREEN + "CloackAuctions commands and permissions:");
            sendCommandMsg(player, "ah", "Open gui.", "CloackAuctions.open-gui");
            sendCommandMsg(player, "ah help", "This page.", "CloackAuctions.help");
            sendCommandMsg(player, "ah reload", "Reload config.", "CloackAuctions.reload");
            sendCommandMsg(player, "ah clear", "Clear all items in auction house.", "CloackAuctions.clear");
            sendCommandMsg(player, "ah sell <price>", "Sell item in hand.", "CloackAuctions.sell");

            player.sendMessage("" + ChatColor.WHITE + ChatColor.UNDERLINE + "_______________________________________________________");

        } else {
            Util.print("");
            Util.print("" +  ChatColor.DARK_GRAY + ChatColor.UNDERLINE + "___________________" + ChatColor.DARK_GRAY + " [" + ChatColor.RED + ChatColor.BOLD + "MagicRods Help" + ChatColor.DARK_GRAY + "] ___________________");
            Util.print("");

            Util.print(ChatColor.GREEN + "CloackAuctions commands and permissions:");
            sendCommandMsg("ah help", "This page.", "CloackAuctions.help");
            sendCommandMsg("ah reload", "Reload config.", "CloackAuctions.reload");
            sendCommandMsg("ah clear", "Clear all items in auction house.", "CloackAuctions.clear");
            sendCommandMsg("ah sell <price>", "Sell item in hand.", "CloackAuctions.sell");

            Util.print("" +  ChatColor.DARK_GRAY + ChatColor.UNDERLINE + "_______________________________________________________");

        }
    }

    private static void sendCommandMsg(Player player, String command, String description, String permission) {
        player.sendMessage("" + ChatColor.GOLD + "     - " + ChatColor.GRAY + "/" + command);
        if(!description.equalsIgnoreCase("")) {
            player.sendMessage("" + ChatColor.GRAY + "       " + description);
        }
        player.sendMessage("" + ChatColor.GRAY + "       Permission: " + permission);
    }
    private static void sendCommandMsg(String command, String description, String permission) {
        Util.print("" + ChatColor.DARK_GRAY + "     - " + ChatColor.GRAY + "/" + command);
        if(!description.equalsIgnoreCase("")) {
            Util.print("" + ChatColor.GRAY + "       " + description);
        }
        Util.print("" + ChatColor.GRAY + "       Permission: " + permission);
    }
}
