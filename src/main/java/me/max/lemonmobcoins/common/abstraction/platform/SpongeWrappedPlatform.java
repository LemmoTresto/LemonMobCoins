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

package me.max.lemonmobcoins.common.abstraction.platform;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.entity.SpongeWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.SpongeWrappedPlayer;
import me.max.lemonmobcoins.sponge.LemonMobCoinsSpongePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpongeWrappedPlatform implements IWrappedPlatform {

    private LemonMobCoinsSpongePlugin plugin;

    public SpongeWrappedPlatform(LemonMobCoinsSpongePlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public IWrappedPlayer[] getOnlinePlayers() {
        List<IWrappedPlayer> players = new ArrayList<>();
        Sponge.getGame().getServer().getOnlinePlayers().forEach(player -> players.add(new SpongeWrappedPlayer(player)));
        return players.toArray(new IWrappedPlayer[0]);
    }

    @Override
    public IWrappedPlayer getPlayer(String name) {
        Optional<Player> player = Sponge.getGame().getServer().getPlayer(name);
        return player.map(SpongeWrappedPlayer::new).orElse(null);
    }

    @Override
    public IWrappedPlayer getPlayer(UUID uuid) {
        Optional<Player> player = Sponge.getGame().getServer().getPlayer(uuid);
        return player.map(SpongeWrappedPlayer::new).orElse(null);
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(UUID uuid) {
        Optional<User> user = getUser(uuid);
        return user.map(SpongeWrappedOfflinePlayer::new).orElse(null);
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(String name) {
        Optional<User> user = getUser(name);
        return user.map(SpongeWrappedOfflinePlayer::new).orElse(null);
    }

    @Override
    public void enable() {
        //todo
    }

    @Override
    public void disable() {
        //todo
    }

    private Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.flatMap(userStorage1 -> userStorage1.get(uuid));
    }

    private Optional<User> getUser(String name) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.flatMap(userStorage1 -> userStorage1.get(name));
    }
}
