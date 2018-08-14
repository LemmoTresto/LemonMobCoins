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

package me.max.lemonmobcoins.common.abstraction.platform;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedInventory;
import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedItemStack;
import me.max.lemonmobcoins.common.gui.ShopItem;

import java.util.List;
import java.util.UUID;

public interface IWrappedPlatform {

    IWrappedPlayer[] getOnlinePlayers();

    IWrappedPlayer getPlayer(String name);

    IWrappedPlayer getPlayer(UUID uuid);

    IWrappedOfflinePlayer getOfflinePlayer(UUID uuid);

    IWrappedOfflinePlayer getOfflinePlayer(String name);

    void enable();

    void disable();

    IWrappedInventory createInventory(String title, int rows, List<ShopItem> items);

    IWrappedItemStack toItemStack(ShopItem item);

}
