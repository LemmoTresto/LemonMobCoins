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

package me.max.lemonmobcoins.listeners;

import me.max.lemonmobcoins.LemonMobCoins;
import me.max.lemonmobcoins.coins.CoinMob;
import me.max.lemonmobcoins.files.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    private LemonMobCoins lemonMobCoins;

    public EntityDeathListener(LemonMobCoins lemonMobCoins){
        this.lemonMobCoins = lemonMobCoins;
        lemonMobCoins.getServer().getPluginManager().registerEvents(this, lemonMobCoins);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event){
        if (event.getEntity().getKiller() == null) return;

        CoinMob coinMob = null;
        for (CoinMob coinMob1 : lemonMobCoins.getCoinManager().getCoinMobList()){
            if (coinMob1.getMob() == event.getEntityType()){
                coinMob = coinMob1;
                break;
            }
        }

        if (coinMob == null) return;

        int amountToDrop = coinMob.getAmountToDrop();
        if (amountToDrop == 0) return;
        lemonMobCoins.getCoinManager().addCoinsToPlayer(event.getEntity().getKiller(), amountToDrop);

        event.getEntity().getKiller().sendMessage(Messages.RECEIVED_COINS_FROM_KILL.getMessage(lemonMobCoins, event.getEntity().getKiller(), event.getEntity(), amountToDrop));

    }
}
