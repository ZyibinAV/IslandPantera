package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Bison  extends Herbivore{

    public Bison() {
        super();
        setType("Bison");
        setWeight(700.0);
        setMaxCountInCell(10);
        setSpeed(3);
        setMaxFood(100.0);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
