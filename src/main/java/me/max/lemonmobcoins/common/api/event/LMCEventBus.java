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
package me.max.lemonmobcoins.common.api.event;

import com.google.common.eventbus.EventBus;

/**
 * Event bus used by the plugin to handle API events
 */
public class LMCEventBus {

    // Instance of the event bus
    private EventBus eventBus = new EventBus();

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
