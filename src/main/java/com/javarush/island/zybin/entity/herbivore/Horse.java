package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.Arrays;

public class Horse extends Herbivore {
    public Horse() {
        super();
        setType("Horse");
        setWeight(400.0);
        setInitialWeight(400.0);
        setMaxCountInCell(20);
        setSpeed(4);
        setMaxFood(60.0);
        setFoodTypes(Arrays.asList(Grass.class));
    }





    @Override
    public void reproduce() {

    }
}


