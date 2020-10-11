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

package me.max.lemonmobcoins.common.coinmob;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class CoinMobManager {

    private final List<CoinMob> coinMobList;

    public CoinMobManager(ConfigurationNode config) {
        coinMobList = new ArrayList<>();

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : config.getNode("mob-list").getChildrenMap()
                                                                          .entrySet()) {

            List<String> amounts;
            String amount = entry.getValue().getNode("amount").getString();
            if (amount.contains("-")) amounts = Arrays.asList(amount.split("-"));
            else {
                amounts = new ArrayList<>();
                amounts.add(amount);
                amounts.add("0");
            }

            coinMobList.add(new CoinMob(entry.getKey().toString(), entry.getValue().getNode("chance").getInt(), Integer
                    .parseInt(amounts.get(0)), Integer.parseInt(amounts.get(1))));
        }
    }

    private List<CoinMob> getCoinMobList() {
        return coinMobList;
    }

    public CoinMob getCoinMob(String type) {
        return getCoinMobList().stream().filter(coinMob -> coinMob.getMob().equalsIgnoreCase(type)).findFirst()
                               .orElse(null);
    }

}
