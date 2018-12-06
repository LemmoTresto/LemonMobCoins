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

package me.max.lemonmobcoins.bukkit.listeners;

import me.max.lemonmobcoins.common.LemonMobCoins;
import me.max.lemonmobcoins.common.abstraction.entity.BukkitWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.inventory.BukkitHolder;
import me.max.lemonmobcoins.common.abstraction.inventory.BukkitWrappedItemStack;
import me.max.lemonmobcoins.common.api.event.shop.PlayerPurchaseItemEvent;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.gui.GuiManager;
import me.max.lemonmobcoins.common.gui.ShopItem;
import me.max.lemonmobcoins.common.messages.Messages;
import me.max.lemonmobcoins.common.pluginmessaging.AbstractPluginMessageManager;
import me.max.lemonmobcoins.common.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private final CoinManager coinManager;
    private final GuiManager guiManager;
    private final AbstractPluginMessageManager pluginMessageManager;

    public InventoryClickListener(CoinManager coinManager, GuiManager guiManager, AbstractPluginMessageManager pluginMessageManager) {
        this.coinManager = coinManager;
        this.guiManager = guiManager;
        this.pluginMessageManager = pluginMessageManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (!(event.getInventory().getHolder() instanceof BukkitHolder)) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        ShopItem item = guiManager.getShopItem(new BukkitWrappedItemStack(event.getCurrentItem()));

        if (item.isPermission()) {
            if (!p.hasPermission("lemonmobcoins.buy." + item.getIdentifier())) {
                p.sendMessage(Messages.NO_PERMISSION_TO_PURCHASE
                        .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0));
                return;
            }
        }

        if (!(coinManager.getCoinsOfPlayer(p.getUniqueId()) >= item.getPrice())) {
            p.sendMessage(Messages.NOT_ENOUGH_MONEY_TO_PURCHASE
                    .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0));
            return;
        }

        PlayerPurchaseItemEvent purchaseItemEvent = new PlayerPurchaseItemEvent(new BukkitWrappedPlayer(p), item);
        if (!LemonMobCoins.getLemonMobCoinsAPI().getEventBus().post(purchaseItemEvent)) {
            coinManager.deductCoinsFromPlayer(p.getUniqueId(), item.getPrice());
            if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId());
            p.sendMessage(Messages.PURCHASED_ITEM_FROM_SHOP
                    .getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, item.getPrice())
                    .replaceAll("%item%", item.getDisplayname()));
            for (String cmd : item.getCommands())
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ColorUtil
                        .colorize(cmd.replaceAll("%player%", p.getName())));
        }
    }

}
