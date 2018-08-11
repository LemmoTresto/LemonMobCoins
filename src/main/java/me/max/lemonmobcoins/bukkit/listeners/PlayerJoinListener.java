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

import me.max.lemonmobcoins.bukkit.PluginMessageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerJoinListener implements Listener {

    private final Timer timer = new Timer();
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            pluginMessageManager.sendPendingPluginMessages();
        }
    };
    private final PluginMessageManager pluginMessageManager;

    public PlayerJoinListener(PluginMessageManager pluginMessageManager){
        this.pluginMessageManager = pluginMessageManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if (pluginMessageManager.getCache().isEmpty()) return;
        timer.schedule(timerTask, 1500);
    }
}
