package com.javarush.island.zybin.entities.predator;

import com.javarush.island.zybin.entities.herbivore.Duck;
import com.javarush.island.zybin.entities.herbivore.Mouse;
import com.javarush.island.zybin.entities.herbivore.Rabbit;

import java.util.Arrays;

public class Eagle extends  Predator {

    public Eagle() {
        super();
        setType("Eagle");
        setWeight(6.0);
        setInitialWeight(6.0);
        setMaxCountInCell(20);
        setSpeed(3);
        setMaxFood(1.0);
        setFoodTypes(Arrays.asList(Fox.class, Rabbit.class, Mouse.class, Duck.class));
    }




    @Override
    public void reproduce() {

    }
}
