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

package me.max.lemonmobcoins.sponge.listeners;

import me.max.lemonmobcoins.bukkit.PluginMessageManager;
import me.max.lemonmobcoins.bukkit.hooks.PAPIHook;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.files.gui.GuiManager;
import me.max.lemonmobcoins.common.files.gui.ShopItem;
import me.max.lemonmobcoins.common.files.messages.Messages;
import org.bukkit.ChatColor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.text.Text;

public class ClickInventoryListener {

    private final CoinManager coinManager;
    private final GuiManager guiManager;
    private final PluginMessageManager pluginMessageManager;
    private final PAPIHook papiHook;

    public ClickInventoryListener(CoinManager coinManager, GuiManager guiManager, PluginMessageManager pluginMessageManager, PAPIHook papiHook){
        this.coinManager = coinManager;
        this.guiManager = guiManager;
        this.pluginMessageManager = pluginMessageManager;
        this.papiHook = papiHook;
    }

    @Listener
    public void onInventoryClick(ClickInventoryEvent event){
        if (!event.getTargetInventory().getPlugin().getId().equals("lemonmobcoins")) return;
        event.setCancelled(true);

        Player p = (Player) event.getSource();
        ShopItem item = guiManager.getGuiMobCoinItemFromItemStack(event.getCursorTransaction().getFinal().createStack()); //todo

        if (item.isPermission()){
            if (!p.hasPermission("lemonmobcoins.buy." + item.getIdentifier())) {
                p.sendMessage(Text.of(Messages.NO_PERMISSION_TO_PURCHASE.getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0, papiHook)));
                return;
            }
        }

        if (!(coinManager.getCoinsOfPlayer(p.getUniqueId()) >= item.getPrice())){
            p.sendMessage(Text.of(Messages.NOT_ENOUGH_MONEY_TO_PURCHASE.getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, 0, papiHook)));
            return;
        }

        coinManager.deductCoinsFromPlayer(p.getUniqueId(), item.getPrice());
        if (pluginMessageManager != null) pluginMessageManager.sendPluginMessage(p.getUniqueId());
        p.sendMessage(Text.of(Messages.PURCHASED_ITEM_FROM_SHOP.getMessage(coinManager.getCoinsOfPlayer(p.getUniqueId()), p.getName(), null, item.getPrice(), papiHook).replaceAll("%item%", item.getDisplayname())));
        for (String cmd : item.getCommands()) Sponge.getCommandManager().process(Sponge.getServer().getConsole(), ChatColor.translateAlternateColorCodes('&', cmd.replaceAll("%player%", p.getName())));
    }
}
