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

package me.max.lemonmobcoins.common.api;

import java.util.UUID;

public interface LemonMobCoinsAPI {

    /**
     * @param uuid the uuid of the player of who you want to get the coins.
     * @return integer amount of coins player has.
     */
    double getCoinsOfPlayer(UUID uuid);

    /**
     * @param uuid  the uuid of the player to change the balance of.
     * @param coins integer amount of how many coins to set the player's balance to.
     */
    void setCoinsOfPlayer(UUID uuid, double coins);

    /**
     * @param uuid  uuid of the player to add the coins to
     * @param coins amount of coins to add to balance.
     */
    void addCoinsToPlayer(UUID uuid, double coins);

    /**
     * Increments the player's balance by 1.
     *
     * @param uuid the uuid of the player to increment the balance of
     */
    void incrementPlayerBalance(UUID uuid);

    /**
     * Deducts an amount of coins from the player's balance.
     *
     * @param uuid  the uuid of the player from who to deduct the coins
     * @param coins the amount of coins to deduct.
     */
    void deductCoinsFromPlayer(UUID uuid, double coins);

}
