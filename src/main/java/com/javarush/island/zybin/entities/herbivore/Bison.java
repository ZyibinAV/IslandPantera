package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Bison  extends Herbivore{

    public Bison() {
        super();
        setType("Bison");
        setWeight(700.0);
        setInitialWeight(700.0);
        setMaxCountInCell(10);
        setSpeed(3);
        setMaxFood(100.0);
        setFoodTypes(Arrays.asList(Grass.class));
    }

    @Override
    public void reproduce() {
    }
}
