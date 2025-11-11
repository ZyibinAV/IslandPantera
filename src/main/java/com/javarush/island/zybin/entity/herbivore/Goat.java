package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Goat extends Herbivore{

    public Goat() {
        super();
        setType("Goat");
        setWeight(60.0);
        setMaxCountInCell(140);
        setSpeed(3);
        setMaxFood(10);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
