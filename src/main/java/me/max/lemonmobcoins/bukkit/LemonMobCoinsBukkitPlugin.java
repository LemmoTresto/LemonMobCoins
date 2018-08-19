/*
 *
 *  *
 *  *  * LemonMobCoins - Kill mobs and get coins that can be used to buy awesome things
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

import co.aikar.commands.BukkitCommandManager;
import me.max.lemonmobcoins.bukkit.hooks.PAPIHook;
import me.max.lemonmobcoins.bukkit.listeners.EntityDeathListener;
import me.max.lemonmobcoins.bukkit.listeners.InventoryClickListener;
import me.max.lemonmobcoins.bukkit.listeners.PlayerJoinListener;
import me.max.lemonmobcoins.bukkit.listeners.PluginMessagingListener;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.platform.BukkitWrappedPlatform;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import me.max.lemonmobcoins.common.coinmob.CoinMobManager;
import me.max.lemonmobcoins.common.commands.CustomShopCommand;
import me.max.lemonmobcoins.common.commands.MStoreCommand;
import me.max.lemonmobcoins.common.commands.MobCoinsCommand;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.messages.MessageManager;
import me.max.lemonmobcoins.common.pluginmessaging.AbstractPluginMessageManager;
import me.max.lemonmobcoins.common.utils.FileUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public final class LemonMobCoinsBukkitPlugin extends JavaPlugin {

    private final Logger logger = LoggerFactory.getLogger(LemonMobCoins.class);
    private LemonMobCoins lemonMobCoins;
    private AbstractPluginMessageManager pluginMessageManager;
    private PAPIHook papiHook;
    private YAMLConfigurationLoader dataLoader;

    @Override
    public void onDisable() {
        ConfigurationNode node = null;
        try {
            node = dataLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (node != null && node.getNode("bungeecord").getBoolean()) return;
        try {
            info("Saving data..");
            lemonMobCoins.disable();
            info("Saved data!");
        } catch (IOException | SQLException e) {
            error("Failed saving data! Retrying..");
            try {
                lemonMobCoins.disable();
                info("Saved data!");
            } catch (IOException | SQLException e1) {
                error("Failed saving data again! Data will be lost ;(");
                e.printStackTrace();
            }
        }
        info("Disabled successfully!");
    }

    @Override
    public void onEnable() {
        try {
            info("Loading files and config..");
            FileUtil.saveResource("generalConfig.yml", getDataFolder().toString(), "config.yml");
            MessageManager.load(getDataFolder().toString(), getSLF4JLogger());
            dataLoader = YAMLConfigurationLoader.builder().setFile(new File(getDataFolder().toString(), "config.yml"))
                                                .build();
            info("Loaded config and files!");
        } catch (IOException e) {
            error("Could not load config and files! Stopping plugin!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        final IWrappedPlatform platform = new BukkitWrappedPlatform(this);
        lemonMobCoins = new LemonMobCoins(getSLF4JLogger(), getDataFolder().toString(), platform);

        ConfigurationNode node;
        try {
            node = dataLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        info("Loading listeners..");
        if (node.getNode("bungeecord").getBoolean()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            getServer().getMessenger()
                       .registerIncomingPluginChannel(this, "BungeeCord", new PluginMessagingListener(getCoinManager(), getSLF4JLogger()));
            pluginMessageManager = new BukkitPluginMessageManager(getCoinManager(), getSLF4JLogger());
            registerListeners(new PlayerJoinListener(pluginMessageManager));
        }
        registerListeners(new EntityDeathListener(getCoinManager(), getCoinMobManager(), getPluginMessageManager(), papiHook), new InventoryClickListener(getCoinManager(), getGuiManager(), getPluginMessageManager(), papiHook));
        info("Loaded listeners!");

        info("Loading commands..");
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new MobCoinsCommand(getCoinManager(), papiHook, platform, getGuiManager()));
        manager.registerCommand(new CustomShopCommand(platform, papiHook, getGuiManager()));
        manager.registerCommand(new MStoreCommand(platform, papiHook, getGuiManager()));
        manager.getCommandReplacements().addReplacement("shopCmd", getGuiManager().getCommand().substring(1));
        info("Loaded commands!");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiHook = new PAPIHook();
        } else {
            warn("PlaceholderAPI was not found placeholders from PlaceholderAPI will NOT work!");
        }
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void error(String s) {
        getSLF4JLogger().error(s);
    }

    private void warn(String s) {
        getSLF4JLogger().warn(s);
    }

    private void info(String s) {
        getSLF4JLogger().info(s);
    }


    @NotNull
    private CoinManager getCoinManager() {
        return lemonMobCoins.getCoinManager();
    }

    @NotNull
    private GuiManager getGuiManager() {
        return lemonMobCoins.getGuiManager();
    }

    @NotNull
    private CoinMobManager getCoinMobManager() {
        return lemonMobCoins.getCoinMobManager();
    }

    private AbstractPluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
    }

    private Logger getSLF4JLogger() {
        return logger;
    }

}
