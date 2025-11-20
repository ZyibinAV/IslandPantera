package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Rabbit extends Herbivore {

    public Rabbit() {
        super();
        setType("Rabbit");
        setWeight(2.0);
        setInitialWeight(2.0);
        setMaxCountInCell(150);
        setSpeed(2);
        setMaxFood(0.45);
        setFoodTypes(Arrays.asList(Grass.class));
    }

    @Override
    public void reproduce() {
    }
}
