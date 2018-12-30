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

package me.max.lemonmobcoins.bukkit.impl.platform;

import me.max.lemonmobcoins.bukkit.LemonMobCoinsBukkitPlugin;
import me.max.lemonmobcoins.bukkit.impl.entity.OfflinePlayerBukkitImpl;
import me.max.lemonmobcoins.bukkit.impl.entity.PlayerBukkitImpl;
import me.max.lemonmobcoins.bukkit.impl.inventory.InventoryBukkitImpl;
import me.max.lemonmobcoins.bukkit.impl.inventory.InventoryHolderBukkitImpl;
import me.max.lemonmobcoins.bukkit.impl.inventory.ItemStackBukkitImpl;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedInventory;
import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedItemStack;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import me.max.lemonmobcoins.common.gui.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlatformBukkitImpl implements IWrappedPlatform {

    private final LemonMobCoinsBukkitPlugin plugin;

    public PlatformBukkitImpl(LemonMobCoinsBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public IWrappedPlayer[] getOnlinePlayers() {
        List<IWrappedPlayer> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(o -> players.add(new PlayerBukkitImpl(o)));
        return players.toArray(new IWrappedPlayer[0]);
    }

    @Override
    public IWrappedPlayer getPlayer(String name) {
        return new PlayerBukkitImpl(Bukkit.getPlayer(name));
    }

    @Override
    public IWrappedPlayer getPlayer(UUID uuid) {
        return new PlayerBukkitImpl(Bukkit.getPlayer(uuid));
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(UUID uuid) {
        return new OfflinePlayerBukkitImpl(Bukkit.getOfflinePlayer(uuid));
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(String name) {
        return new OfflinePlayerBukkitImpl(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public void enable() {
        plugin.onEnable();
    }

    @Override
    public void disable() {
        plugin.onDisable();
    }

    @Override
    public IWrappedInventory createInventory(String title, int rows, List<ShopItem> items) {
        Inventory inv = Bukkit.createInventory(new InventoryHolderBukkitImpl(), rows, title);
        for (ShopItem item : items) {
            inv.setItem(item.getSlot(), (ItemStack) toItemStack(item));
        }
        return new InventoryBukkitImpl(inv);
    }

    @Override
    public IWrappedItemStack toItemStack(ShopItem item) {
        ItemStack itemStack = new ItemStack(Material.matchMaterial(item.getMaterial()), item.getAmount());
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(item.getDisplayname());
        meta.setLore(item.getLore());
        if (item.isGlowing()) {
            meta.addEnchant(Enchantment.KNOCKBACK, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        itemStack.setItemMeta(meta);
        return new ItemStackBukkitImpl(itemStack);
    }

}
