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
import me.max.lemonmobcoins.common.gui.ShopItem;

/**
 * Fired when the player purchases an item
 */
@Cancellable
public class PlayerPurchaseItemEvent extends Event {

    private IWrappedPlayer player;

    private ShopItem item;

    public PlayerPurchaseItemEvent(IWrappedPlayer player, ShopItem item) {
        this.player = player;
        this.item = item;
    }

    public IWrappedPlayer getPlayer() {
        return player;
    }

    public ShopItem getItem() {
        return item;
    }
}
