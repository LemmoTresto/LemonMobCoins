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

package me.max.lemonmobcoins.common.files.messages;

import me.max.lemonmobcoins.bukkit.hooks.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    INVALID_USAGE_SET_COMMAND(""),
    INVALID_USAGE_GIVE_COMMAND(""),
    INVALID_USAGE_TAKE_COMMAND(""),
    INVALID_USAGE_RESET_COMMAND(""),
    PAY_PLAYER(""),
    PAID_BY_PLAYER(""),
    NOT_ENOUGH_MONEY_TO_PAY("");

    private String message;

    @SuppressWarnings("SameParameterValue")
    Messages(String message){
        this.message = message;
    }

    public String getMessage(double balance, @Nullable String playerName, @Nullable String entityName, double amount, @Nullable PAPIHook papiHook) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);

        if (entityName != null) msg = msg.replaceAll("%entity%", entityName);

        msg = msg.replaceAll("%amount%", removeZeroDecimal(String.valueOf(amount)));

        if (playerName != null) msg = msg.replaceAll("%player%", playerName);

        msg = msg.replaceAll("%balance%", removeZeroDecimal(String.valueOf(balance)));

        if (papiHook != null) msg = papiHook.replacePlaceholders(Bukkit.getOfflinePlayer(playerName), msg);

        return msg;
    }

    private String removeZeroDecimal(String s){
        if (s.endsWith(".0")) return s.substring(0, s.length() - 2);
        return s;
    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
