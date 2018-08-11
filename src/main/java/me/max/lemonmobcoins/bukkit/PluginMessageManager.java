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
import me.max.lemonmobcoins.common.data.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PluginMessageManager {

    //This should not be reloaded when doing /mc reload which is why we instantiate it statically.
    private final List<UUID> cache = new ArrayList<>();
    private final Logger logger;
    private final CoinManager coinManager;

    public PluginMessageManager(Logger logger, CoinManager coinManager){
        this.logger = logger;
        this.coinManager = coinManager;
    }

    public void sendPluginMessage(UUID uuid){
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (p == null) {
            cache.add(uuid);
            return;
        }

        double balance = coinManager.getCoinsOfPlayer(uuid);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LemonMobCoins");
        out.writeUTF(uuid.toString());
        out.writeDouble(balance);
        p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("LemonMobCoins"), "BungeeCord", out.toByteArray());

        logger.info("Sent information of Player " + uuid + ". Balance sent: " + balance);
    }

    public void sendPendingPluginMessages() {
        cache.forEach(this::sendPluginMessage);
    }

    public List<UUID> getCache() {
        return cache;
    }
}
