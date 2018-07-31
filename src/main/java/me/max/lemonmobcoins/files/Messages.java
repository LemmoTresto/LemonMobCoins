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

import me.max.lemonmobcoins.LemonMobCoins;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

public enum Messages {
    RECEIVED_COINS_FROM_KILL("You received %amount% MobCoins for killing %entity%, your total balance is %balance%"),
    PURCHASED_ITEM_FROM_SHOP("You bought %item% for %amount%, the balance left is %balance%"),
    NOT_ENOUGH_MONEY_TO_PURCHASE("You do not have enough funds to buy this! Your current balance is %balance%"),
    NO_PERMISSION_TO_PURCHASE("You do not have permission to buy this!"),
    UNKNOWN_SUBCOMMAND("Unknown subcommand, try /mobcoins help"),
    ADMIN_HELP_MENU("/mobcoins help - Display this menu\n" +
            "/mobcoins balance - Check your balance\n" +
            "/mobcoins balance [name] - Check somebody else balance\n" +
            "/mobcoins shop\n" +
            "/mshop - Open the shop\n" +
            "/mc give [name] [amount] - Give mob coins to a player\n" +
            "/mc take [name] [amount] - Take mob coins from a player\n" +
            "/mc set [name] [amount] - Set the balance of a player"),
    PLAYER_HELP_MENU("/mobcoins help - Display this menu\n" +
            "/mobcoins balance - Check your balance\n" +
            "/mobcoins balance [name] - Check somebody else balance\n" +
            "/mobcoins shop\n" +
            "/mshop - Open the shop"),
    OWN_PLAYER_BALANCE("Your balance is %balance%"),
    OTHER_PLAYER_BALANCE("%player%'s balance is %balance%"),
    CONSOLE_CANNOT_USE_COMMAND("Console cannot use this command!"),
    UNKNOWN_PLAYER("Unknown player."),
    NO_PERMISSION_TO_EXECUTE("You don't have permission to execute this command!"),
    SET_PLAYER_BALANCE("Successfully set %player%'s balance to %balance%"),
    TAKE_PLAYER_BALANCE("Successfully taken %amount$ from %player%'s balance."),
    GIVE_PLAYER_BALANCE("Successfully given %amount% to %player%");

    private String message;

    Messages(String message){
        this.message = message;
    }

    public String getMessage(LemonMobCoins lemonMobCoins, OfflinePlayer p, Entity e, double received_coins) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);
        if (p != null) msg = msg.replaceAll("%player%", p.getName());
        if (e != null) msg = msg.replaceAll("%entity%", e.getName());
        if (received_coins != 0) msg = msg.replaceAll("%amount%", String.valueOf(received_coins));
        if (p != null) msg = msg.replaceAll("%balance%", String.valueOf(lemonMobCoins.getCoinManager().getCoinsOfPlayer(p)));
        return msg;

    }

    public void setMessage(String message) {
        this.message = message;
    }
}
