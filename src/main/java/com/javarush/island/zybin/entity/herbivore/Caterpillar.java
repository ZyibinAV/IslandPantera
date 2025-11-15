package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.Arrays;

public class Caterpillar extends Herbivore{

    public Caterpillar() {
        super();
        setType("Caterpillar");
        setWeight(0.01);
        setInitialWeight(0.01);
        setMaxCountInCell(1000);
        setSpeed(0);
        setMaxFood(0.0);
        setFoodTypes(Arrays.asList(Grass.class));
    }





    @Override
    public void reproduce() {

    }
}
