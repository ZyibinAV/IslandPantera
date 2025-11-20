package com.javarush.island.zybin.entities;



import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.controllers.FeedingController;
import com.javarush.island.zybin.controllers.MovementController;
import com.javarush.island.zybin.controllers.ReproductionController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal implements LivingEntity {

    private String type;
    private double weight;
    private double initialWeight;
    private int maxCountInCell;
    private int speed;
    private double maxFood;
    private List<Class<? extends LivingEntity>> foodTypes;
    private boolean isAlive;

    private final AtomicInteger currentFoodCount = new AtomicInteger(0);
    private MovementController movementController;
    private FeedingController feedingController;
    private ReproductionController reproductionController;

    public Animal() {

        this.isAlive = true;
    }

    public String getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    public int getSpeed() {
        return speed;
    }

    public double getMaxFood() {
        return maxFood;
    }

    public int getMaxCountInCell() {
        return maxCountInCell;
    }

    public List<Class<? extends LivingEntity>> getFoodTypes() {
        return foodTypes;
    }

    public boolean isAlive() {
        return isAlive;
    }


    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setInitialWeight(double initialWeight) {
        this.initialWeight = initialWeight;
    }

    public void setMaxCountInCell(int maxCountInCell) {
        this.maxCountInCell = maxCountInCell;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setMaxFood(double maxFood) {
        this.maxFood = maxFood;
    }

    public void setFoodTypes(List<Class<? extends LivingEntity>> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setMovementController(MovementController controller) {
        this.movementController = controller;
    }

    public void setFeedingController(FeedingController controller) {
        this.feedingController = controller;
    }

    public void setReproductionController(ReproductionController controller) {
        this.reproductionController = controller;
    }

    public void move(Cell currentCell) {
        if (movementController == null) {
            throw new IllegalStateException("MovementController не установлен. Сначала вызовите setMovementController.");
        }
        movementController.moveAnimal(this, currentCell);
    }

    public void eat(Cell currentCell) {
        if (feedingController == null) {
            throw new IllegalStateException("FeedingController не установлен. Сначала вызовите setFeedingController.");
        }
        feedingController.feedAnimal(this, currentCell);
    }

    @Override
    public void reproduce() {
        if (reproductionController == null) {
            throw new IllegalStateException("ReproductionController не установлен.");
        }
    }

    public void reduceWeight(double lossPercent) {
        if (isAlive) {
            double weightLoss = initialWeight * lossPercent;
            weight -= weightLoss;
            if (weight <= 0) {
                weight = 0;
                setAlive(false);
            }
        }
    }

    public void gainWeight(double foodWeight) {
        if (isAlive) {
            weight += foodWeight;
            if (weight > initialWeight) {
                weight = initialWeight;
            }
            currentFoodCount.incrementAndGet();
        }
    }

    public boolean isFull() {
        return currentFoodCount.get() >= maxFood;
    }

    public void resetFoodCount() {
        currentFoodCount.set(0);
    }

}
