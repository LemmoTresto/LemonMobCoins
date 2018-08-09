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

package me.max.lemonmobcoins.bukkit.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class PlayerJoinListener implements Listener {

    //This should not be reloaded when doing /mc reload which is why we instantiate it statically.
    private Map<UUID, Double> cache = new HashMap<>();
    private Timer timer = new Timer();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Double> entry : cache.entrySet()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("LemonMobCoins");
                    out.writeUTF(entry.getKey().toString());
                    out.writeDouble(entry.getValue());
                    event.getPlayer().sendPluginMessage(Bukkit.getPluginManager().getPlugin("LemonMobCoins"), "BungeeCord", out.toByteArray());
                    Bukkit.getPluginManager().getPlugin("LemonMobCoins").getLogger().info("Sent information of Player " + entry.getKey() + ". Balance sent: " + entry.getValue());
                }
                cache.clear();
            }
        }, 1500);
    }

    public void addToCache(UUID uuid, double balance){
        cache.put(uuid, balance);
    }
}
