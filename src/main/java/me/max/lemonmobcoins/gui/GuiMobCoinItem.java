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

package me.max.lemonmobcoins.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiMobCoinItem {

    private String identifier;
    private int slot;
    private Material material;
    private int amount;
    private String displayname;
    private boolean glowing;
    private List<String> lore;
    private boolean permission;
    private double price;
    private List<String> commands;

    GuiMobCoinItem(@NotNull String identifier, int slot, @NotNull Material material, int amount, @NotNull String displayname, boolean glowing, @NotNull List<String> lore, boolean permission, double price, @NotNull List<String> commands){
        this.identifier = identifier;
        this.slot = slot;
        this.material = material;
        this.amount = amount;
        this.displayname = displayname;
        this.glowing = glowing;
        this.lore = lore;
        this.permission = permission;
        this.price = price;
        this.commands = commands;
    }

    @Contract("-> new")
    ItemStack toItemStack(){
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(lore);
        if (glowing) itemMeta.addEnchant(Enchantment.KNOCKBACK, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull public String getDisplayname() {
        return displayname;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public boolean isPermission() {
        return permission;
    }

    public double getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    @NotNull
    public List<String> getLore() {
        return lore;
    }

    @NotNull
    public List<String> getCommands() {
        return commands;
    }

    @NotNull
    public Material getMaterial() {
        return material;
    }

    public String getIdentifier() {
        return identifier;
    }

    static class Builder {
        private String identifier;
        private int slot;
        private Material material;
        private int amount;
        private String displayname;
        private boolean glowing;
        private List<String> lore;
        private boolean permission;
        private double price;
        private List<String> commands;

        Builder(String identifier){
            this.identifier = identifier;
        }

        @Contract("_ -> this")
        Builder setDisplayname(String displayname) {
            this.displayname = displayname;
            return this;
        }

        @Contract("_ -> this")
        Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        @Contract("_ -> this")
        Builder setPermission(boolean permission) {
            this.permission = permission;
            return this;
        }

        @Contract("_ -> this")
        Builder setCommands(List<String> commands) {
            this.commands = commands;
            return this;
        }

        @Contract("_ -> this")
        Builder setGlowing(boolean glowing) {
            this.glowing = glowing;
            return this;
        }

        @Contract("_ -> this")
        Builder setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        @Contract("_ -> this")
        Builder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        @Contract("_ -> this")
        Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        @Contract("_ -> this")
        Builder setSlot(int slot) {
            this.slot = slot;
            return this;
        }

        @Contract("-> new")
        GuiMobCoinItem build(){
            return new GuiMobCoinItem(identifier, slot, material, amount, displayname, glowing, lore, permission, price, commands);
        }
    }
}
