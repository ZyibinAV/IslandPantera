package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Sheep extends Herbivore {

    public Sheep() {
        super();
        setType("Sheep");
        setWeight(70.0);
        setMaxCountInCell(140);
        setSpeed(3);
        setMaxFood(15.0);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
