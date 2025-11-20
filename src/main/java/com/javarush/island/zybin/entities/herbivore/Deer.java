package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Deer extends Herbivore{
    public Deer() {
        super();
        setType("Deer");
        setWeight(300.0);
        setInitialWeight(300.0);
        setMaxCountInCell(20);
        setSpeed(4);
        setMaxFood(50.0);
        setFoodTypes(Arrays.asList(Grass.class));
    }

    @Override
    public void reproduce() {
    }
}
