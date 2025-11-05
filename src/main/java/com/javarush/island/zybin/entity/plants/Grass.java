package com.javarush.island.zybin.entity.plants;

import com.javarush.island.zybin.entity.LivingEntity;

public class Grass implements LivingEntity {

    private final String name = "Grass";
    private final double weight = 1.0;
    private final int maxCountInCell = 200;
    private boolean isAlive;

    public Grass() {
          this.isAlive = true;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxCountInCell() {
        return maxCountInCell;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public void reproduction() {

    }
}
