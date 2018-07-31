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

package me.max.lemonmobcoins.coins;

import me.max.lemonmobcoins.LemonMobCoins;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CoinManager {

    private LemonMobCoins lemonMobCoins;
    private Map<OfflinePlayer, Double> coins;
    private List<CoinMob> coinMobList;

    public CoinManager(LemonMobCoins lemonMobCoins) throws IOException {
        this.lemonMobCoins = lemonMobCoins;
        coins = new HashMap<>();
        coinMobList = new ArrayList<>();

        //Create the data directory and coins file if they do not exist.
        File file = new File(lemonMobCoins.getDataFolder().toString(), "data");
        file.mkdir();
        file = new File(lemonMobCoins.getDataFolder().toString() + "/data/", "coins.yml");
        file.createNewFile();

        //Read the data file and put all data in the map.
        YamlConfiguration coinsData = YamlConfiguration.loadConfiguration(file);
        for (String key : coinsData.getKeys(false)) coins.put(Bukkit.getOfflinePlayer(UUID.fromString(key)), coinsData.getDouble(key));

        //Read config file
        ConfigurationSection mobList = lemonMobCoins.getConfig().getConfigurationSection("mob-list");
        for (String key : mobList.getKeys(false)) {
            ConfigurationSection coinMob = mobList.getConfigurationSection(key);

            List<String> amounts;
            String amount = coinMob.getString("amount");
            if (amount.contains("-")) amounts = Arrays.asList(amount.split("-"));
            else {
                amounts = new ArrayList<>();
                amounts.add(amount);
                amounts.add("0");
            }

            coinMobList.add(new CoinMob(EntityType.valueOf(key.toUpperCase()), coinMob.getInt("chance"), Integer.parseInt(amounts.get(0)), Integer.parseInt(amounts.get(1))));
        }
    }

    public void saveData() throws IOException {
        File file = new File(lemonMobCoins.getDataFolder().toString() + "/data/", "coins.yml");
        YamlConfiguration coinsData = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<OfflinePlayer, Double> entry : coins.entrySet()) coinsData.set(entry.getKey().getUniqueId().toString(), entry.getValue());
        coinsData.save(file);
    }

    public double getCoinsOfPlayer(OfflinePlayer p){
        return coins.getOrDefault(p, 0.0);
    }

    public void setCoinsOfPlayer(OfflinePlayer p, double coins){
        this.coins.put(p, coins);
    }

    public void addCoinsToPlayer(OfflinePlayer p, double coins){
        setCoinsOfPlayer(p, getCoinsOfPlayer(p) + coins);
    }

    public void incrementPlayerBalance(OfflinePlayer p){
        setCoinsOfPlayer(p, getCoinsOfPlayer(p) + 1);
    }

    public List<CoinMob> getCoinMobList() {
        return coinMobList;
    }

    public void deductCoinsFromPlayer(OfflinePlayer p, double price) {
        setCoinsOfPlayer(p, getCoinsOfPlayer(p) - price);
    }
}
