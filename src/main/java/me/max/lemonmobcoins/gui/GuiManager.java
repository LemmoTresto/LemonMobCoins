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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiManager {

    private int rows;
    private String command;
    private String title;
    private List<GuiMobCoinItem> items;

    public GuiManager(FileConfiguration config){
        rows = config.getInt("gui.rows");
        command = config.getString("gui.command");
        title = ChatColor.translateAlternateColorCodes('&', config.getString("gui.name"));
        items = new ArrayList<>();

        for (String key : config.getConfigurationSection("gui.items").getKeys(false)){
            ConfigurationSection item = config.getConfigurationSection("gui.items." + key);
            items.add(new GuiMobCoinItem.Builder(key)
                    .setAmount(item.getInt("amount"))
                    .setSlot(item.getInt("slot"))
                    .setMaterial(Material.matchMaterial(item.getString("material")))
                    .setDisplayname(ChatColor.translateAlternateColorCodes('&', item.getString("displayname")))
                    .setGlowing(item.getBoolean("glowing"))
                    .setLore(item.getStringList("lore")
                            .stream()
                            .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                            .collect(Collectors.toList()))
                    .setPermission(item.getBoolean("permission"))
                    .setPrice(item.getDouble("price"))
                    .setCommands(item.getStringList("commands")).build());
        }
    }

    public Inventory getInventory(){
        Inventory inv = Bukkit.createInventory(new GuiHolder(), rows * 9, title);
        items.forEach(item -> inv.setItem(item.getSlot(), item.toItemStack()));
        return inv;
    }

    public GuiMobCoinItem getGuiMobCoinItemFromItemStack(@NotNull ItemStack item) {
        return items.stream().filter(guiMobCoinItem -> guiMobCoinItem.toItemStack().equals(item)).findFirst().orElse(null);
    }

    @NotNull
    public String getCommand() {
        return command;
    }
}
