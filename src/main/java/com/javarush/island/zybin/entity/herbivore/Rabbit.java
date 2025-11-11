package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Rabbit extends Herbivore {

    public Rabbit() {
        super();
        setType("Rabbit");
        setWeight(2.0);
        setMaxCountInCell(150);
        setSpeed(2);
        setMaxFood(0.45);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
