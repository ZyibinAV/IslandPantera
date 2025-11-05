package com.javarush.island.zybin.entity.herbivore;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.List;

public abstract class Herbivore extends Animal {

    public Herbivore(String name, double weight, int speed, int maxFood, int maxCountInCell, List<Class<? extends LivingEntity>> foodTypes) {
        super(name, weight, speed, maxFood, maxCountInCell, List.of(Grass.class));
    }
}
