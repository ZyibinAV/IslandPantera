package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.Arrays;

public class Goat extends Herbivore{

    public Goat() {
        super();
        setType("Goat");
        setWeight(60.0);
        setInitialWeight(60.0);
        setMaxCountInCell(140);
        setSpeed(3);
        setMaxFood(10);
        setFoodTypes(Arrays.asList(Grass.class));
    }





    @Override
    public void reproduction() {

    }
}
