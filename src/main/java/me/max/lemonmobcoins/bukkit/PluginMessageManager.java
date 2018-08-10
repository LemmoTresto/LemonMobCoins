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

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PluginMessageManager {

    //This should not be reloaded when doing /mc reload which is why we instantiate it statically.
    private Map<UUID, Double> cache = new HashMap<>();
    private Logger logger;

    public PluginMessageManager(Logger logger){
        this.logger = logger;
    }

    public void sendPluginMessage(UUID uuid, double balance){
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (p == null) {
            cache.put(uuid, balance);
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LemonMobCoins");
        out.writeUTF(uuid.toString());
        out.writeDouble(balance);
        p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("LemonMobCoins"), "BungeeCord", out.toByteArray());
        logger.info("Sent information of Player " + uuid + ". Balance sent: " + balance);
    }

    public void sendPendingPluginMessages() {
        for (Map.Entry<UUID, Double> entry : cache.entrySet()){
            sendPluginMessage(entry.getKey(), entry.getValue());
        }
    }
}
