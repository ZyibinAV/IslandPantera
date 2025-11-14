package com.javarush.island.zybin.services;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ReproductionController {
    private final Island island;

    public ReproductionController(Island island) {
        this.island = island;
    }

    /**
     * Метод, который вызывается из Animal.reproduce().
     *
     * @param animal Животное, которое размножается.
     */
    public void reproduceAnimal(Animal animal) {
        // Логика: найти ячейку, в которой находится животное, и проверить условия
        // Это требует, чтобы Animal знал, в какой ячейке он находится.
        // Это можно реализовать, передавая ячейку в вызов reproduce, или храня ссылку на ячейку.
        // Но это усложнит интерфейс. Лучше вызывать reproduction из симуляции для всей ячейки.
        // Поэтому этот метод не будет использоваться напрямую.
        throw new UnsupportedOperationException("Reproduce для животных должен вызываться в контексте ячейки.");
    }

    /**
     * Метод, который вызывается из Grass.reproduce().
     *
     * @param grass Трава, которая размножается.
     */
    public void reproduceGrass(Grass grass) {
        // Логика: найти ячейку, в которой находится трава, и проверить условия
        // Аналогично — вызов должен происходить в контексте ячейки.
        throw new UnsupportedOperationException("Reproduce для травы должен вызываться в контексте ячейки.");
    }

    /**
     * Основной метод, вызываемый из симуляции для ячейки.
     *
     * @param cell Ячейка, в которой происходит размножение.
     */
    public void reproduceInCell(Cell cell) {
        // животные
        Map<String, List<Animal>> animalsByType = cell.getEntities().stream().filter(e -> e instanceof Animal && ((Animal) e).isAlive()).map(e -> (Animal) e).filter(a -> a.getWeight() >= a.getInitialWeight() * 0.5).collect(Collectors.groupingBy(Animal::getType));

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

                    synchronized (cell) {
                        if (cell.getCountByType(clazz) < maxCount) {
                            cell.addEntity(child);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Трава
        List<Grass> grasses = cell.getEntities().stream().filter(e -> e instanceof Grass && ((Grass) e).isAlive()).map(e -> (Grass) e).toList();

        int currentGrassCount = grasses.size();
        int maxGrassCount = grasses.isEmpty() ? 200 : grasses.get(0).getMaxCountInCell();
        int newGrassToAdd = (int) (currentGrassCount * 01);

        if (currentGrassCount + newGrassToAdd <= maxGrassCount) {
            synchronized (cell) {
                for (int i = 0; i < newGrassToAdd; i++) {
                    cell.addEntity(new Grass());
                }
            }
        }
    }
}
