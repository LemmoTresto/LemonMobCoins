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

package me.max.lemonmobcoins.files;

import me.max.lemonmobcoins.LemonMobCoins;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager {

    private LemonMobCoins lemonMobCoins;

    public MessageManager(LemonMobCoins lemonMobCoins){
        this.lemonMobCoins = lemonMobCoins;

        File file = new File(lemonMobCoins.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            lemonMobCoins.saveResource("messages.yml", false);
            return;
        }

        YamlConfiguration messages = YamlConfiguration.loadConfiguration(file);
        for (String key : messages.getKeys(false)){
            Messages.valueOf(key).setMessage(messages.getString(key));
        }

    }


}
