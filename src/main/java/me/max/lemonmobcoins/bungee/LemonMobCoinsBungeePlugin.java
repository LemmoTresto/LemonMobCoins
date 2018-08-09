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

package me.max.lemonmobcoins.bungee;


import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.max.lemonmobcoins.bungee.listeners.PluginMessagingListener;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.data.DataProvider;
import me.max.lemonmobcoins.common.data.providers.MySqlProvider;
import me.max.lemonmobcoins.common.data.providers.YamlBungeeProvider;
import me.max.lemonmobcoins.common.exceptions.APILoadException;
import me.max.lemonmobcoins.common.exceptions.DataLoadException;
import me.max.lemonmobcoins.common.utils.FileUtil;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class LemonMobCoinsBungeePlugin extends Plugin {

    private LemonMobCoins lemonMobCoins;
    private Configuration config;

    @Override
    public void onLoad(){
        try {
            info("Loading config..");
            FileUtil.saveResource("bungeeconfig.yml", getDataFolder(), "config.yml" , this.getClass().getClassLoader());
            info("Loaded config!");
        } catch (Exception e){
            error("Could not load config and messages! Stopping plugin!");
            e.printStackTrace();
            shutdown();
        }

        try {
            String storageType = getConfig().getString("storage.type");
            DataProvider dataProvider;
            if (storageType.equalsIgnoreCase("flatfile")) dataProvider = new YamlBungeeProvider(getDataFolder());
            else if (storageType.equalsIgnoreCase("mysql")){
                Configuration mysqlSection = getConfig().getSection("storage.mysql");
                dataProvider = new MySqlProvider(mysqlSection.getString("hostname"), mysqlSection.getString("port"), mysqlSection.getString("username"), mysqlSection.getString("password"), mysqlSection.getString("database"));
            } else {
                error("Invalid storage type found! Using flatfile!");
                dataProvider = new YamlBungeeProvider(getDataFolder());
            }
            lemonMobCoins = new LemonMobCoins(dataProvider, getLogger());
        } catch (SQLException e){
            error("Failed loading MySql! Stopping plugin!");
            e.printStackTrace();
            shutdown();
        } catch (DataLoadException e){
            error("Failed loading data! Stopping plugin!");
            e.printStackTrace();
            shutdown();
        } catch (APILoadException e){
            error("Failed loading API! Stopping plugin!");
            e.printStackTrace();
            shutdown();
        }
    }

    @Override
    public void onEnable() {
        try {
            info("Loading listeners..");
            getProxy().getPluginManager().registerListener(this, new PluginMessagingListener(getProxy(), getCoinManager()));
            info("Loaded listeners!");
        } catch (Exception e){
            error("Loading Listeners failed! Stopping plugin..");
            e.printStackTrace();
            shutdown();
        }
        for (Map.Entry<UUID, Double> entry : getCoinManager().getCoins().entrySet()){
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("LemonMobCoins");
            out.writeUTF(entry.getKey().toString());
            out.writeDouble(entry.getValue());
            getProxy().getServers().values().forEach(s -> s.sendData("BungeeCord", out.toByteArray(), true));
            info("Sent information of Player " + entry.getKey() + ". Balance sent: " + entry.getValue());
        }

    }

    @Override
    public void onDisable() {
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
    private Configuration getConfig(){
        if (config == null) {
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    private void shutdown(){
        getProxy().unregisterChannel("BungeeCord");
        getProxy().getPluginManager().unregisterListeners(this);
    }
}
