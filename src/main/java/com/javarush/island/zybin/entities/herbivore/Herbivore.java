package com.javarush.island.zybin.entities.herbivore;

/**
 * Абстрактный класс для травоядных животных.
 * Наследуется от Animal.
 * foodTypes будет задаваться индивидуально в конкретных классах (например, Rabbit, Duck).
 */

import com.javarush.island.zybin.entities.Animal;

public abstract class Herbivore extends Animal {

    public Herbivore() {
        super();
    }
}
