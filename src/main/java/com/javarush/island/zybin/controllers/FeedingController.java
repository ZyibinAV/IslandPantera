package com.javarush.island.zybin.controllers;

import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;
import com.javarush.island.zybin.entities.plants.Grass;
import com.javarush.island.zybin.simulation.StatisticsCollector;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;



/**
 * Controller responsible for managing the feeding mechanics in the island simulation.
 * <p>
 * This class handles the feeding behavior of animals, including predator-prey interactions
 * and plant consumption. It ensures thread-safe operations when modifying cell contents.
 *
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Managing feeding attempts between animals</li>
 *   <li>Handling grass consumption by herbivores</li>
 *   <li>Updating statistics for tracking purposes</li>
 *   <li>Ensuring thread safety during concurrent access</li>
 * </ul>
 *
 * @see Animal
 * @see Cell
 * @see StatisticsCollector
 * @see Island
 */
public class FeedingController {
    private final Island island;
    private final StatisticsCollector statisticsCollector;
    private final Map<String, Map<String, Integer>> FEEDING_CHANCES;

    public FeedingController(Island island, StatisticsCollector statisticsCollector,
                             Map<String, Map<String, Integer>> feedingChances) {
        this.island = island;
        this.statisticsCollector = statisticsCollector;
        this.FEEDING_CHANCES = feedingChances;
    }

    public void feedAnimal(Animal animal, Cell currentCell) {
        if (!animal.isAlive() || animal.isFull()) {
            return;
        }
        String animalType = animal.getType();
        Map<String, Integer> chances = FEEDING_CHANCES.get(animalType);
        if (chances == null) {
            return;
        }

        for (LivingEntity food : currentCell.getEntities()) {
            String foodType = getEntityType(food);

            Integer chance = chances.get(foodType);
            if (chance == null) {
                continue;
            }
            int roll = ThreadLocalRandom.current().nextInt(0, 101);
            if (roll <= chance) {
                currentCell.getLock().lock();
                try {
                    if (currentCell.getEntities().contains(food)) {
                        if (food instanceof Animal) {
                            Animal prey = (Animal) food;
                            if(prey.isAlive()){
                                prey.setAlive(false);
                                statisticsCollector.incrementDied(prey.getType());
                                statisticsCollector.incrementEaten(prey.getType());
                            }
                        } else if (food instanceof Grass) {
                            Grass grass = (Grass) food;
                            if (grass.isAlive()) {
                                grass.setAlive(false);
                            }
                        }
                        currentCell.removeEntity(food);
                        double foodWeight = 0;
                        if (food instanceof Animal) {
                            foodWeight = ((Animal) food).getWeight();
                        } else if (food instanceof Grass) {
                            foodWeight = ((Grass) food).getWeight();
                        }
                        animal.gainWeight(foodWeight);
                        if (animal.isFull()) {
                            break;
                        }
                    }
                } finally {
                    currentCell.getLock().unlock();
                }
            }
        }
    }

    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return "Grass";
        }
        return "Unknown";
    }


}
