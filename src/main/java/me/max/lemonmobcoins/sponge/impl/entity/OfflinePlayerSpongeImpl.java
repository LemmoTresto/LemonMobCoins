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

package me.max.lemonmobcoins.sponge.impl.entity;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import org.spongepowered.api.entity.living.player.User;

import java.util.UUID;

public final class OfflinePlayerSpongeImpl implements IWrappedOfflinePlayer {

    private final User user;

    public OfflinePlayerSpongeImpl(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public UUID getUniqueId() {
        return user.getUniqueId();
    }

    @Override
    public boolean isOnline() {
        return user.isOnline();
    }

    @Override
    public IWrappedPlayer getOnlinePlayer() {
        return user.getPlayer().map(PlayerSpongeImpl::new).orElse(null);
    }

}
