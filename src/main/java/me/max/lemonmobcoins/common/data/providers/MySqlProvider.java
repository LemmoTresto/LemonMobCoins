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

package me.max.lemonmobcoins.common.data.providers;

import me.max.lemonmobcoins.common.data.DataProvider;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySqlProvider implements DataProvider {

    private final Connection connection;

    public MySqlProvider(String hostname, String port, String username, String password, String database) throws SQLException {
        connection = DriverManager
                .getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
        createTable();
    }

    @Override
    public Map<UUID, Double> loadData() throws SQLException {
        Map<UUID, Double> coins = new HashMap<>();
        ResultSet rs = getCoins();
        while (rs.next()) {
            coins.put(UUID.fromString(rs.getString(1)), rs.getDouble(2));
        }
        rs.close();
        return coins;
    }

    @Override
    public void saveData(Map<UUID, Double> coins) throws SQLException {
        for (Map.Entry<UUID, Double> entry : coins.entrySet()) setCoin(entry.getKey(), entry.getValue());
        connection.close();
    }

    private PreparedStatement prepareStatement(Queries query) throws SQLException {
        return connection.prepareStatement(query.getQuery());
    }

    private void setCoin(UUID uuid, double amount) throws SQLException {
        PreparedStatement stm = prepareStatement(Queries.SET_COIN);
        stm.setString(1, uuid.toString());
        stm.setDouble(2, amount);
        stm.executeUpdate();
        stm.close();
    }

    private ResultSet getCoins() throws SQLException {
        PreparedStatement stm = prepareStatement(Queries.GET_COINS);
        ResultSet rs = stm.executeQuery();
        stm.close();
        return rs;
    }

    private void createTable() throws SQLException {
        PreparedStatement stm = prepareStatement(Queries.CREATE_TABLE);
        stm.executeUpdate();
        stm.close();
    }

    private enum Queries {
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS balance(uuid VARCHAR(36), amount DOUBLE);"),
        GET_COINS("SELECT * FROM balance;"),
        SET_COIN("INSERT INTO balance(uuid, amount) VALUES(?, ?) ON DUPLICATE KEY UPDATE amount = VALUES(amount);");

        private final String query;

        Queries(String query) {
            this.query = query;
        }

        String getQuery() {
            return query;
        }
    }


}
