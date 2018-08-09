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
import me.max.lemonmobcoins.bukkit.coins.CoinMob;
import me.max.lemonmobcoins.bukkit.messages.Messages;
import me.max.lemonmobcoins.common.data.CoinManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityDeathListener implements Listener {

    private CoinManager coinManager;
    private List<CoinMob> coinMobList;
    private PluginMessageManager pluginMessageManager;

    public EntityDeathListener(CoinManager coinManager, ConfigurationSection mobList, PluginMessageManager pluginMessageManager){
        this.coinManager = coinManager;
        this.pluginMessageManager = pluginMessageManager;
        coinMobList = new ArrayList<>();

        for (String key : mobList.getKeys(false)) {
            ConfigurationSection coinMob = mobList.getConfigurationSection(key);

            List<String> amounts;
            String amount = coinMob.getString("amount");
            if (amount.contains("-")) amounts = Arrays.asList(amount.split("-"));
            else {
                amounts = new ArrayList<>();
                amounts.add(amount);
                amounts.add("0");
            }

            coinMobList.add(new CoinMob(EntityType.valueOf(key.toUpperCase()), coinMob.getInt("chance"), Integer.parseInt(amounts.get(0)), Integer.parseInt(amounts.get(1))));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event){
        if (event.getEntity().getKiller() == null) return;

        CoinMob coinMob = getCoinMob(event.getEntityType());
        if (coinMob == null) return;

        int amountToDrop = coinMob.getAmountToDrop();
        if (amountToDrop == 0) return;
        coinManager.addCoinsToPlayer(event.getEntity().getKiller().getUniqueId(), amountToDrop);
        if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(event.getEntity().getKiller().getUniqueId(), coinManager.getCoinsOfPlayer(event.getEntity().getKiller().getUniqueId()));

        event.getEntity().getKiller().sendMessage(Messages.RECEIVED_COINS_FROM_KILL.getMessage(coinManager, event.getEntity().getKiller(), event.getEntity(), amountToDrop));

    }

    private List<CoinMob> getCoinMobList(){
        return coinMobList;
    }

    private CoinMob getCoinMob(@NotNull EntityType entityType){
        return getCoinMobList().stream().filter(coinMob -> coinMob.getMob() == entityType).findFirst().orElse(null);
    }

}
