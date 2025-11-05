package com.javarush.island.zybin.entity;


import java.util.ArrayList;
import java.util.List;

public abstract class Animal implements LivingEntity {

    private String name;
    private double weight;
    private int speed;
    private int maxFood;
    private int maxCountInCell;
    private List<Class<? extends  LivingEntity>> foodTypes;
    private boolean isAlive;

    public Animal(String name, double weight, int speed, int maxFood, int maxCountInCell, List<Class<? extends LivingEntity>> foodTypes) {
        this.name = name;
        this.weight = weight;
        this.speed = speed;
        this.maxFood = maxFood;
        this.maxCountInCell = maxCountInCell;
        this.foodTypes = foodTypes;
        this.isAlive = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMaxFood() {
        return maxFood;
    }

    public void setMaxFood(int maxFood) {
        this.maxFood = maxFood;
    }

    public int getMaxCountInCell() {
        return maxCountInCell;
    }

    public void setMaxCountInCell(int maxCountInCell) {
        this.maxCountInCell = maxCountInCell;
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
