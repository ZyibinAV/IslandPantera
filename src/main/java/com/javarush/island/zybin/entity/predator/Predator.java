package com.javarush.island.zybin.entity.predator;


import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;

import java.util.List;

public abstract class Predator extends Animal {


    public Predator(String name, double weight, int speed, int maxFood, int maxCountInCell, List<Class<? extends LivingEntity>> foodTypes) {
        super(name, weight, speed, maxFood, maxCountInCell, foodTypes);
    }
}
