package com.javarush.island.zybin.entity.predator;
/**
 * Абстрактный класс для хищников.
 * Наследуется от Animal.
 * foodTypes будет задаваться индивидуально в конкретных классах (например, Wolf, Fox).
 */

import com.javarush.island.zybin.entity.Animal;

import java.util.List;

public abstract class Predator extends Animal {
    public Predator() {
        super();
    }
}
