package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;

public class Python extends  Predator {

    public Python() {
        super();
        setType("Python");
        setWeight(15.0);
        setMaxCountInCell(30);
        setSpeed(1);
        setMaxFood(3);
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
