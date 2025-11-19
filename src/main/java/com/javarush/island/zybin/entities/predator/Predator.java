package com.javarush.island.zybin.entities.predator;
/**
 * Абстрактный класс для хищников.
 * Наследуется от Animal.
 * foodTypes будет задаваться индивидуально в конкретных классах (например, Wolf, Fox).
 */

import com.javarush.island.zybin.entities.Animal;

public abstract class Predator extends Animal {
    public Predator() {
        super();
    }
}
