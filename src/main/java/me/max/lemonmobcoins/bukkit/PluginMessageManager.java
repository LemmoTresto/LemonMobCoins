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
import me.max.lemonmobcoins.bukkit.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginMessageManager {

    private PlayerJoinListener playerJoinListener;

    PluginMessageManager(PlayerJoinListener playerJoinListener){
        this.playerJoinListener = playerJoinListener;
    }

    public void sendPluginMessage(UUID uuid, double balance){
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        if (p == null) {
            playerJoinListener.addToCache(uuid, balance);
            return;
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LemonMobCoins");
        out.writeUTF(uuid.toString());
        out.writeDouble(balance);
        p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("LemonMobCoins"), "BungeeCord", out.toByteArray());
        Bukkit.getPluginManager().getPlugin("LemonMobCoins").getLogger().info("Sent information of Player " + uuid + ". Balance sent: " + balance);
    }
}
