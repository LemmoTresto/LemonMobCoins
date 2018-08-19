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

package me.max.lemonmobcoins.sponge;

import co.aikar.commands.SpongeCommandManager;
import com.google.inject.Inject;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import me.max.lemonmobcoins.common.abstraction.platform.SpongeWrappedPlatform;
import me.max.lemonmobcoins.common.coinmob.CoinMobManager;
import me.max.lemonmobcoins.common.commands.CustomShopCommand;
import me.max.lemonmobcoins.common.commands.MStoreCommand;
import me.max.lemonmobcoins.common.commands.MobCoinsCommand;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.messages.MessageManager;
import me.max.lemonmobcoins.common.pluginmessaging.AbstractPluginMessageManager;
import me.max.lemonmobcoins.common.utils.FileUtil;
import me.max.lemonmobcoins.sponge.listeners.ClickInventoryListener;
import me.max.lemonmobcoins.sponge.listeners.EntityDeathListener;
import me.max.lemonmobcoins.sponge.listeners.PlayerJoinListener;
import me.max.lemonmobcoins.sponge.listeners.PluginMessagingListener;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Arrays;

@Plugin(id = "lemonmobcoins", name = "LemonMobCoins", version = "1.5", authors = "LemmoTresto")
public final class LemonMobCoinsSpongePlugin {

    private LemonMobCoins lemonMobCoins;

    @Inject
    private Game game;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private YAMLConfigurationLoader dataLoader;
    private IWrappedPlatform platform;
    private AbstractPluginMessageManager pluginMessageManager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        try {
            info("Loading messages and config..");
            FileUtil.saveResource("generalConfig.yml", configDir.toString(), "config.yml");
            MessageManager.load(configDir.toString(), logger);
            dataLoader = YAMLConfigurationLoader.builder().setFile(new File(configDir.toString(), "config.yml"))
                                                .build();
            info("Loaded config and files!");
        } catch (IOException e) {
            error("Could not load config and files!");
            e.printStackTrace();
            shutdown();
            return;
        }
        platform = new SpongeWrappedPlatform(this);
        lemonMobCoins = new LemonMobCoins(getLogger(), configDir.toString(), new SpongeWrappedPlatform(this));
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        ConfigurationNode node;
        try {
            node = dataLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        info("Loading listeners..");
        if (node.getNode("bungeecord").getBoolean()) {
            pluginMessageManager = new SpongePluginMessageManager(getCoinManager(), getLogger());
            if (!Sponge.getChannelRegistrar().getChannel("BungeeCord").isPresent()) {
                Sponge.getChannelRegistrar().createRawChannel(this, "BungeeCord")
                      .addListener(new PluginMessagingListener(getCoinManager(), getLogger()));
            }
            registerListeners(new PlayerJoinListener(pluginMessageManager));
        }
        registerListeners(new EntityDeathListener(getCoinManager(), getCoinMobManager(), getPluginMessageManager()), new ClickInventoryListener(getCoinManager(), getGuiManager(), getPluginMessageManager()));
        info("Loaded listeners!");

        info("Loading commands..");
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        SpongeCommandManager manager = new SpongeCommandManager(game.getPluginManager().getPlugin("lemonmobcoins")
                                                                    .get());
        manager.registerCommand(new MobCoinsCommand(getCoinManager(), null, platform, getGuiManager()));
        manager.registerCommand(new CustomShopCommand(platform, null, getGuiManager()));
        manager.registerCommand(new MStoreCommand(platform, null, getGuiManager()));
        manager.getCommandReplacements().addReplacement("shopCmd", getGuiManager().getCommand().substring(1));
        info("Loaded commands!");
    }

    @Listener
    public void onServerStop(GameStoppingEvent event) {
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
        shutdown();
        info("Disabled successfully!");
    }

    private void shutdown() {
        game.getEventManager().unregisterPluginListeners(this);
        game.getCommandManager().getOwnedBy(this).forEach(game.getCommandManager()::removeMapping);
        game.getScheduler().getScheduledTasks(this).forEach(Task::cancel);
    }

    private void registerListeners(Object... listeners) {
        Arrays.stream(listeners).forEach(l -> game.getEventManager().registerListeners(this, l));
    }

    private void info(String s) {
        logger.info(s);
    }

    private void warn(String s) {
        logger.warn(s);
    }

    private void error(String s) {
        logger.error(s);
    }

    private Logger getLogger() {
        return logger;
    }

    private CoinManager getCoinManager() {
        return lemonMobCoins.getCoinManager();
    }

    private CoinMobManager getCoinMobManager() {
        return lemonMobCoins.getCoinMobManager();
    }

    private GuiManager getGuiManager() {
        return lemonMobCoins.getGuiManager();
    }

    private AbstractPluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
    }

}
