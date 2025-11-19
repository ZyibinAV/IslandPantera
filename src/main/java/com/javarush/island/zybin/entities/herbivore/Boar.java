package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Boar extends Herbivore{

    public Boar() {
        super();
        setType("Boar");
        setWeight(400.0);
        setInitialWeight(400.0);
        setMaxCountInCell(50);
        setSpeed(2);
        setMaxFood(50);
        setFoodTypes(Arrays.asList(Mouse.class, Caterpillar.class, Grass.class));
    }





    @Override
    public void reproduce() {

    }
}
