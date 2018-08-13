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

import me.max.lemonmobcoins.common.coinmob.CoinMob;
import me.max.lemonmobcoins.common.coinmob.CoinMobManager;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.messages.Messages;
import me.max.lemonmobcoins.common.pluginmessaging.AbstractPluginMessageManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;

public class EntityDeathListener {

    private final CoinManager coinManager;
    private final CoinMobManager coinMobManager;
    private final AbstractPluginMessageManager pluginMessageManager;

    public EntityDeathListener(CoinManager coinManager, CoinMobManager coinMobManager, AbstractPluginMessageManager pluginMessageManager) {
        this.coinManager = coinManager;
        this.coinMobManager = coinMobManager;
        this.pluginMessageManager = pluginMessageManager;
    }

    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        if (! (event.getSource() instanceof Player)) return;
        Player p = (Player) event.getSource();

        CoinMob coinMob = coinMobManager.getCoinMob(event.getTargetEntity().getType().toString());
        if (coinMob == null) return;

        int amountToDrop = coinMob.getAmountToDrop();
        if (amountToDrop == 0) return;
        coinManager.addCoinsToPlayer(p.getUniqueId(), amountToDrop);

        if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId());

        p.sendMessage(Text.of(Messages.RECEIVED_COINS_FROM_KILL
                .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), event.getTargetEntity()
                                                                                             .getType()
                                                                                             .getName(), amountToDrop, null)));

    }

}
