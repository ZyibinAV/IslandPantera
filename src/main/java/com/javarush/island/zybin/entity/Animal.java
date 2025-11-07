package com.javarush.island.zybin.entity;

/**
 * Абстрактный класс для всех животных.
 * Содержит общие свойства и методы для хищников и травоядных.
 */

import java.util.List;

public abstract class Animal implements LivingEntity {

    private String type; // тип животного (например, "Волк", "Заяц")
    private double weight; // вес животного
    private int maxCountInCell; // максимальное количество животных в ячейке
    private int speed;// скорость передвижения (количество ячеек)
    private int maxFood; // количество еды для насыщения
    private List<Class<? extends LivingEntity>> foodTypes; // типы еды, которую может есть животное
    private boolean isAlive; // статус животного

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

    public int getMaxFood() {
        return maxFood;
    }

    public int getMaxCountInCell() {
        return maxCountInCell;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setMaxCountInCell(int maxCountInCell) {
        this.maxCountInCell = maxCountInCell;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    public List<Class<? extends LivingEntity>> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(List<Class<? extends LivingEntity>> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


    public abstract void eat(LivingEntity food);

    public abstract void choiceMove();
}
