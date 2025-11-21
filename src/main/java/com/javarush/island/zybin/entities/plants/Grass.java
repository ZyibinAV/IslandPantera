package com.javarush.island.zybin.entities.plants;

import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.controllers.ReproductionController;


/**
 * Represents grass in the island simulation, serving as a primary food source for herbivores.
 * <p>
 * Grass is a fundamental component of the island's ecosystem, providing energy for herbivorous animals.
 * It has a high reproduction rate and population density compared to other entities.
 *
 * <p>Characteristics:
 * <ul>
 *   <li>Weight: 1.0 unit</li>
 *   <li>Maximum count per cell: 200</li>
 *   <li>Type: Plant</li>
 *   <li>Base entity in the food chain</li>
 * </ul>
 *
 * @see LivingEntity
 * @see ReproductionController
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
            throw new IllegalStateException("ReproductionController is not installed.");
        }
        reproductionController.reproduceGrass(this);
    }
}
