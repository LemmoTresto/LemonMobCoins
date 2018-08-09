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

package me.max.lemonmobcoins.common;

import me.max.lemonmobcoins.common.api.LemonMobCoinsAPI;
import me.max.lemonmobcoins.common.data.CoinManager;
import me.max.lemonmobcoins.common.data.DataProvider;
import me.max.lemonmobcoins.common.exceptions.APILoadException;
import me.max.lemonmobcoins.common.exceptions.DataLoadException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LemonMobCoins {

    private static LemonMobCoinsAPI lemonMobCoinsAPI;
    private CoinManager coinManager;
    private Logger logger;

    public LemonMobCoins(DataProvider dataProvider, Logger logger) throws DataLoadException, APILoadException{
        this.logger = logger;

        try {
            info("Loading data..");
            coinManager = new CoinManager(dataProvider);
            info("Loaded data!");
        } catch (Exception e) {
            throw new DataLoadException(e);
        }

        try {
            info("Loading API..");
            lemonMobCoinsAPI = coinManager;
            info("Loaded API!");
        } catch (Exception e){
            throw new APILoadException(e);
        }
    }

    public void disable() throws IOException, SQLException{
        getCoinManager().saveData();
    }

    private void info(String s){
        log(Level.INFO, s);
    }

    private void log(Level level, String s){
        getLogger().log(level, s);
    }
    public CoinManager getCoinManager() {
        return coinManager;
    }

    @SuppressWarnings("unused")
    public static LemonMobCoinsAPI getLemonMobCoinsAPI() {
        return lemonMobCoinsAPI;
    }

    private Logger getLogger() {
        return logger;
    }
}
