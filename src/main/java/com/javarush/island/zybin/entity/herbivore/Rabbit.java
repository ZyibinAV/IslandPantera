package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

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
    public void reproduction() {

    }
}
