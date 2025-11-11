package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Deer extends Herbivore{
    public Deer() {
        super();
        setType("Deer");
        setWeight(300.0);
        setMaxCountInCell(20);
        setSpeed(4);
        setMaxFood(50.0);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
