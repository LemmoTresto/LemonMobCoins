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

package me.max.lemonmobcoins.sponge.impl.inventory;

import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedItemStack;
import org.spongepowered.api.item.inventory.ItemStack;

public final class ItemStackSpongeImpl implements IWrappedItemStack {

    private final ItemStack itemStack;

    public ItemStackSpongeImpl(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public ItemStack getStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStack)) return false;
        return itemStack.equalTo((ItemStack) obj);
    }

}
