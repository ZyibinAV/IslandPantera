package com.javarush.island.zybin.entities.plants;

import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.controllers.ReproductionController;

/**
 * Класс, представляющий траву — растение на острове.
 * Реализует интерфейс LivingEntity.
 */
public class Grass implements LivingEntity {

    private String type = "Grass";
    private double weight = 1.0;
    private int maxCountInCell = 200;
    private boolean isAlive;

    private ReproductionController reproductionController;

    public Grass() {
        this.isAlive = true;
    }

    public String getType() {
        return type;
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

    public void setReproductionController(ReproductionController controller) {
        this.reproductionController = controller;
    }


    @Override
    public void reproduce() {
        if (reproductionController == null) {
            throw new IllegalStateException("ReproductionController не установлен.");
        }
        reproductionController.reproduceGrass(this);
    }
}
