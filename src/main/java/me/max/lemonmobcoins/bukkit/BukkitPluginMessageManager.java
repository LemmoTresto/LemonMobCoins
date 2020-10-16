/*
 *
 *  *
 *  *  * LemonMobCoins - Kill mobs and get coins that can be used to buy awesome things
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
import me.max.lemonmobcoins.bukkit.impl.entity.PlayerBukkitImpl;
import me.max.lemonmobcoins.common.abstraction.pluginmessaging.AbstractPluginMessageManager;
import me.max.lemonmobcoins.common.data.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.UUID;

public final class BukkitPluginMessageManager extends AbstractPluginMessageManager {

    BukkitPluginMessageManager(CoinManager coinManager, Logger logger) {
        super(coinManager, logger);
    }

    public void sendPluginMessage(UUID uuid) {
        Player p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        double balance = getCoinManager().getCoinsOfPlayer(uuid);

        ByteArrayDataOutput out = getPluginMessage(new PlayerBukkitImpl(p), uuid, balance);
        if (out == null) return;

        p.sendPluginMessage(Bukkit.getPluginManager().getPlugin("LemonMobCoins"), "BungeeCord", out.toByteArray());

        getLogger().info("Sent information of Player " + uuid + ". Balance sent: " + balance);
    }

}
