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

/**
 * Base Event class that all other events are derived from
 */
@SuppressWarnings("WeakerAccess")
public abstract class Event {

    // Whether the event was canceled or not
    private boolean canceled;

    /**
     * Determines whether the event is cancelable or not.
     * The value is determined on whether the event class is annotated with
     * {@link Cancellable} or not
     *
     * @return True if the event can be cancelled using {@link #setCanceled(boolean)} or not
     */
    public boolean isCancelable() {
        return this.getClass().isAnnotationPresent(Cancellable.class);
    }

    /**
     * Determine if this event is canceled and should stop executing.
     *
     * @return The current canceled state
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * Sets the cancel state of this event. Note, not all command are cancelable, and any attempt to
     * invoke this method on an event that is not cancelable will be ignored
     * <p>
     * The functionality of setting the canceled state is defined on a per-event bases.
     *
     * @param canceled The new canceled value
     */
    public void setCanceled(boolean canceled) {
        if (!isCancelable()) return;
        this.canceled = canceled;
    }

}
