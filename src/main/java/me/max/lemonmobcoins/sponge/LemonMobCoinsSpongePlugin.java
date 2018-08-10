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

package me.max.lemonmobcoins.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "lemonmobcoins", name = "LemonMobCoins", version = "1.5", authors = "LemmoTresto")
public class LemonMobCoinsSpongePlugin {

    //So I do not understand why there are 20 different onEnable methods. Every method should be used for something different don't really understand this.

    //The GameConstructionEvent is triggered. During this state, the @Plugin class instance for each plugin is triggered.
    @Listener
    public void onConstruct(GameConstructionEvent event){

    }

    //The GamePreInitializationEvent is triggered. During this state, the plugin gets ready for initialization. Access to a default logger instance and access to information regarding preferred configuration file locations is available.
    @Listener
    public void onPreInit(GamePreInitializationEvent event){

    }

    //The GameInitializationEvent is triggered. During this state, the plugin should finish any work needed in order to be functional. Global event handlers should get registered in this stage.
    @Listener
    public void onInit(GameInitializationEvent event){

    }

    //The GamePostInitializationEvent is triggered. By this state, inter-plugin communication should be ready to occur. Plugins providing an API should be ready to accept basic requests.
    @Listener
    public void onPostInit(GamePostInitializationEvent event){

    }

    //The GameLoadCompleteEvent is triggered. By this state, all plugin initialization should be completed.
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event){

    }
}
