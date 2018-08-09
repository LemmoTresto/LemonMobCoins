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

package me.max.lemonmobcoins.bukkit.listeners;

import me.max.lemonmobcoins.bukkit.PluginMessageManager;
import me.max.lemonmobcoins.bukkit.gui.GuiHolder;
import me.max.lemonmobcoins.bukkit.gui.GuiManager;
import me.max.lemonmobcoins.bukkit.gui.GuiMobCoinItem;
import me.max.lemonmobcoins.bukkit.messages.Messages;
import me.max.lemonmobcoins.common.data.CoinManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private CoinManager coinManager;
    private GuiManager guiManager;
    private PluginMessageManager pluginMessageManager;

    public InventoryClickListener(CoinManager coinManager, GuiManager guiManager, PluginMessageManager pluginMessageManager){
        this.coinManager = coinManager;
        this.guiManager = guiManager;
        this.pluginMessageManager = pluginMessageManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.getCurrentItem() == null) return;
        if (!(event.getInventory().getHolder() instanceof GuiHolder)) return;
        event.setCancelled(true);

        Player p = (Player) event.getWhoClicked();
        GuiMobCoinItem item = guiManager.getGuiMobCoinItemFromItemStack(event.getCurrentItem());

        if (item.isPermission()){
            if (!p.hasPermission("lemonmobcoins.buy." + item.getIdentifier())) {
                p.sendMessage(Messages.NO_PERMISSION_TO_PURCHASE.getMessage(coinManager, p, null, 0));
                return;
            }
        }

        if (!(coinManager.getCoinsOfPlayer(p.getUniqueId()) >= item.getPrice())){
            p.sendMessage(Messages.NOT_ENOUGH_MONEY_TO_PURCHASE.getMessage(coinManager, p, null, 0));
            return;
        }

        coinManager.deductCoinsFromPlayer(p.getUniqueId(), item.getPrice());
        if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId(), coinManager.getCoinsOfPlayer(p.getUniqueId()));
        p.sendMessage(Messages.PURCHASED_ITEM_FROM_SHOP.getMessage(coinManager, p, null, item.getPrice()).replaceAll("%item%", item.getDisplayname()));
        for (String cmd : item.getCommands()) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', cmd.replaceAll("%player%", p.getName())));
    }

}
