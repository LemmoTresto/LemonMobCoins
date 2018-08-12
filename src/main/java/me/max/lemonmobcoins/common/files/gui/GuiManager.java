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

package me.max.lemonmobcoins.common.files.gui;

import com.google.common.reflect.TypeToken;
import me.max.lemonmobcoins.common.abstraction.inventory.IWrappedInventory;
import me.max.lemonmobcoins.common.abstraction.platform.IWrappedPlatform;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GuiManager {

    private final int rows;
    private final String command;
    private final String title;
    private final List<ShopItem> items;
    private final IWrappedPlatform platform;

    public GuiManager(ConfigurationNode config, Logger logger, IWrappedPlatform platform){
        config = config.getNode("gui");
        rows = config.getNode("rows").getInt();
        command = config.getNode("command").getString();
        title = ChatColor.translateAlternateColorCodes('&', config.getNode("name").getString());
        items = new ArrayList<>();
        this.platform = platform;

        for (ConfigurationNode key : config.getNode("items").getChildrenList()){
            ConfigurationNode itemNode = config.getNode("items", key.getString());
            try {
                items.add(new ShopItem.Builder(key.getString())
                        .setAmount(itemNode.getNode("amount").getInt())
                        .setSlot(itemNode.getNode("slot").getInt())
                        .setMaterial(itemNode.getNode("material").getString())
                        .setDisplayname(ChatColor.translateAlternateColorCodes('&', itemNode.getNode("displayname").getString()))
                        .setGlowing(itemNode.getNode("glowing").getBoolean())
                        .setLore(itemNode.getNode("lore").getList(TypeToken.of(String.class))
                                .stream()
                                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                                .collect(Collectors.toList()))
                        .setPermission(itemNode.getNode("permission").getBoolean())
                        .setPrice(itemNode.getNode("price").getDouble())
                        .setCommands(itemNode.getNode("commands").getList(TypeToken.of(String.class))).build());
            } catch (ObjectMappingException e) {
                logger.error("Error mapping config shop items!");
                e.printStackTrace();
            }
        }
    }

    public int getRows() {
        return rows;
    }

    @NotNull
    public List<ShopItem> getItems() {
        return items;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public IWrappedInventory getInventory(){
        return platform.createInventory(title, rows, items);
    }

    public ShopItem getGuiMobCoinItemFromItemStack(@NotNull ItemStack item) {
        return items.stream().filter(guiMobCoinItem -> guiMobCoinItem.equals(item)).findFirst().orElse(null);
    }

    //todo abstract this for sponge too.

    @NotNull
    public String getCommand() {
        return command;
    }
}
