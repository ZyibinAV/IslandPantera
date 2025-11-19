package com.javarush.island.zybin.entities.herbivore;

import com.javarush.island.zybin.entities.plants.Grass;

import java.util.Arrays;

public class Duck extends Herbivore{

    public Duck() {
        super();
        setType("Duck");
        setWeight(1.0);
        setInitialWeight(1.0);
        setMaxCountInCell(200);
        setSpeed(4);
        setMaxFood(0.15);
        setFoodTypes(Arrays.asList(Caterpillar.class, Grass.class));
    }




    @Override
    public void reproduce() {

    }
}
