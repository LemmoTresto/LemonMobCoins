/*
 * * Copyright 2018 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.max.lemonmobcoins.common.api.event.shop;

import me.max.lemonmobcoins.common.abstraction.entity.IWrappedPlayer;
import me.max.lemonmobcoins.common.api.event.Cancellable;
import me.max.lemonmobcoins.common.api.event.Event;

/**
 * Fired when the shop is opened
 */
@Cancellable
public class ShopOpenedEvent extends Event {

    // The shop who opened the shop
    private IWrappedPlayer player;

    public ShopOpenedEvent(IWrappedPlayer player) {
        this.player = player;
    }

    public IWrappedPlayer getPlayer() {
        return player;
    }
}
