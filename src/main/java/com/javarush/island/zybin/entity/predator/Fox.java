package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;

public class Fox extends  Predator{

    public Fox() {
        super();
        setType("Fox");
        setWeight(8.0);
        setMaxCountInCell(30);
        setSpeed(2);
        setMaxFood(2);
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
