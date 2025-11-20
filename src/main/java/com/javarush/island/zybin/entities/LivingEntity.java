package com.javarush.island.zybin.entities;

/**
 * Base interface for all living entities in the island simulation.
 * <p>
 * This interface defines the fundamental contract that all living organisms
 * in the ecosystem must implement. It serves as the root of the entity hierarchy
 * and ensures that all living things can participate in the simulation's lifecycle.
 *
 * <p>Key aspects:
 * <ul>
 *   <li>Represents any living organism in the ecosystem</li>
 *   <li>Defines the basic behavior for reproduction</li>
 *   <li>Implemented by both animals and plants</li>
 *   <li>Forms the foundation of the simulation's entity system</li>
 * </ul>
 *
 * @see Animal
 * @see com.javarush.island.zybin.entities.plants.Grass
 */
public interface LivingEntity {


    void reproduce();
}
