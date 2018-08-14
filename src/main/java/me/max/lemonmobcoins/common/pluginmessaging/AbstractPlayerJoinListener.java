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

package me.max.lemonmobcoins.common.pluginmessaging;

import java.util.Timer;
import java.util.TimerTask;

public class AbstractPlayerJoinListener {

    private final Timer timer = new Timer();
    private final AbstractPluginMessageManager pluginMessageManager;
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            pluginMessageManager.sendPendingPluginMessages();
        }
    };

    protected AbstractPlayerJoinListener(AbstractPluginMessageManager pluginMessageManager) {
        this.pluginMessageManager = pluginMessageManager;
    }

    private AbstractPluginMessageManager getPluginMessageManager() {
        return pluginMessageManager;
    }

    protected void launchTimer() {
        if (getPluginMessageManager().getCache().isEmpty()) return;
        timer.schedule(timerTask, 1500L);
    }

}
