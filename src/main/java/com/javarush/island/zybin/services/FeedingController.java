package com.javarush.island.zybin.services;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс, отвечающий за логику питания животных.
 * Использует многопоточный Random (ThreadLocalRandom).
 */

public class FeedingController {
    private final Island island;
    private final StatisticsCollector statisticsCollector;

    // Статическая таблица шансов поедания (в %)
    private static final Map<String, Map<String, Integer>> FEEDING_CHANCES = Map.ofEntries(
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


    public FeedingController(Island island, StatisticsCollector statisticsCollector) {
        this.island = island;
        this.statisticsCollector = statisticsCollector;
    }

    /**
     * Метод, который пытается покормить животное в текущей ячейке.
     *
     * @param animal      Животное, которое ест.
     * @param currentCell Ячейка, в которой животное находится.
     */
    public void feedAnimal(Animal animal, Cell currentCell) {
        if (animal.isAlive() || animal.isFull()) {
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
                synchronized (currentCell) {
                    if (currentCell.getEntities().contains(food)) {
                        if (food instanceof Animal) {
                            Animal prey = (Animal) food;
                            if(prey.isAlive()){
                                prey.setAlive(false);
                                statisticsCollector.incrementDied(prey.getType()); // Статистика умерших
                                statisticsCollector.incrementEaten(prey.getType()); // Статистика съеденных
                            }
                        } else if (food instanceof Grass) {
                            ((Grass) food).setAlive(false); // Трава "умирает"
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
