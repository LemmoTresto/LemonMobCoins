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

package me.max.lemonmobcoins.files;

import me.clip.placeholderapi.PlaceholderAPI;
import me.max.lemonmobcoins.coins.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public enum Messages {
    RECEIVED_COINS_FROM_KILL(""),
    PURCHASED_ITEM_FROM_SHOP(""),
    NOT_ENOUGH_MONEY_TO_PURCHASE(""),
    NO_PERMISSION_TO_PURCHASE(""),
    UNKNOWN_SUBCOMMAND(""),
    ADMIN_HELP_MENU(""),
    PLAYER_HELP_MENU(""),
    OWN_PLAYER_BALANCE(""),
    OTHER_PLAYER_BALANCE(""),
    CONSOLE_CANNOT_USE_COMMAND(""),
    UNKNOWN_PLAYER(""),
    NO_PERMISSION_TO_EXECUTE(""),
    SET_PLAYER_BALANCE(""),
    TAKE_PLAYER_BALANCE(""),
    GIVE_PLAYER_BALANCE(""),
    RESET_PLAYER_BALANCE(""),
    START_RELOAD(""),
    SUCCESSFUL_RELOAD(""),
    FAILED_RELOAD(""),
    // These supress warnings are because we use Messages#valueOf() instead of directly accessing these to reduce code.
    @SuppressWarnings("unused") INVALID_USAGE_SET_COMMAND(""),
    @SuppressWarnings("unused") INVALID_USAGE_GIVE_COMMAND(""),
    @SuppressWarnings("unused") INVALID_USAGE_TAKE_COMMAND(""),
    INVALID_USAGE_RESET_COMMAND("");

    private String message;

    Messages(String message){
        this.message = message;
    }

    @NotNull
    public String getMessage(CoinManager coinManager, OfflinePlayer p, Entity e, double received_coins) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);

        if (e != null) msg = msg.replaceAll("%entity%", e.getName());

        if (received_coins != 0){
            String coins = String.valueOf(received_coins);
            if (coins.endsWith(".0")){
                msg = msg.replaceAll("%amount%", coins.substring(0, coins.length() - 2));
            } else {
                msg = msg.replaceAll("%amount", coins);
            }
        }

        if (p != null){
            msg = msg.replaceAll("%player%", p.getName());
            String coins = String.valueOf(coinManager.getCoinsOfPlayer(p));
            if (coins.endsWith(".0")){
                msg = msg.replaceAll("%balance%", coins.substring(0, coins.length() - 2));
            } else {
                msg = msg.replaceAll("%balance%", coins);
            }
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) msg = PlaceholderAPI.setPlaceholders(p, msg);

        }

        return msg;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
