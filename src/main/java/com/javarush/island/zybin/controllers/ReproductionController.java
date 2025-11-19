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
 * Класс, отвечающий за логику размножения животных и рост травы.
 * Использует многопоточный Random (ThreadLocalRandom).
 */
public class ReproductionController {
    private final Island island;
    private final StatisticsCollector statisticsCollector;

    // --- Добавим поля для контроллеров ---
    private final MovementController movementController;
    private final FeedingController feedingController;
   // private  ReproductionController selfReference;

    public ReproductionController(Island island, StatisticsCollector statisticsCollector,
                                  MovementController movementController,
                                  FeedingController feedingController) {
        this.island = island;
        this.statisticsCollector = statisticsCollector;
        this.movementController = movementController;
        this.feedingController = feedingController;
    }
    /**
     * Метод, вызываемый из Animal.reproduce().
     * В текущей архитектуре не используется, т.к. размножение происходит по ячейкам.
     */
    public void reproduceAnimal(Animal animal) {
        throw new UnsupportedOperationException("Reproduce для животных должен вызываться в контексте ячейки.");
    }

    /**
     * Метод, вызываемый из Grass.reproduce().
     * В текущей архитектуре не используется, т.к. размножение происходит по ячейкам.
     */
    public void reproduceGrass(Grass grass) {
        throw new UnsupportedOperationException("Reproduce для травы должен вызываться в контексте ячейки.");
    }


    /**
     * Основной метод, вызываемый из симуляции для ячейки.
     *
     * @param cell Ячейка, в которой происходит размножение.
     */
    public void reproduceInCell(Cell cell) {
        // животные
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

                    // --- УСТАНОВКА КОНТРОЛЛЕРОВ ---
                    child.setMovementController(movementController);
                    child.setFeedingController(feedingController);
                   // child.setReproductionController(this.selfReference);

                    synchronized (cell) {
                        if (cell.getCountByType(clazz) < maxCount) {
                            cell.addEntity(child);
                            statisticsCollector.incrementBorn(child.getType()); //статистика рожденных
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Трава
        List<Grass> grasses = cell.getEntities().stream()
                .filter(e -> e instanceof Grass && ((Grass) e).isAlive())
                .map(e -> (Grass) e)
                .toList();

        int currentGrassCount = grasses.size();
        int maxGrassCount = grasses.isEmpty() ? 200 : grasses.get(0).getMaxCountInCell();
        double newGrassToAdd = (double) (currentGrassCount * 2);

        if (currentGrassCount + newGrassToAdd <= maxGrassCount) {
            synchronized (cell) {
                for (int i = 0; i < newGrassToAdd; i++) {
                    cell.addEntity(new Grass());
                    statisticsCollector.incrementBorn("Grass"); // статистика выросшей травы
                }
            }
        }
    }
}
