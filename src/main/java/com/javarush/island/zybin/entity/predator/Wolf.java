package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.herbivore.*;

import java.util.Arrays;

public class Wolf extends Predator{

    public Wolf() {
        super();
        setType("Wolf");
        setWeight(50.0);
        setInitialWeight(50.0);
        setMaxCountInCell(30);
        setSpeed(3);
        setMaxFood(8.0);
        setFoodTypes(Arrays.asList(Horse.class, Deer.class, Rabbit.class, Mouse.class,
                Goat.class, Sheep.class, Boar.class, Bison.class, Duck.class));
    }





    @Override
    public void reproduce() {

    }
}
