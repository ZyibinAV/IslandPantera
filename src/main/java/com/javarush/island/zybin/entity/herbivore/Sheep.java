package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.Arrays;

public class Sheep extends Herbivore {

    public Sheep() {
        super();
        setType("Sheep");
        setWeight(70.0);
        setInitialWeight(70.0);
        setMaxCountInCell(140);
        setSpeed(3);
        setMaxFood(15.0);
        setFoodTypes(Arrays.asList(Grass.class));
    }




    @Override
    public void reproduction() {

    }
}
