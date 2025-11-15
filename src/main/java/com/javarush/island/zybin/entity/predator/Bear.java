package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.herbivore.*;

import java.util.Arrays;

public class Bear extends  Predator {

    public Bear() {
        super();
        setType("Bear");
        setWeight(500.0);
        setInitialWeight(500.0);
        setMaxCountInCell(5);
        setSpeed(2);
        setMaxFood(80.0);
        setFoodTypes(Arrays.asList(Python.class, Horse.class, Deer.class, Rabbit.class, Mouse.class,
                Goat.class, Sheep.class, Boar.class, Bison.class, Duck.class));
    }





    @Override
    public void reproduce() {

    }
}
