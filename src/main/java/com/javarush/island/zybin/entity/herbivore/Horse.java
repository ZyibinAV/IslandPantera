package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Horse extends Herbivore {
    public Horse() {
        super();
        setType("Horse");
        setWeight(400.0);
        setMaxCountInCell(20);
        setSpeed(4);
        setMaxFood(60.0);
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


