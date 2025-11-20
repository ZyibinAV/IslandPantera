package com.javarush.island.zybin.config;

import java.util.Map;

/**
 * Configuration class for the island simulation.
 * <p>
 * This class holds all the configuration parameters required to run the island simulation,
 * including island dimensions, hunger mechanics, initial animal population, and feeding behaviors.
 * It provides a default configuration through the {@link #getDefaultConfig()} method.
 *
 * <p>Key configuration parameters include:
 * <ul>
 *   <li>Island dimensions (rows and columns)</li>
 *   <li>Hunger mechanics (percentage of hunger loss per turn)</li>
 *   <li>Initial animal population</li>
 *   <li>Grass growth and distribution parameters</li>
 *   <li>Feeding chances between different animal species</li>
 * </ul>
 *
 * <p>The feeding chances are represented as a nested map where the outer map's key is the predator
 * and the inner map contains the prey species with their respective success percentages.
 *
 * @see #getDefaultConfig()
 */
public class SimulationConfig {

    public final int islandRows;
    public final int islandCols;

    public final double hungerLossPercent;
    public final int initialAnimalAmount;

    public final int grassPerCellMultiplier;
    public final double grassGrowthPercent;

    public final Map<String, Map<String, Integer>> FEEDING_CHANCES;


    public SimulationConfig(int islandRows, int islandCols,
                            double hungerLossPercent, int initialAnimalAmount,
                            int grassPerCellMultiplier, double grassGrowthPercent,
                            Map<String, Map<String, Integer>> feedingChances) {
        this.islandRows = islandRows;
        this.islandCols = islandCols;
        this.hungerLossPercent = hungerLossPercent;
        this.initialAnimalAmount = initialAnimalAmount;
        this.grassPerCellMultiplier = grassPerCellMultiplier;
        this.grassGrowthPercent = grassGrowthPercent;
        this.FEEDING_CHANCES = feedingChances;
    }

    public static SimulationConfig getDefaultConfig() {
        Map<String, Map<String, Integer>> feedingChances = Map.ofEntries(
                Map.entry("Wolf", Map.ofEntries(
                        Map.entry("Horse", 10),
                        Map.entry("Deer", 15),
                        Map.entry("Rabbit", 60),
                        Map.entry("Mouse", 80),
                        Map.entry("Goat", 60),
                        Map.entry("Sheep", 70),
                        Map.entry("Boar", 15),
                        Map.entry("Bison", 10),
                        Map.entry("Duck", 40)
                )),
                Map.entry("Python", Map.ofEntries(
                        Map.entry("Fox", 15),
                        Map.entry("Rabbit", 20),
                        Map.entry("Mouse", 40),
                        Map.entry("Duck", 10)
                )),
                Map.entry("Fox", Map.ofEntries(
                        Map.entry("Rabbit", 70),
                        Map.entry("Mouse", 90),
                        Map.entry("Duck", 60),
                        Map.entry("Caterpillar", 40)
                )),
                Map.entry("Bear", Map.ofEntries(
                        Map.entry("Python", 80),
                        Map.entry("Horse", 40),
                        Map.entry("Deer", 80),
                        Map.entry("Rabbit", 80),
                        Map.entry("Mouse", 90),
                        Map.entry("Goat", 70),
                        Map.entry("Sheep", 70),
                        Map.entry("Boar", 50),
                        Map.entry("Bison", 20),
                        Map.entry("Duck", 10)
                )),
                Map.entry("Eagle", Map.ofEntries(
                        Map.entry("Fox", 10),
                        Map.entry("Rabbit", 90),
                        Map.entry("Mouse", 90),
                        Map.entry("Duck", 80)
                )),
                Map.entry("Horse", Map.of("Grass", 100)),
                Map.entry("Deer", Map.of("Grass", 100)),
                Map.entry("Rabbit", Map.of("Grass", 100)),
                Map.entry("Mouse", Map.ofEntries(
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Goat", Map.of("Grass", 100)),
                Map.entry("Sheep", Map.of("Grass", 100)),
                Map.entry("Boar", Map.ofEntries(
                        Map.entry("Mouse", 50),
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Bison", Map.of("Grass", 100)),
                Map.entry("Duck", Map.ofEntries(
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Caterpillar", Map.of("Grass", 100))
        );
        return new SimulationConfig(
                120,
                20,
                0.02,
                1000,
                50,
                0.10,
                feedingChances
        );
    }
}
