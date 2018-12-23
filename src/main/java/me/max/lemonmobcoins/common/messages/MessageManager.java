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

package me.max.lemonmobcoins.common.messages;

import me.max.lemonmobcoins.common.utils.FileUtil;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class MessageManager {

    public MessageManager() {
        throw new UnsupportedOperationException("Instantiation of this class is not allowed.");

    }

    public static void load(@NonNull File dataFolder, @NonNull Logger logger) {
        File file = new File(dataFolder, "messages.yml");
        if (!file.exists()) {
            try {
                logger.info("No files file found, creating one now..");
                FileUtil.saveResource("messages.yml", dataFolder, "messages.yml");
                logger.info("Created files file!");
            } catch (IOException e) {
                logger.error("Could not create files!");
                e.printStackTrace();
            }
        }

        try {
            YAMLConfigurationLoader.builder().setFile(file).build().load().getChildrenMap()
                                   .forEach((key, value) -> Messages.valueOf(String.valueOf(key))
                                                                    .setMessage(value.getString()));
        } catch (IOException e) {
            logger.error("Could not load files file!");
            e.printStackTrace();
        }
    }

}
