package com.javarush.island.zybin.entities.predator;

import com.javarush.island.zybin.entities.herbivore.Caterpillar;
import com.javarush.island.zybin.entities.herbivore.Duck;
import com.javarush.island.zybin.entities.herbivore.Mouse;
import com.javarush.island.zybin.entities.herbivore.Rabbit;

import java.util.Arrays;

public class Fox extends  Predator{

    public Fox() {
        super();
        setType("Fox");
        setWeight(8.0);
        setInitialWeight(8.0);
        setMaxCountInCell(30);
        setSpeed(2);
        setMaxFood(2.0);
        setFoodTypes(Arrays.asList(Rabbit.class, Mouse.class, Duck.class, Caterpillar.class));
    }





    @Override
    public void reproduce() {

    }
}
