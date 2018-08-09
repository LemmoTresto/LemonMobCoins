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

package me.max.lemonmobcoins.common.data.providers;

import me.max.lemonmobcoins.common.data.DataProvider;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlBukkitProvider implements DataProvider {

    private File dataFolder;

    public YamlBukkitProvider(File dataFolder){
        this.dataFolder = dataFolder;
    }

    @Override
    public Map<UUID, Double> loadData() throws IOException {
        Map<UUID, Double> coins = new HashMap<>();

        //Create the data directory and coins file if they do not exist.
        File file = new File(dataFolder.toString(), "data");
        file.mkdir();
        file = new File(dataFolder.toString() + "/data/", "coins.yml");
        file.createNewFile();

        //Read the data file and put all data in the map.
        YamlConfiguration coinsData = YamlConfiguration.loadConfiguration(file);
        coinsData.getKeys(false).forEach(key -> coins.put(UUID.fromString(key), coinsData.getDouble(key)));

        return coins;
    }

    @Override
    public void saveData(Map<UUID, Double> coins) throws IOException {
        File file = new File(dataFolder.toString() + "/data/", "coins.yml");
        YamlConfiguration coinsData = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<UUID, Double> entry : coins.entrySet()) coinsData.set(entry.getKey().toString(), entry.getValue());
        coinsData.save(file);
    }
}
