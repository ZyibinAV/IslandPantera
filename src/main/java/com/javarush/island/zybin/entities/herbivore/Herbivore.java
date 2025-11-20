package com.javarush.island.zybin.entities.herbivore;



import com.javarush.island.zybin.entities.Animal;

/**
 * Abstract base class representing herbivorous animals in the island simulation.
 * <p>
 * This class serves as the foundation for all herbivore entities, providing common
 * functionality and structure. Concrete herbivore classes should extend this class
 * and define their specific characteristics such as weight, speed, and food types.

 * @see Animal
 * @see com.javarush.island.zybin.entities.plants.Grass
 */
public abstract class Herbivore extends Animal {

    public Herbivore() {
        super();
    }
}
