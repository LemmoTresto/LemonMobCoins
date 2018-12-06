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
    public boolean isCancellable() {
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
        if (!isCancellable()) return;
        this.canceled = canceled;
    }

}
