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

package me.max.lemonmobcoins.common.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import me.max.lemonmobcoins.common.api.event.balance.PlayerBalanceModifiedEvent;
import me.max.lemonmobcoins.common.api.event.balance.PlayerBalanceModifiedEvent.Type;
import me.max.lemonmobcoins.common.api.event.plugin.PluginReloadedEvent;
import me.max.lemonmobcoins.common.api.event.shop.ShopOpenedEvent;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.messages.Messages;

@SuppressWarnings("unused")
@CommandAlias("mobcoins|mobcoin|mc|mcoin|mcoins|mobc")
public class MobCoinsCommand extends BaseCommand {

    private final CoinManager coinManager;
    private final IWrappedPlatform platform;
    private final GuiManager guiManager;

    public MobCoinsCommand(CoinManager coinManager, IWrappedPlatform platform, GuiManager guiManager) {
        this.coinManager = coinManager;
        this.platform = platform;
        this.guiManager = guiManager;
    }

    @Subcommand("balance|bal")
    @CommandPermission("lemonmobcoins.balance")
    public void onBalance(CommandIssuer issuer, @Optional String player) {
        if (player == null) {
            if (issuer.isPlayer()) {
                issuer.sendMessage(Messages.OWN_PLAYER_BALANCE
                        .getMessage(coinManager.getCoinsOfPlayer(issuer.getUniqueId()), platform
                                .getPlayer(issuer.getUniqueId()).getName(), null, 0));
                return;
            }
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        issuer.sendMessage(Messages.OTHER_PLAYER_BALANCE
                .getMessage(coinManager.getCoinsOfPlayer(offlinePlayer.getUniqueId()), offlinePlayer
                        .getName(), null, 0));
    }

    @Subcommand("set")
    @CommandPermission("lemonmobcoins.admin")
    public void onSet(CommandIssuer issuer, String player, double amount) {
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        PlayerBalanceModifiedEvent playerBalanceModifiedEvent = new PlayerBalanceModifiedEvent(offlinePlayer, coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()), coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()) + amount, Type.SET);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(playerBalanceModifiedEvent)) {
            coinManager.setCoinsOfPlayer(offlinePlayer.getUniqueId(), amount);
            issuer.sendMessage(Messages.SET_PLAYER_BALANCE
                    .getMessage(coinManager.getCoinsOfPlayer(offlinePlayer.getUniqueId()), player, null, 0));
        }
    }

    @Subcommand("give")
    @CommandPermission("lemonmobcoins.admin")
    public void onGive(CommandIssuer issuer, String player, double amount) {
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        PlayerBalanceModifiedEvent playerBalanceModifiedEvent = new PlayerBalanceModifiedEvent(offlinePlayer, coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()), coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()) + amount, Type.GIVEN);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(playerBalanceModifiedEvent)) {
            coinManager.addCoinsToPlayer(offlinePlayer.getUniqueId(), amount);
            issuer.sendMessage(Messages.GIVE_PLAYER_BALANCE
                    .getMessage(coinManager.getCoinsOfPlayer(offlinePlayer.getUniqueId()), player, null, amount));
        }
    }

    @Subcommand("take")
    @CommandPermission("lemonmobcoins.admin")
    public void onTake(CommandIssuer issuer, String player, double amount) {
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        PlayerBalanceModifiedEvent playerBalanceModifiedEvent = new PlayerBalanceModifiedEvent(offlinePlayer, coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()), coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()) + amount, Type.TAKEN);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(playerBalanceModifiedEvent)) {
            coinManager.deductCoinsFromPlayer(offlinePlayer.getUniqueId(), amount);
            issuer.sendMessage(Messages.TAKE_PLAYER_BALANCE
                    .getMessage(coinManager.getCoinsOfPlayer(offlinePlayer.getUniqueId()), player, null, amount));
        }
    }

    @Subcommand("reset")
    @CommandPermission("lemonmobcoins.admin")
    public void onReset(CommandIssuer issuer, String player) {
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        PlayerBalanceModifiedEvent playerBalanceModifiedEvent = new PlayerBalanceModifiedEvent(offlinePlayer, coinManager
                .getCoinsOfPlayer(offlinePlayer.getUniqueId()), 0, Type.RESET);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(playerBalanceModifiedEvent)) {
            coinManager.setCoinsOfPlayer(offlinePlayer.getUniqueId(), 0);
            issuer.sendMessage(Messages.RESET_PLAYER_BALANCE.getMessage(0, player, null, 0));
        }
    }

    @Subcommand("pay")
    @CommandPermission("lemonmobcoins.pay")
    public void onPay(CommandIssuer issuer, String player, double amount) {
        if (!issuer.isPlayer()) {
            issuer.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0));
            return;
        }
        IWrappedOfflinePlayer offlinePlayer = platform.getOfflinePlayer(player);
        if (offlinePlayer == null) {
            issuer.sendMessage(Messages.UNKNOWN_PLAYER.getMessage(0, null, null, 0));
            return;
        }
        if (!(coinManager.getCoinsOfPlayer(issuer.getUniqueId()) >= amount)) {
            issuer.sendMessage(Messages.NOT_ENOUGH_MONEY_TO_PAY
                    .getMessage(coinManager.getCoinsOfPlayer(issuer.getUniqueId()), null, null, 0));
            return;
        }
        coinManager.deductCoinsFromPlayer(issuer.getUniqueId(), amount);

        coinManager.addCoinsToPlayer(offlinePlayer.getUniqueId(), amount);
        issuer.sendMessage(Messages.PAY_PLAYER
                .getMessage(coinManager.getCoinsOfPlayer(issuer.getUniqueId()), player, null, amount));
        if (offlinePlayer.isOnline()) offlinePlayer.getOnlinePlayer().sendMessage(Messages.PAID_BY_PLAYER
                .getMessage(coinManager.getCoinsOfPlayer(offlinePlayer.getUniqueId()), platform
                        .getPlayer(issuer.getUniqueId()).getName(), null, amount));
    }

    @Subcommand("reload")
    @CommandPermission("lemonmobcoins.admin")
    public void onReload(CommandIssuer issuer) {
        try {
            issuer.sendMessage(Messages.START_RELOAD.getMessage(0, null, null, 0));
            platform.disable();
            platform.enable();
            PluginReloadedEvent event = new PluginReloadedEvent();
            LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(event);
            issuer.sendMessage(Messages.SUCCESSFUL_RELOAD.getMessage(0, null, null, 0));
        } catch (Throwable t) {
            issuer.sendMessage(Messages.FAILED_RELOAD.getMessage(0, null, null, 0));
            t.printStackTrace();
        }
    }

    @Subcommand("help|?|h")
    @CommandPermission("lemonmobcoins.help")
    public void onHelp(CommandIssuer issuer) {
        if (issuer.hasPermission("lemonmobcoins.admin")) {
            issuer.sendMessage(Messages.ADMIN_HELP_MENU.getMessage(0, null, null, 0));
            return;
        }
        issuer.sendMessage(Messages.PLAYER_HELP_MENU.getMessage(0, null, null, 0));
    }

    @Subcommand("shop|s")
    @CommandPermission("lemonmobcoins.shop")
    public void onShop(CommandIssuer issuer) {
        IWrappedPlayer player = platform.getPlayer(issuer.getUniqueId());
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(new ShopOpenedEvent(player)))
            player.openInventory(guiManager.getInventory());
    }

    @Default
    @CatchUnknown
    public void onUnknownSubCommand(CommandIssuer issuer) {
        issuer.sendMessage(Messages.UNKNOWN_SUBCOMMAND.getMessage(0, null, null, 0));
    }

}
