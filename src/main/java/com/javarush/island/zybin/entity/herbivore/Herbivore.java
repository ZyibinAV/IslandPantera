package com.javarush.island.zybin.entity.herbivore;

/**
 * Абстрактный класс для травоядных животных.
 * Наследуется от Animal.
 * foodTypes будет задаваться индивидуально в конкретных классах (например, Rabbit, Duck).
 */

import com.javarush.island.zybin.entity.Animal;

import java.util.List;

public abstract class Herbivore extends Animal {

    public Herbivore() {
        super();
    }
}
