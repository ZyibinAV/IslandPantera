package com.javarush.island.zybin.entity;

/**
 * Абстрактный класс для всех животных.
 * Содержит общие свойства и методы для хищников и травоядных.
 */

import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.services.FeedingController;
import com.javarush.island.zybin.services.MovementController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Animal implements LivingEntity {

    private String type; // тип животного (например, "Волк", "Заяц")
    private double weight; // вес животного
    private double initialWeight; // изначальный вес (для расчётов)
    private int maxCountInCell; // максимальное количество животных в ячейке
    private int speed;// скорость передвижения (количество ячеек)
    private double maxFood; // количество еды для насыщения
    private List<Class<? extends LivingEntity>> foodTypes; // типы еды, которую может есть животное
    private boolean isAlive; // статус животного

    // --- Обновляем на AtomicInteger ---
    private final AtomicInteger currentFoodCount = new AtomicInteger(0);
    // добавим контроллеры
    private MovementController movementController;
    private FeedingController feedingController;

    public Animal() {

        this.isAlive = true;
    }

    public String getType() {
        return type;
    }

    public double getWeight() {
        return weight;
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

    // Устанавливаем контроллер извне (например, в симуляции)
    public void setMovementController(MovementController controller) {
        this.movementController = controller;
    }
    public void setFeedingController(FeedingController controller) {
        this.feedingController = controller;
    }

    // Метод, который будет вызываться симуляцией
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
        feedingController.feedAnimal( this, currentCell);
    }



    // --- Метод для уменьшения веса за ход (голод) ---
    public void reduceWeight() {
        if(isAlive) {
            double weightLoss = initialWeight * 0.1; // 10% от изначального веса
            weight -= weightLoss;
            if (weight <= 0) {
                weight = 0;
                setAlive(false);
            }
        }
    }

    // --- Метод для добавления веса при поедании ---
    public void gainWeight(double foodWeight) {
        if (isAlive) {
            weight += foodWeight;
            if (weight > initialWeight) {
                weight = initialWeight;// вес не может превышать изначальный
            }
            currentFoodCount.incrementAndGet(); // потокобезопасное увеличение
        }
    }

    public boolean isFull() {
        return currentFoodCount.get() >= maxFood;
    }

    public void resetFoodCount() {
        currentFoodCount.set(0); // потокобезопасный сброс
    }

}
