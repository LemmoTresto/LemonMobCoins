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

package me.max.lemonmobcoins.common.utils;

import me.max.lemonmobcoins.bukkit.LemonMobCoinsBukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

public final class FileUtil {

    public static void saveResource(@NotNull String resource, @NotNull File dataFolder, @NotNull String file) throws IOException {
        final LemonMobCoinsBukkitPlugin plugin = JavaPlugin.getPlugin(LemonMobCoinsBukkitPlugin.class);

        try {
            dataFolder.mkdir();

            final InputStream resourceStream = plugin.getResource(resource);
            Files.copy(resourceStream, new File(dataFolder, file).toPath());
        } catch (FileAlreadyExistsException ignored) {
        }
    }

}
