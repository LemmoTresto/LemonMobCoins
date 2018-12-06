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

package me.max.lemonmobcoins.bukkit.listeners;

import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.entity.BukkitWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.api.event.balance.PlayerBalanceModifiedEvent;
import me.max.lemonmobcoins.common.coinmob.CoinMob;
import me.max.lemonmobcoins.common.coinmob.CoinMobManager;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.messages.Messages;
import me.max.lemonmobcoins.common.pluginmessaging.AbstractPluginMessageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private final CoinManager coinManager;
    private final AbstractPluginMessageManager pluginMessageManager;
    private final CoinMobManager coinMobManager;

    public EntityDeathListener(CoinManager coinManager, CoinMobManager coinMobManager, AbstractPluginMessageManager pluginMessageManager) {
        this.coinManager = coinManager;
        this.pluginMessageManager = pluginMessageManager;
        this.coinMobManager = coinMobManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        Player p = event.getEntity().getKiller();
        if (p == null) return;

        CoinMob coinMob = coinMobManager.getCoinMob(event.getEntityType().toString());
        if (coinMob == null) return;

        int amountToDrop = coinMob.getAmountToDrop();
        if (amountToDrop == 0) return;

        PlayerBalanceModifiedEvent playerBalanceModifiedEvent = new PlayerBalanceModifiedEvent(new BukkitWrappedOfflinePlayer(p), coinManager
                .getCoinsOfPlayer(p.getUniqueId()), coinManager
                .getCoinsOfPlayer(p.getUniqueId()) + amountToDrop, PlayerBalanceModifiedEvent.Type.EARN);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(playerBalanceModifiedEvent)) {
            coinManager.addCoinsToPlayer(p.getUniqueId(), amountToDrop);
            if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId());

            event.getEntity().getKiller().sendMessage(Messages.RECEIVED_COINS_FROM_KILL
                    .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), event.getEntity()
                                                                                                 .getName(), amountToDrop));
        }
    }

}
