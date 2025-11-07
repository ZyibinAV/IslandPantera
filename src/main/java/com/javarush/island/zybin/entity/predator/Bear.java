package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;

public class Bear extends  Predator {

    public Bear() {
        super();
        setType("Bear");
        setWeight(500.0);
        setMaxCountInCell(5);
        setSpeed(2);
        setMaxFood(80);
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
