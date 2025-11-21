package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Mouse extends Herbivore {

    public Mouse() {
        super();
        setType("Mouse");
        setWeight(0.05);
        setInitialWeight(0.05);
        setMaxCountInCell(500);
        setSpeed(1);
        setMaxFood(0.01);
        setFoodTypes(Arrays.asList(Caterpillar.class, Grass.class));
    }

    @Override
    public void reproduce() {
    }
}
