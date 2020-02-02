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

package me.max.lemonmobcoins.sponge.listeners;

import me.max.lemonmobcoins.common.abstraction.pluginmessaging.AbstractPluginMessageManager;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.gui.ShopItem;
import me.max.lemonmobcoins.common.messages.Messages;
import me.max.lemonmobcoins.common.utils.ColorUtil;
import me.max.lemonmobcoins.sponge.impl.inventory.ItemStackSpongeImpl;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.text.Text;

public class ClickInventoryListener {

    private final CoinManager coinManager;
    private final GuiManager guiManager;
    private final AbstractPluginMessageManager pluginMessageManager;

    public ClickInventoryListener(CoinManager coinManager, GuiManager guiManager, AbstractPluginMessageManager pluginMessageManager) {
        this.coinManager = coinManager;
        this.guiManager = guiManager;
        this.pluginMessageManager = pluginMessageManager;
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event) {
        if (!event.getTargetInventory().getPlugin().getId().equals("lemonmobcoins")) return;
        event.setCancelled(true);

        Player p = (Player) event.getSource();
        ShopItem item = guiManager
                .getShopItem(new ItemStackSpongeImpl(event.getCursorTransaction().getFinal().createStack()));

        if (item.isPermission()) {
            if (!p.hasPermission("lemonmobcoins.buy." + item.getIdentifier())) {
                p.sendMessage(Text.of(Messages.NO_PERMISSION_TO_PURCHASE
                        .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0)));
                return;
            }
        }

        if (!(coinManager.getCoinsOfPlayer(p.getUniqueId()) >= item.getPrice())) {
            p.sendMessage(Text.of(Messages.NOT_ENOUGH_MONEY_TO_PURCHASE
                    .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0)));
            return;
        }

        coinManager.deductCoinsFromPlayer(p.getUniqueId(), item.getPrice());

        if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId());
        p.sendMessage(Text.of(Messages.PURCHASED_ITEM_FROM_SHOP
                .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, item.getPrice())
                .replaceAll("%item%", item.getDisplayname())));
        for (String cmd : item.getCommands())
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ColorUtil
                    .colorize(cmd.replaceAll("%player%", p.getName())));
    }

}
