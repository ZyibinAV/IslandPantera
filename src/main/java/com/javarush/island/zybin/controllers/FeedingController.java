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
 * Класс, отвечающий за логику питания животных.
 * Использует многопоточный Random (ThreadLocalRandom).
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

    /**
     * Метод, который пытается покормить животное в текущей ячейке.
     *
     * @param animal      Животное, которое ест.
     * @param currentCell Ячейка, в которой животное находится.
     */
    public void feedAnimal(Animal animal, Cell currentCell) {
        if (!animal.isAlive() || animal.isFull()) {
            return;
        }
        String animalType = animal.getType();
        Map<String, Integer> chances = FEEDING_CHANCES.get(animalType);
        if (chances == null) {
            return; // Нет данных о том, чем питается это животное
        }

        // Ищем подходящую еду в ячейке
        for (LivingEntity food : currentCell.getEntities()) {
            String foodType = getEntityType(food);

            Integer chance = chances.get(foodType);
            if (chance == null) {
                continue; //Это животное не ест эту еду
            }
            // Проверяем шанс
            int roll = ThreadLocalRandom.current().nextInt(0, 101);
            if (roll <= chance) {
                // Животное съедает еду
                currentCell.getLock().lock();
                try {
                    if (currentCell.getEntities().contains(food)) {
                        if (food instanceof Animal) {
                            Animal prey = (Animal) food;
                            if(prey.isAlive()){
                                prey.setAlive(false);
                                statisticsCollector.incrementDied(prey.getType()); // Статистика умерших
                                statisticsCollector.incrementEaten(prey.getType()); // Статистика съеденных
                            }
                        } else if (food instanceof Grass) {
                            Grass grass = (Grass) food;
                            if (grass.isAlive()) {
                                grass.setAlive(false); // Трава "умирает"
                            }
                        }
                        currentCell.removeEntity(food);
                        double foodWeight = 0;
                        if (food instanceof Animal) {
                            foodWeight = ((Animal) food).getWeight();
                        } else if (food instanceof Grass) {
                            foodWeight = ((Grass) food).getWeight();
                        }
                        animal.gainWeight(foodWeight); // Прибавляем вес
                        if (animal.isFull()) {
                            break; // живтное наелось
                        }
                    }
                } finally {
                    currentCell.getLock().unlock();
                }
            }
        }
    }

    /**
     * Вспомогательный метод для получения типа сущности.
     */
    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return "Grass";
        }
        return "Unknown";
    }


}
