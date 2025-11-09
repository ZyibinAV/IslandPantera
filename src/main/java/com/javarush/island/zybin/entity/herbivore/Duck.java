package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Duck extends Herbivore{

    public Duck() {
        super();
        setType("Duck");
        setWeight(1.0);
        setMaxCountInCell(200);
        setSpeed(4);
        setMaxFood(0.15);
    }

    @Override
    public void eat(LivingEntity food) {

    }

    @Override
    public void choiceMove() {

    }

    @Override
    public void reproduction() {

    }
}
