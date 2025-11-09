package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Boar extends Herbivore{

    public Boar() {
        super();
        setType("Boar");
        setWeight(400.0);
        setMaxCountInCell(50);
        setSpeed(2);
        setMaxFood(50);
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
