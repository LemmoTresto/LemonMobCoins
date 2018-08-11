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

import me.max.lemonmobcoins.bukkit.hooks.PAPIHook;
import me.max.lemonmobcoins.bukkit.listeners.*;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.files.coinmob.CoinMobManager;
import me.max.lemonmobcoins.common.files.gui.GuiManager;
import me.max.lemonmobcoins.common.files.messages.MessageManager;
import me.max.lemonmobcoins.common.files.messages.Messages;
import me.max.lemonmobcoins.common.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public final class LemonMobCoinsBukkitPlugin extends JavaPlugin {

    private LemonMobCoins lemonMobCoins;
    private GuiManager guiManager;
    private PluginMessageManager pluginMessageManager;
    private PAPIHook papiHook;
    private final Logger logger = LoggerFactory.getLogger(LemonMobCoins.class);

    @Override
    public void onEnable() {
        try {
            info("Loading files and config..");
            FileUtil.saveResource("generalconfig.yml", getDataFolder().toString(), "config.yml");
            MessageManager.load(getDataFolder().toString(), getSLF4JLogger());
            info("Loaded config and files!");
        } catch (IOException e){
            error("Could not load config and files! Stopping plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        lemonMobCoins = new LemonMobCoins(getSLF4JLogger(), getDataFolder().toString());

        try {
            info("Loading listeners..");
            if (getConfig().getBoolean("bungeecord")) {
                getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
                getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessagingListener(getCoinManager(), getSLF4JLogger()));
                pluginMessageManager = new PluginMessageManager(getSLF4JLogger(), getCoinManager());
                PlayerJoinListener playerJoinListener = new PlayerJoinListener(pluginMessageManager);
                Bukkit.getPluginManager().registerEvents(playerJoinListener, this);
            }
            registerListeners(new EntityDeathListener(getCoinManager(), getCoinMobManager(), getPluginMessageManager(), papiHook), new InventoryClickListener(getCoinManager(), getGuiManager(), getPluginMessageManager(), papiHook), new PlayerPreProcessCommandListener(getCoinManager(), getGuiManager(), papiHook));
            info("Loaded listeners!");
        } catch (Exception e){
            error("Loading Listeners failed! Stopping plugin..");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiHook = new PAPIHook();
        } else {
            warn("PlaceholderAPI was not found placeholders from PlaceholderAPI will NOT work!");
        }
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
        getSLF4JLogger().error(s);
    }

    private void warn(String s){
        getSLF4JLogger().warn(s);
    }

    private void info(String s){
        getSLF4JLogger().info(s);
    }


    @NotNull
    private CoinManager getCoinManager() {
        return lemonMobCoins.getCoinManager();
    }

    @NotNull
    private GuiManager getGuiManager() {
        return guiManager;
    }

    @NotNull
    private CoinMobManager getCoinMobManager(){
        return lemonMobCoins.getCoinMobManager();
    }

    private PluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
    }

    private Logger getSLF4JLogger(){
        return logger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mobcoins") || command.getName().equalsIgnoreCase("mc") || command.getName().equalsIgnoreCase("mobc") || command.getName().equalsIgnoreCase("mcoins") || command.getName().equalsIgnoreCase("mcoin") || command.getName().equalsIgnoreCase("mobcoin")){
            if (args.length == 0) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    sender.sendMessage(Messages.OWN_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0, papiHook));
                    return true;
                }
                sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0, papiHook));
            }

            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
                if (sender.hasPermission("lemonmobcoins.admin")) {
                    sender.sendMessage(Messages.ADMIN_HELP_MENU.getMessage(0, null, null, 0, papiHook));
                    return true;
                }
                sender.sendMessage(Messages.PLAYER_HELP_MENU.getMessage(0, null, null, 0, papiHook));
                return true;
            }

            if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")){
                if (args.length == 1){
                    if (sender instanceof Player){
                        Player p = (Player) sender;
                        p.sendMessage(Messages.OWN_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0, papiHook));
                        return true;
                    }
                    sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0, papiHook));
                    return true;
                }
                if (args.length == 2){
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0, papiHook));
                        return true;
                    }
                    sender.sendMessage(Messages.OTHER_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(player.getUniqueId()), player.getName(), null, 0, papiHook));
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("shop")){
                if (sender instanceof Player){
                    ((Player) sender).openInventory(getGuiManager().getBukkitInventory());
                    return true;
                }
                sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0, papiHook));
                return true;
            }

            if (sender.hasPermission("mobcoins.admin")){
                if (args.length != 3) {
                    if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")){
                        sender.sendMessage(Messages.valueOf("INVALID_USAGE_" + args[0].toUpperCase() + "_COMMAND").getMessage(0, null, null, 0, papiHook));
                        return true;
                    }

                    if (args.length != 2){
                        if (args[0].equalsIgnoreCase("reset")) {
                            sender.sendMessage(Messages.INVALID_USAGE_RESET_COMMAND.getMessage(0, null, null, 0, papiHook));
                        }

                        if (args[0].equalsIgnoreCase("reload")){
                            try {
                                sender.sendMessage(Messages.START_RELOAD.getMessage(0, null, null, 0, papiHook));
                                onDisable();
                                onEnable();
                                sender.sendMessage(Messages.SUCCESSFUL_RELOAD.getMessage(0, null, null, 0, papiHook));
                                return true;
                            } catch (Exception e){
                                sender.sendMessage(Messages.FAILED_RELOAD.getMessage(0, null, null, 0, papiHook));
                                e.printStackTrace();
                                return true;
                            }
                        }

                        sender.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(0, null, null, 0, papiHook));
                        return true;
                    }
                }

                OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

                if (player == null) {
                    sender.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0, papiHook));
                    return true;
                }

                if (args[0].equalsIgnoreCase("reset")){
                    getCoinManager().setCoinsOfPlayer(player.getUniqueId(), 0);
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId());
                    sender.sendMessage(Messages.RESET_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(player.getUniqueId()), player.getName(), null, 0, papiHook));
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")){
                    getCoinManager().setCoinsOfPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId());
                    sender.sendMessage(Messages.SET_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(player.getUniqueId()), player.getName(), null, 0, papiHook));
                    return true;
                }

                if (args[0].equalsIgnoreCase("take")){
                    getCoinManager().deductCoinsFromPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId());
                    sender.sendMessage(Messages.TAKE_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(player.getUniqueId()), player.getName(), null, Integer.valueOf(args[2]), papiHook));
                    return true;
                }

                if (args[0].equalsIgnoreCase("give")) {
                    getCoinManager().addCoinsToPlayer(player.getUniqueId(), Double.parseDouble(args[2]));
                    if (getPluginMessageManager() != null) getPluginMessageManager().sendPluginMessage(player.getUniqueId());
                    sender.sendMessage(Messages.GIVE_PLAYER_BALANCE.getMessage(getCoinManager().getCoinsOfPlayer(player.getUniqueId()), player.getName(), null, Integer.valueOf(args[2]), papiHook));
                    return true;
                }
            }

            sender.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(0, null, null, 0, papiHook));
            return true;
        }

        if (command.getName().equalsIgnoreCase("mshop")){
            if (sender instanceof Player){
                Player p = (Player) sender;
                if (p.hasPermission("lemonmobcoins.shop")){
                    p.openInventory(getGuiManager().getBukkitInventory());
                    return true;
                }
                p.sendMessage(Messages.NO_PERMISSION_TO_EXECUTE.getMessage(0, p.getName(), null, 0, papiHook));
                return true;
            }
            sender.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0, papiHook));
            return true;
        }
        return false;
    }
}
