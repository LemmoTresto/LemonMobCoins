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
    RECEIVED_COINS_FROM_KILL("&7You received &e%amount% &7MobCoins for killing &e%entity%&7, your total balance is &e%balance%"),
    PURCHASED_ITEM_FROM_SHOP("&7You bought &e%item% &7for &e%amount%&7, the balance left is &e%balance%"),
    NOT_ENOUGH_MONEY_TO_PURCHASE("&7You do not have enough funds to buy this! Your current balance is &e%balance%"),
    NO_PERMISSION_TO_PURCHASE("&cYou do not have permission to buy this!"),
    UNKNOWN_SUBCOMMAND("&7Unknown subcommand, try &e/mobcoins help"),
    ADMIN_HELP_MENU("&e/mobcoins help &8- &7Display this menu\n" +
            "&e/mobcoins balance &8- &7Check your balance\n" +
            "&e/mobcoins balance [name] &8- &7Check somebody else balance\n" +
            "&e/mobcoins shop\n" +
            "&e/mshop &8- &7Open the shop\n" +
            "&e/mc give [name] [amount] &8- &7Give mob coins to a player\n" +
            "&e/mc take [name] [amount] &8- &7Take mob coins from a player\n" +
            "&e/mc set [name] [amount] &8- &7Set the balance of a player"),
    PLAYER_HELP_MENU("&e/mobcoins help &8- &7Display this menu\n" +
            "&e/mobcoins balance &8- &7Check your balance\n" +
            "&e/mobcoins balance [name] &8- &7Check somebody else balance\n" +
            "&e/mobcoins shop\n" +
            "&e/mshop &8- &7Open the shop"),
    OWN_PLAYER_BALANCE("&7Your balance is &e%balance%"),
    OTHER_PLAYER_BALANCE("&e%player%'s &7balance is &e%balance%"),
    CONSOLE_CANNOT_USE_COMMAND("Console cannot use this command!"),
    UNKNOWN_PLAYER("&7Unknown player."),
    NO_PERMISSION_TO_EXECUTE("&cYou don't have permission to execute this command!"),
    SET_PLAYER_BALANCE("&7Successfully set &e%player%'s &7balance to &e%balance%"),
    TAKE_PLAYER_BALANCE("&7Successfully taken &e%amount &7coins from &e%player%'s &7balance."),
    GIVE_PLAYER_BALANCE("&7Successfully given &e%amount% &7to &e%player%");

    private String message;

    Messages(String message){
        this.message = message;
    }

    public String getMessage(LemonMobCoins lemonMobCoins, OfflinePlayer p, Entity e, double received_coins) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);
        if (p != null) msg = msg.replaceAll("%player%", p.getName());
        if (e != null) msg = msg.replaceAll("%entity%", e.getName());
        if (received_coins != 0) msg = msg.replaceAll("%amount%", String.valueOf(received_coins));
        if (received_coins != 0){
            if (String.valueOf(received_coins).split(".")[1].equalsIgnoreCase("0")){
                msg = msg.replaceAll("%balance%", String.valueOf(received_coins).split(".")[0]);
            }
        }
        if (p != null){
            double amt = lemonMobCoins.getCoinManager().getCoinsOfPlayer(p);
            if (String.valueOf(amt).split(".")[1].equalsIgnoreCase("0")){
                msg = msg.replaceAll("%balance%", String.valueOf(amt).split(".")[0]);
            }
        }
        return msg;

    }

    public void setMessage(String message) {
        this.message = message;
    }
}
