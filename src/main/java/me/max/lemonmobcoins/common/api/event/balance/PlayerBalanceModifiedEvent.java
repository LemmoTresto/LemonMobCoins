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

package me.max.lemonmobcoins.common.api.event.balance;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.api.event.Cancellable;
import me.max.lemonmobcoins.common.api.event.Event;

/**
 * Fired when a player's balance gets modified
 * <p>
 * This event gets fired in different types.
 */
@SuppressWarnings("unused")
@Cancellable
public final class PlayerBalanceModifiedEvent extends Event {

    private IWrappedOfflinePlayer player;
    private double oldBalance;
    private double newBalance;
    private Type type;

    public PlayerBalanceModifiedEvent(IWrappedOfflinePlayer player, double oldBalance, double newBalance, Type type) {
        this.player = player;
        this.newBalance = newBalance;
        this.type = type;
    }

    public IWrappedOfflinePlayer getPlayer() {
        return player;
    }

    public double getOldBalance() {
        return oldBalance;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        GIVEN,
        TAKEN,
        SET,
        RESET,
        EARN,

    }

}
