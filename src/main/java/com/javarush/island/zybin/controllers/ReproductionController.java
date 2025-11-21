package com.javarush.island.zybin.controllers;

import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;
import com.javarush.island.zybin.entities.plants.Grass;
import com.javarush.island.zybin.simulation.StatisticsCollector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Controller responsible for managing reproduction mechanics in the island simulation.
 * <p>
 * Handles both animal reproduction and grass growth within individual cells while maintaining
 * thread-safety and population limits. Works in conjunction with other controllers to ensure
 * proper ecosystem balance.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Manages animal reproduction based on population density and conditions</li>
 *   <li>Controls grass growth according to specified growth percentage</li>
 *   <li>Ensures thread-safe operations during reproduction</li>
 *   <li>Updates statistics for new organisms</li>
 *   <li>Maintains maximum population limits per cell</li>
 * </ul>
 *
 * @see Animal
 * @see Grass
 * @see Cell
 * @see StatisticsCollector
 * @see MovementController
 * @see FeedingController
 */
public class ReproductionController {
    private final Island island;
    private final StatisticsCollector statisticsCollector;
    private final MovementController movementController;
    private final FeedingController feedingController;
    private final  double grassGrowthPercent;

    public ReproductionController(Island island, StatisticsCollector statisticsCollector,
                                  MovementController movementController,
                                  FeedingController feedingController,
                                  double grassGrowthPercent) {
        this.island = island;
        this.statisticsCollector = statisticsCollector;
        this.movementController = movementController;
        this.feedingController = feedingController;
        this.grassGrowthPercent = grassGrowthPercent;
    }

    public void reproduceGrass(Grass grass) {
        throw new UnsupportedOperationException("Reproduce for grass must be called in the context of the cell.");
    }



    public void reproduceInCell(Cell cell) {
        Map<String, List<Animal>> animalsByType = cell.getEntities().stream()
                .filter(e -> e instanceof Animal && ((Animal) e).isAlive())
                .map(e -> (Animal) e)
                .filter(a -> a.getWeight() >= a.getInitialWeight() * 0.5)
                .collect(Collectors.groupingBy(Animal::getType));

        for (List<Animal> sameTypeAnimals : animalsByType.values()) {
            if (sameTypeAnimals.size() < 2) {
                continue;
            }

            Class<? extends Animal> clazz = sameTypeAnimals.get(0).getClass();
            int maxCount = sameTypeAnimals.get(0).getMaxCountInCell();

            if (cell.getCountByType(clazz) >= maxCount) {
                continue;
            }

            if (ThreadLocalRandom.current().nextInt(0, 100) < 50) {
                try {
                    Animal child = clazz.getDeclaredConstructor().newInstance();
                    Animal parent = sameTypeAnimals.get(0);
                    child.setType(parent.getType());
                    child.setWeight(parent.getWeight());
                    child.setInitialWeight(parent.getInitialWeight());
                    child.setMaxCountInCell(parent.getMaxCountInCell());
                    child.setSpeed(parent.getSpeed());
                    child.setMaxFood(parent.getMaxFood());
                    child.setFoodTypes(parent.getFoodTypes());

                    child.setMovementController(movementController);
                    child.setFeedingController(feedingController);
                    cell.getLock().lock();
                    try {
                        if (cell.getCountByType(clazz) < maxCount) {
                            cell.addEntity(child);
                            statisticsCollector.incrementBorn(child.getType());
                        }
                    } finally {
                        cell.getLock().unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        List<Grass> grasses = cell.getEntities().stream()
                .filter(e -> e instanceof Grass && ((Grass) e).isAlive())
                .map(e -> (Grass) e)
                .toList();

        int currentGrassCount = grasses.size();
        int maxGrassCount = grasses.isEmpty() ? 200 : grasses.get(0).getMaxCountInCell();
        int newGrassToAdd = (int) (currentGrassCount * grassGrowthPercent);

        if (currentGrassCount + newGrassToAdd <= maxGrassCount) {
            cell.getLock().lock();
            try {
                for (int i = 0; i < newGrassToAdd; i++) {
                    cell.addEntity(new Grass());
                    statisticsCollector.incrementBorn("Grass");
                }
            } finally {
                cell.getLock().unlock();
            }
        }
    }
}
