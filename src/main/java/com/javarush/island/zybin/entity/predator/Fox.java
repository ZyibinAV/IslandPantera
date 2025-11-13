package com.javarush.island.zybin.entity.predator;

import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.herbivore.Caterpillar;
import com.javarush.island.zybin.entity.herbivore.Duck;
import com.javarush.island.zybin.entity.herbivore.Mouse;
import com.javarush.island.zybin.entity.herbivore.Rabbit;

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
    public void reproduction() {

    }
}
