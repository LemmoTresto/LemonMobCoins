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
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlProvider implements DataProvider {

    private YAMLConfigurationLoader dataLoader;

    public YamlProvider(String dataFolder) throws IOException {
        File dataFile = new File(dataFolder + File.separator + "data" + File.separator, "coins.yml");
        dataFile.mkdir();
        dataFile.createNewFile();
        dataLoader = YAMLConfigurationLoader.builder().setFile(dataFile).build();
    }

    @Override
    public Map<UUID, Double> loadData() throws IOException {
        Map<UUID, Double> coins = new HashMap<>();
        ConfigurationNode coinsData = dataLoader.load();
        coinsData.getChildrenMap().forEach((key, value) -> coins.put(UUID.fromString(String.valueOf(key)), Double.parseDouble(String.valueOf(value))));
        return coins;
    }

    @Override
    public void saveData(Map<UUID, Double> coins) throws IOException {
        ConfigurationNode coinsData = dataLoader.load();
        coins.forEach((key, value) -> coinsData.getNode(key).setValue(value));
        dataLoader.save(coinsData);
    }
}
