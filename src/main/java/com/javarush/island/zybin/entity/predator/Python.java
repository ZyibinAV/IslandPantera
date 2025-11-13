package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.herbivore.Duck;
import com.javarush.island.zybin.entity.herbivore.Mouse;
import com.javarush.island.zybin.entity.herbivore.Rabbit;

import java.util.Arrays;

public class Python extends  Predator {

    public Python() {
        super();
        setType("Python");
        setWeight(15.0);
        setInitialWeight(15.0);
        setMaxCountInCell(30);
        setSpeed(1);
        setMaxFood(3.0);
        setFoodTypes(Arrays.asList(Fox.class, Rabbit.class, Mouse.class, Duck.class));
    }





    @Override
    public void reproduction() {

    }
}
