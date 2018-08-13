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

package me.max.lemonmobcoins.sponge.listeners;

import me.max.lemonmobcoins.common.data.CoinManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;

import java.util.UUID;

public class PluginMessagingListener implements RawDataListener {

    private CoinManager coinManager;
    private Logger logger;

    public PluginMessagingListener(CoinManager coinManager, Logger logger) {
        this.coinManager = coinManager;
        this.logger = logger;
    }

    @Override
    public void handlePayload(@NotNull ChannelBuf data, @NotNull RemoteConnection connection, @NotNull Platform.Type side) {
        String subChannel = data.readUTF();
        if (!subChannel.equals("LemonMobCoins")) return;

        String playerUuid = data.readUTF();
        double balance = data.readDouble();

        coinManager.setCoinsOfPlayer(UUID.fromString(playerUuid), balance);
        logger.info("Received information of Player " + playerUuid + ". Balance received: " + balance);
    }

}
