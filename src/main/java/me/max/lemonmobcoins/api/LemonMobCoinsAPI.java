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

package me.max.lemonmobcoins.api;

import me.max.lemonmobcoins.LemonMobCoins;
import org.bukkit.OfflinePlayer;

@SuppressWarnings("unused")
public class LemonMobCoinsAPI {

    private LemonMobCoins lemonMobCoins;

    public LemonMobCoinsAPI(LemonMobCoins lemonMobCoins){
        this.lemonMobCoins = lemonMobCoins;
    }

    /**
     * @param player offline player of who you want to get the coins.
     * @return integer amount of coins player has.
     */
    public double getCoinsOfPlayer(OfflinePlayer player){
        return lemonMobCoins.getCoinManager().getCoinsOfPlayer(player);
    }

    /**
     * @param player the player to change the balance of.
     * @param coins integer amount of how many coins to set the player's balance to.
     */
    public void setCoinsOfPlayer(OfflinePlayer player, int coins){
        lemonMobCoins.getCoinManager().setCoinsOfPlayer(player, coins);
    }

    /**
     * @param p player to add the coins to
     * @param coins amount of coins to add to balance.
     */
    public void addCoinsToPlayer(OfflinePlayer p, int coins){
        lemonMobCoins.getCoinManager().addCoinsToPlayer(p, coins);
    }

    /**
     * Increments the player's balance by 1.
     * @param player the player to increment the balance of
     */
    public void incrementPlayerBalance(OfflinePlayer player){
        lemonMobCoins.getCoinManager().incrementPlayerBalance(player);
    }

    /**
     * Deducts an amount of coins from the player's balance.
     * @param p the player from who to deduct the coins
     * @param coins the amount of coins to deduct.
     */
    public void deductCoinsFromPlayer(OfflinePlayer p, double coins) {
        lemonMobCoins.getCoinManager().deductCoinsFromPlayer(p, coins);
    }

}
