package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;

public class Eagle extends  Predator {

    public Eagle() {
        super();
        setType("Eagle");
        setWeight(6.0);
        setMaxCountInCell(20);
        setSpeed(3);
        setMaxFood(1.0);
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
