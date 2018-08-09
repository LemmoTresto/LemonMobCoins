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

package me.max.lemonmobcoins.bukkit;

import me.max.lemonmobcoins.bukkit.gui.GuiManager;
import me.max.lemonmobcoins.bukkit.listeners.*;
import me.max.lemonmobcoins.bukkit.messages.MessageManager;
import me.max.lemonmobcoins.bukkit.messages.Messages;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.data.DataProvider;
import me.max.lemonmobcoins.common.data.providers.MySqlProvider;
import me.max.lemonmobcoins.common.data.providers.YamlBukkitProvider;
import me.max.lemonmobcoins.common.exceptions.DataLoadException;
import me.max.lemonmobcoins.common.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

public final class LemonMobCoinsBukkitPlugin extends JavaPlugin {

    private LemonMobCoins lemonMobCoins;
    private GuiManager guiManager;
    private PluginMessageManager pluginMessageManager;

    @Override
    public void onEnable() {
        try {
            info("Loading messages and config..");
            FileUtil.saveResource("bukkitconfig.yml", getDataFolder(), "config.yml");
            MessageManager.load(getDataFolder(), getLogger());
            info("Loaded config and messages!");
        } catch (Exception e){
            error("Could not load config and messages! Stopping plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        try {
            String storageType = getConfig().getString("storage.type");
            DataProvider dataProvider;
            if (storageType.equalsIgnoreCase("flatfile")) dataProvider = new YamlBukkitProvider(getDataFolder());
            else if (storageType.equalsIgnoreCase("mysql")){
                ConfigurationSection mysqlSection = getConfig().getConfigurationSection("storage.mysql");
                dataProvider = new MySqlProvider(mysqlSection.getString("hostname"), mysqlSection.getString("port"), mysqlSection.getString("username"), mysqlSection.getString("password"), mysqlSection.getString("database"));
            } else {
                error("Invalid storage type found! Using flatfile!");
                dataProvider = new YamlBukkitProvider(getDataFolder());
            }
            lemonMobCoins = new LemonMobCoins(dataProvider, getLogger());
            guiManager = new GuiManager(getConfig());
        } catch (SQLException e){
            error("Failed loading MySql! Stopping plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        } catch (DataLoadException e){
            error("Failed loading data! Stopping plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        try {
            info("Loading listeners..");
            if (getConfig().getBoolean("bungeecord")) {
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessagingListener(getCoinManager(), getLogger()));
                PlayerJoinListener playerJoinListener = new PlayerJoinListener();
                pluginMessageManager = new PluginMessageManager(playerJoinListener);
                Bukkit.getPluginManager().registerEvents(playerJoinListener, this);
            }
            registerListeners(new EntityDeathListener(getCoinManager(), getConfig().getConfigurationSection("mob-list"), getPluginMessageManager()), new InventoryClickListener(getCoinManager(), getGuiManager(), getPluginMessageManager()), new PlayerPreProcessCommandListener(getCoinManager(), getGuiManager()));
            info("Loaded listeners!");
        } catch (Exception e){
            error("Loading Listeners failed! Stopping plugin..");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) warn("PlaceholderAPI was not found placeholders from this plugin will NOT work!");
    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("bungeecord")) return;
        try {
            info("Saving data..");
            lemonMobCoins.disable();
            info("Saved data!");
        } catch (IOException | SQLException e) {
            error("Failed saving data! Retrying..");
            try {
                lemonMobCoins.disable();
                info("Saved data!");
            } catch (IOException | SQLException e1){
                error("Failed saving data again! Data will be lost ;(");
                e.printStackTrace();
            }
        }
        info("Disabled successfully!");
    }

    private void registerListeners(Listener... listeners){
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void error(String s){
        log(Level.SEVERE, s);
    }

    private void warn(String s){
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
        return lemonMobCoins.getCoinManager();
    }

    @NotNull
    private GuiManager getGuiManager() {
        return guiManager;
    }

    private PluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
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
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")){
                        sender.sendMessage(Messages.valueOf("INVALID_USAGE_" + args[0].toUpperCase() + "_COMMAND").getMessage(getCoinManager(), null, null, 0));
                        return true;
                    }

                    if (args.length != 2){
                        if (args[0].equalsIgnoreCase("reset")) {
                            sender.sendMessage(Messages.INVALID_USAGE_RESET_COMMAND.getMessage(getCoinManager(), null, null, 0));
                        }

                        if (args[0].equalsIgnoreCase("reload")){
                            try {
                                sender.sendMessage(Messages.START_RELOAD.getMessage(getCoinManager(), null, null, 0));
                                onDisable();
                                onEnable();
                                sender.sendMessage(Messages.SUCCESSFUL_RELOAD.getMessage(getCoinManager(), null, null, 0));
                                return true;
                            } catch (Exception e){
                                sender.sendMessage(Messages.FAILED_RELOAD.getMessage(getCoinManager(), null, null, 0));
                                return true;
                            }
                        }

                        sender.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(getCoinManager(), null, null, 0));
                        return true;
                    }
                }

                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                if (player == null) {
                    sender.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(getCoinManager(), null, null, 0));
                    return true;
                }

                if (args[0].equalsIgnoreCase("reset")){
                    getCoinManager().setCoinsOfPlayer(player.getUniqueId(), 0);
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId(), getCoinManager().getCoinsOfPlayer(player.getUniqueId()));
                    sender.sendMessage(Messages.RESET_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, 0));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")){
                    getCoinManager().setCoinsOfPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId(), getCoinManager().getCoinsOfPlayer(player.getUniqueId()));
                    sender.sendMessage(Messages.SET_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, 0));
                    return true;
                }

                if (args[0].equalsIgnoreCase("take")){
                    getCoinManager().deductCoinsFromPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId(), getCoinManager().getCoinsOfPlayer(player.getUniqueId()));
                    sender.sendMessage(Messages.TAKE_PLAYER_BALANCE.getMessage(getCoinManager(), player, null, Integer.valueOf(args[2])));
                    return true;
                }

                if (args[0].equalsIgnoreCase("give")) {
                    getCoinManager().addCoinsToPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId(), getCoinManager().getCoinsOfPlayer(player.getUniqueId()));
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
