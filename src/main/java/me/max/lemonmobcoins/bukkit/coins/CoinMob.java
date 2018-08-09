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

package me.max.lemonmobcoins.bukkit.coins;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CoinMob {

    private EntityType mob;
    private int chance;

    //In case they do amount: "1-5"
    //amount will be 1 and amount2 will be 5.
    //If amount2 is zero then they are not using something random
    private int amount;
    private int amount2;

    private Random r = new Random();

    public CoinMob(@NotNull EntityType mob, int chance, int amount, int amount2){
        this.mob = mob;
        this.chance = chance;
        this.amount = amount;
        this.amount2 = amount2;
    }

    @NotNull
    public EntityType getMob() {
        return mob;
    }

    private int getAmount() {
        return amount;
    }

    private int getAmount2(){
        return amount2;
    }

    private int getChance() {
        return chance;
    }

    private boolean willDropCoins(){
        return r.nextInt(101) < (getChance());
    }

    public int getAmountToDrop(){
        if (!willDropCoins()) return 0;

        //Not using random amount
        if (getAmount2() == 0) return getAmount();

        //Get a random number between 2 integers.
        return r.nextInt(amount2 + 1 - amount) + amount;
    }
}
