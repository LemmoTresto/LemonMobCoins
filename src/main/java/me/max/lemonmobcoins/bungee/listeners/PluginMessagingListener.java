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

package me.max.lemonmobcoins.bungee.listeners;


import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.max.lemonmobcoins.common.data.CoinManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public final class PluginMessagingListener implements Listener {

    private final ProxyServer server;
    private final CoinManager coinManager;

    public PluginMessagingListener(ProxyServer server, CoinManager coinManager) {
        this.server = server;
        this.coinManager = coinManager;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("BungeeCord")) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (!subChannel.equals("LemonMobCoins")) return;

        String playerUuid = in.readUTF();
        double balance = in.readDouble();
        server.getPluginManager().getPlugin("LemonMobCoins").getLogger()
              .info("Received information of Player " + playerUuid + ". Balance received: " + balance);
        coinManager.setCoinsOfPlayer(UUID.fromString(playerUuid), balance);

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("LemonMobCoins");
        out.writeUTF(playerUuid);
        out.writeDouble(balance);

        server.getServers().values().forEach(s -> s.sendData("BungeeCord", out.toByteArray(), true));
        server.getPluginManager().getPlugin("LemonMobCoins").getLogger()
              .info("Sent information of Player " + playerUuid + ". Balance sent: " + balance);
    }

}
