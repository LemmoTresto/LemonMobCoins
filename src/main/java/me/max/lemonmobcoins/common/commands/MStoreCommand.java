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
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import me.max.lemonmobcoins.common.api.event.shop.ShopOpenedEvent;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.messages.Messages;

@SuppressWarnings("unused")
@CommandAlias("mstore|mshop")
@CommandPermission("lemonmobcoins.shop")
public class MStoreCommand extends BaseCommand {

    private final IWrappedPlatform platform;
    private final GuiManager guiManager;

    public MStoreCommand(IWrappedPlatform platform, GuiManager guiManager) {
        this.platform = platform;
        this.guiManager = guiManager;
    }

    @Default
    @CatchUnknown
    public void onShop(CommandIssuer issuer) {
        if (!issuer.isPlayer()) {
            issuer.sendMessage(Messages.CONSOLE_CANNOT_USE_COMMAND.getMessage(0, null, null, 0));
            return;
        }
        IWrappedPlayer player = platform.getPlayer(issuer.getUniqueId());
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(new ShopOpenedEvent(player)))
            player.openInventory(guiManager.getInventory());

    }

}
