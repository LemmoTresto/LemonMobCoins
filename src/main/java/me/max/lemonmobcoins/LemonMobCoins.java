/*
 *
 *  *
 *  *  * MobCoins - Earn coins for killing mobs.
 *  *  * Copyright (C) 2018 Max Berkelmans AKA LemmoTresto
 *  *  *
 *  *  * This program is free software: you can redistribute it and/or modify
 *  *  * it under the terms of the GNU General Public License as published by
 *  *  * the Free Software Foundation, either version 3 of the License, or
 *  *  * (at your option) any later version.
 *  *  *
 *  *  * This program is distributed in the hope that it will be useful,
 *  *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  *  * GNU General Public License for more details.
 *  *  *
 *  *  * You should have received a copy of the GNU General Public License
 *  *  * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *  *
 *
 */

package me.max.lemonmobcoins;

import me.max.lemonmobcoins.api.LemonMobCoinsAPI;
import me.max.lemonmobcoins.coins.CoinManager;
import me.max.lemonmobcoins.files.MessageManager;
import me.max.lemonmobcoins.files.Messages;
import me.max.lemonmobcoins.gui.GuiManager;
import me.max.lemonmobcoins.listeners.EntityDeathListener;
import me.max.lemonmobcoins.listeners.InventoryClickListener;
import me.max.lemonmobcoins.listeners.PlayerPreProcessCommandListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public final class LemonMobCoins extends JavaPlugin {

    private CoinManager coinManager;
    private GuiManager guiManager;
    private static LemonMobCoinsAPI lemonMobCoinsAPI;

    @Override
    public void onEnable() {
        try {
            info("Loading data..");
            saveDefaultConfig();
            coinManager = new CoinManager(getDataFolder(), getConfig());
            new MessageManager(getDataFolder(), getLogger(), this.getClassLoader());
            guiManager = new GuiManager(getConfig());
            info("Loaded data!");
        } catch (IOException e) {
            error("Loading data failed! Stopping plugin..");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            info("Loading listeners..");
            registerListeners(new EntityDeathListener(getCoinManager()), new InventoryClickListener(getCoinManager(), getGuiManager()), new PlayerPreProcessCommandListener(getCoinManager(), getGuiManager()));
            info("Loaded listeners!");
        } catch (Exception e){
            error("Loading Listeners failed! Stopping plugin..");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            info("Loading API..");
            lemonMobCoinsAPI = coinManager;
            info("Loaded API!");
        } catch (Exception e){
            error("Loading API failed! Stopping plugin..");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            info("Saving data..");
            getCoinManager().saveData();
            info("Saved data!");
            info("Disabled successfully!");
        } catch (IOException e) {
            error("Could not save data! Retrying..");
            try {
                getCoinManager().saveData();
                info("Saved data!");
                info("Disabled successfully!");
            } catch (IOException e1) {
                error("Still couldn't save data! Data will be lost ;(");
                e1.printStackTrace();
            }
        }
    }

    private void registerListeners(Listener... listeners){
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void error(String s){
        log(Level.SEVERE, s);
    }

    public void warn(String s){
        log(Level.WARNING, s);
    }

    private void info(String s){
        log(Level.INFO, s);
    }

    private void log(Level level, String s){
        getLogger().log(level, s);
    }

    @NotNull
    private CoinManager getCoinManager() {
        return coinManager;
    }

    @NotNull
    private GuiManager getGuiManager() {
        return guiManager;
    }

    @SuppressWarnings("unused")
    @NotNull
    public static LemonMobCoinsAPI getLemonMobCoinsAPI(){
        return lemonMobCoinsAPI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mobcoins") || command.getName().equalsIgnoreCase("mc") || command.getName().equalsIgnoreCase("mobc") || command.getName().equalsIgnoreCase("mcoins") || command.getName().equalsIgnoreCase("mcoin") || command.getName().equalsIgnoreCase("mobcoin")){
            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage(Messages.OWN_PLAYER_BALANCE.getMessage(getCoinManager(), (Player) sender, null, 0));
                    return true;
                }
                sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(getCoinManager(), null, null, 0));
            }

            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
                if (sender.hasPermission("lemonmobcoins.admin")) {
                    sender.sendMessage(Messages.ADMIN_HELP_MENU.getMessage(getCoinManager(), null, null, 0));
                    return true;
                }
                sender.sendMessage(Messages.PLAYER_HELP_MENU.getMessage(getCoinManager(), null, null, 0));
                return true;
            }

            if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")){
                if (args.length == 1){
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        p.sendMessage(Messages.OWN_PLAYER_BALANCE.getMessage(getCoinManager(), p, null, 0));
                        return true;
                    }
                    sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(getCoinManager(), null, null, 0));
                    return true;
                }
                if (args.length == 2){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(getCoinManager(), null, null, 0));
                        return true;
                    }
                    sender.sendMessage(Messages.OTHER_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, 0));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("shop")){
                if (sender instanceof Player){
                    ((Player) sender).openInventory(getGuiManager().getInventory());
                    return true;
                }
                sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(getCoinManager(), null, null, 0));
                return true;
            }

            if (sender.hasPermission("mobcoins.admin")){
                if (args.length != 3) {
                    sender.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(getCoinManager(), null, null, 0));
                    return true;
                }
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(getCoinManager(), null, null, 0));
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")){
                    getCoinManager().setCoinsOfPlayer(player, Double.parseDouble(args[2]));
                    sender.sendMessage(Messages.SET_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, 0));
                    return true;
                }
                if (args[0].equalsIgnoreCase("take")){
                    getCoinManager().deductCoinsFromPlayer(player, Double.parseDouble(args[2]));
                    sender.sendMessage(Messages.TAKE_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, Integer.valueOf(args[2])));
                    return true;
                }
                if (args[0].equalsIgnoreCase("give")){
                    getCoinManager().addCoinsToPlayer(player, Double.parseDouble(args[2]));
                    sender.sendMessage(Messages.GIVE_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, Integer.valueOf(args[2])));
                    return true;
                }
            }

            sender.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(getCoinManager(), null, null, 0));
            return true;
        }

        if (command.getName().equalsIgnoreCase("mshop")){
            if (sender instanceof Player){
                Player p = (Player) sender;
                if (p.hasPermission("lemonmobcoins.shop")){
                    p.openInventory(getGuiManager().getInventory());
                    return true;
                }
                p.sendMessage(Messages.NO_PERMISSION_TO_EXECUTE.getMessage(getCoinManager(), p, null, 0));
                return true;
            }
            sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(getCoinManager(), null, null, 0));
            return true;
        }
        return false;
    }
}
