package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;

public class Mouse extends Herbivore{

    public Mouse() {
        super();
        setType("Mouse");
        setWeight(0.05);
        setMaxCountInCell(500);
        setSpeed(1);
        setMaxFood(0.01);
    }

    @Override
    public void eat(LivingEntity food) {

    }



    @Override
    public void reproduction() {

    }
}
