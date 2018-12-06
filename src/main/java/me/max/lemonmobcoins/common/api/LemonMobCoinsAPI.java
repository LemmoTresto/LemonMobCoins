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

package me.max.lemonmobcoins.common.api;

import me.max.lemonmobcoins.common.api.event.LMCEventBus;

import java.util.UUID;

@SuppressWarnings("unused")
public class LemonMobCoinsAPI {

    private LMCEventBus eventBus;

    public LemonMobCoinsAPI() {
        this.eventBus = new LMCEventBus();
    }

    /**
     * @param uuid the uuid of the player of who you want to get the balance.
     * @return integer amount of balance player has.
     */
    public double getCoinsOfPlayer(UUID uuid) {
        return 0;
    }

    /**
     * @param uuid  the uuid of the player to change the balance of.
     * @param coins integer amount of how many balance to set the player's balance to.
     */
    public void setCoinsOfPlayer(UUID uuid, double coins) {

    }

    /**
     * @param uuid  uuid of the player to add the balance to
     * @param coins amount of balance to add to balance.
     */
    public void addCoinsToPlayer(UUID uuid, double coins) {

    }

    /**
     * Increments the player's balance by 1.
     *
     * @param uuid the uuid of the player to increment the balance of
     */
    public void incrementPlayerBalance(UUID uuid) {

    }

    /**
     * Deducts an amount of balance from the player's balance.
     *
     * @param uuid  the uuid of the player from who to deduct the balance
     * @param coins the amount of balance to deduct.
     */
    public void deductCoinsFromPlayer(UUID uuid, double coins) {

    }

    /**
     * @return the event bus.
     */
    public LMCEventBus getEventBus() {
        return eventBus;
    }

}
