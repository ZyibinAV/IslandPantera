package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;

public class Wolf extends Predator{

    public Wolf() {
        super();
        setType("Wolf");
        setWeight(50.0);
        setMaxCountInCell(30);
        setSpeed(3);
        setMaxFood(8.0);
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
