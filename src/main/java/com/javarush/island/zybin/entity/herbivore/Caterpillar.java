package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Caterpillar extends Herbivore{

    public Caterpillar() {
        super();
        setType("Caterpillar");
        setWeight(0.01);
        setMaxCountInCell(1000);
        setSpeed(0);
        setMaxFood(0.0);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
