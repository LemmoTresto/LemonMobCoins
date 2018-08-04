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

import me.max.lemonmobcoins.coins.CoinManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public enum Messages {
    RECEIVED_COINS_FROM_KILL("&fYou received &e%amount% &fMobCoins for killing &e%entity%&f, your total balance is &e%balance%"),
    PURCHASED_ITEM_FROM_SHOP("&fYou bought &e%item% &ffor &e%amount%&f, the balance left is &e%balance%"),
    NOT_ENOUGH_MONEY_TO_PURCHASE("&fYou do not have enough funds to buy this! Your current balance is &e%balance%"),
    NO_PERMISSION_TO_PURCHASE("&cYou do not have permission to buy this!"),
    UNKNOWN_SUBCOMMAND("&fUnknown subcommand, try &e/mobcoins help"),
    ADMIN_HELP_MENU("&e/mobcoins help &8- &fDisplay this menu\n&e/mobcoins balance &8- &fCheck your balance\n&e/mobcoins balance [name] &8- &fCheck somebody else balance\n&e/mobcoins shop\n&e/mshop &8- &fOpen the shop\n&e/mc give [name] [amount] &8- &fGive mob coins to a player\n&e/mc take [name] [amount] &8- &fTake mob coins from a player\n&e/mc set [name] [amount] &8- &fSet the balance of a player"),
    PLAYER_HELP_MENU("&e/mobcoins help &8- &fDisplay this menu\n&e/mobcoins balance &8- &fCheck your balance\n&e/mobcoins balance [name] &8- &fCheck somebody else balance\n&e/mobcoins shop\n&e/mshop &8- &fOpen the shop"),
    OWN_PLAYER_BALANCE("&fYour balance is &e%balance%"),
    OTHER_PLAYER_BALANCE("&e%player%'s &fbalance is &e%balance%"),
    CONSOLE_CANNOT_USE_COMMAND("Console cannot use this command!"),
    UNKNOWN_PLAYER("&fUnknown player."),
    NO_PERMISSION_TO_EXECUTE("&cYou don't have permission to execute this command!"),
    SET_PLAYER_BALANCE("&fSuccessfully set &e%player%'s &fbalance to &e%balance%"),
    TAKE_PLAYER_BALANCE("&fSuccessfully taken &e%amount &fcoins from &e%player%'s &fbalance."),
    GIVE_PLAYER_BALANCE("&fSuccessfully given &e%amount% &fto &e%player%");

    private String message;

    Messages(String message){
        this.message = message;
    }

    @NotNull
    public String getMessage(CoinManager coinManager, OfflinePlayer p, Entity e, double received_coins) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);
        if (p != null) msg = msg.replaceAll("%player%", p.getName());
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
            String coins = String.valueOf(coinManager.getCoinsOfPlayer(p));
            if (coins.endsWith(".0")){
                msg = msg.replaceAll("%balance%", coins.substring(0, coins.length() - 2));
            } else {
                msg = msg.replaceAll("%balance%", coins);
            }
        }
        return msg;

    }

    public void setMessage(@NotNull String message) {
        this.message = message;
    }
}
