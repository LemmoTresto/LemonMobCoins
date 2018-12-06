/*
 *
 *  *
 *  *  * MobCoins - Earn balance for killing mobs.
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

package me.max.lemonmobcoins.common.api.event;

import com.google.common.eventbus.EventBus;

/**
 * Event bus used by the plugin to handle API events
 */
public class LMCEventBus {

    // Instance of the event bus
    private EventBus eventBus;

    public LMCEventBus() {
        eventBus = new EventBus();
    }

    /**
     * Registers a specific listener
     *
     * @param listener Listener to register
     */
    public void register(Object listener) {
        eventBus.register(listener);
    }

    /**
     * Unregisters a specific listener
     *
     * @param listener Listener to unregister
     */
    public void unregister(Object listener) {
        eventBus.unregister(listener);
    }

    /**
     * Posts a specific event to tag all its listeners
     *
     * @param event Event to tag and post
     * @return True if the event was canceled, false if otherwise
     */
    public boolean post(Event event) {
        eventBus.post(event);
        return event.isCanceled();
    }

}
