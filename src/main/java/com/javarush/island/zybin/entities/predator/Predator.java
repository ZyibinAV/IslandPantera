package com.javarush.island.zybin.entities.predator;


import com.javarush.island.zybin.entities.Animal;

/**
 * Abstract base class representing predator animals in the island simulation.
 * <p>
 * This class provides the foundation for all predator entities, defining common
 * characteristics and behaviors. Concrete predator classes should extend this class
 * and specify their unique attributes such as weight, speed, and preferred prey.
 *
 * <p>Key characteristics of predators:
 * <ul>
 *   <li>Hunt and consume other animals for food</li>
 *   <li>Play a crucial role in population control</li>
 *   <li>Vary in size, hunting strategies, and territory</li>
 *   <li>Have specific dietary preferences and hunting success rates</li>
 * </ul>
 *
 * @see Animal
 * @see com.javarush.island.zybin.entities.herbivore.Herbivore
 */
public abstract class Predator extends Animal {
    public Predator() {
        super();
    }
}
