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

package me.max.lemonmobcoins.common.abstraction.platform;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.entity.SpongeWrappedOfflinePlayer;
import me.max.lemonmobcoins.common.abstraction.entity.SpongeWrappedPlayer;
import me.max.lemonmobcoins.common.abstraction.inventory.*;
import me.max.lemonmobcoins.common.gui.ShopItem;
import me.max.lemonmobcoins.sponge.LemonMobCoinsSpongePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpongeWrappedPlatform implements IWrappedPlatform {

    private LemonMobCoinsSpongePlugin plugin;

    public SpongeWrappedPlatform(LemonMobCoinsSpongePlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public IWrappedPlayer[] getOnlinePlayers() {
        List<IWrappedPlayer> players = new ArrayList<>();
        Sponge.getGame().getServer().getOnlinePlayers().forEach(player -> players.add(new SpongeWrappedPlayer(player)));
        return players.toArray(new IWrappedPlayer[0]);
    }

    @Override
    public IWrappedPlayer getPlayer(String name) {
        Optional<Player> player = Sponge.getGame().getServer().getPlayer(name);
        return player.map(SpongeWrappedPlayer::new).orElse(null);
    }

    @Override
    public IWrappedPlayer getPlayer(UUID uuid) {
        Optional<Player> player = Sponge.getGame().getServer().getPlayer(uuid);
        return player.map(SpongeWrappedPlayer::new).orElse(null);
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(UUID uuid) {
        Optional<User> user = getUser(uuid);
        return user.map(SpongeWrappedOfflinePlayer::new).orElse(null);
    }

    @Override
    public IWrappedOfflinePlayer getOfflinePlayer(String name) {
        Optional<User> user = getUser(name);
        return user.map(SpongeWrappedOfflinePlayer::new).orElse(null);
    }

    @Override
    public void enable() {
        plugin.onPreInit(null);
        plugin.onInit(null);
    }

    @Override
    public void disable() {
        plugin.onServerStop(null);
    }

    @Override
    public IWrappedInventory createInventory(String title, int rows, List<ShopItem> items) {
        Inventory.Builder invBuilder = Inventory.builder();
        invBuilder.forCarrier(new SpongeCarrier());
        invBuilder.property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(title)));
        invBuilder.property(InventoryDimension.PROPERTY_NAME, InventoryDimension.of(9, rows));
        Inventory inv = invBuilder.build(plugin);

        for (ShopItem item : items){
            Optional<ItemType> type = Sponge.getRegistry().getType(ItemType.class, item.getMaterial());
            if (!type.isPresent()) continue;
            ItemStack itemStack = ItemStack.of(type.get(), item.getAmount());

            if (item.isGlowing()){
                EnchantmentData enchData = itemStack.getOrCreate(EnchantmentData.class).get();
                enchData.set(enchData.enchantments().add(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
                itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
            }

            itemStack.offer(Keys.DISPLAY_NAME, Text.of(item.getDisplayname()));
            itemStack.offer(Keys.ITEM_LORE, item.getLore().stream().map(Text::of).collect(Collectors.toList()));
            inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(item.getSlot()))).first().set(itemStack);
        }

        return new SpongeWrappedInventory(inv);

    }

    @Override
    public IWrappedItemStack toItemStack(ShopItem item) {
        Optional<ItemType> type = Sponge.getRegistry().getType(ItemType.class, item.getMaterial());
        if (! type.isPresent()) return null;
        ItemStack itemStack = ItemStack.of(type.get(), item.getAmount());

        if (item.isGlowing()) {
            EnchantmentData enchData = itemStack.getOrCreate(EnchantmentData.class).get();
            enchData.set(enchData.enchantments().add(Enchantment.of(EnchantmentTypes.UNBREAKING, 1)));
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
        }

        itemStack.offer(Keys.DISPLAY_NAME, Text.of(item.getDisplayname()));
        itemStack.offer(Keys.ITEM_LORE, item.getLore().stream().map(Text::of).collect(Collectors.toList()));
        return new SpongeWrappedItemStack(itemStack);
    }

    private Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.flatMap(userStorage1 -> userStorage1.get(uuid));
    }

    private Optional<User> getUser(String name) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.flatMap(userStorage1 -> userStorage1.get(name));
    }
}
