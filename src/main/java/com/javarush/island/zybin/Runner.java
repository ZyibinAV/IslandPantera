package com.javarush.island.zybin;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

public class Runner {
    public static void main(String[] args) {
        Grass grass = new Grass();
        grass.setAlive(false);
        System.out.println(grass.isAlive());
    }
}
