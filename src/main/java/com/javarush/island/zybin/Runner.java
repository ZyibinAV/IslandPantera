package com.javarush.island.zybin;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.entity.plants.Grass;

public class Runner {
    public static void main(String[] args) {
        Island island = new Island(100, 20);
        Cell cell = new Cell(100, 20);
        Grass grass = new Grass();
        cell.addEntity(grass);

        System.out.println(cell);
        System.out.println(cell.getCountByType(Grass.class));
    }
}
