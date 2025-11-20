package com.javarush.island.zybin.factory;

import com.javarush.island.zybin.config.SimulationConfig;
import com.javarush.island.zybin.controllers.FeedingController;
import com.javarush.island.zybin.controllers.MovementController;
import com.javarush.island.zybin.controllers.ReproductionController;
import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.entities.herbivore.*;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;
import com.javarush.island.zybin.entities.plants.Grass;
import com.javarush.island.zybin.entities.predator.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Factory class responsible for creating and distributing entities across the island.
 * <p>
 * This class implements the Factory pattern to create various types of living entities
 * (animals and plants) and distributes them randomly across the island's cells.
 * It handles concurrent entity creation using a thread pool for better performance.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Creating instances of different entity types</li>
 *   <li>Distributing entities randomly across the island</li>
 *   <li>Managing thread-safe entity creation and placement</li>
 *   <li>Tracking and reporting statistics about created entities</li>
 *   <li>Respecting maximum population limits per cell</li>
 * </ul>
 *
 * @see LivingEntity
 * @see Animal
 * @see Grass
 * @see Island
 */
public class EntityFactory {
    private final Island island;
    private final Random random = new Random();
    private final MovementController movementController;
    private final FeedingController feedingController;
    private final ReproductionController reproductionController;

    private final Map<String, Integer> statistics = new HashMap<>();
    private final SimulationConfig config;

    public EntityFactory(Island island, MovementController movementController,
                         FeedingController feedingController,
                         ReproductionController reproductionController,
                         SimulationConfig config) {
        this.island = island;
        this.movementController = movementController;
        this.feedingController = feedingController;
        this.reproductionController = reproductionController;
        this.config = config;
    }

    public void populateIsland() {
        List<Class<? extends LivingEntity>> entityTypes = Arrays.asList(Bison.class,
                Boar.class, Caterpillar.class, Deer.class, Duck.class, Goat.class,
                Horse.class, Mouse.class, Rabbit.class, Sheep.class, Bear.class,
                Eagle.class, Fox.class, Python.class, Wolf.class, Grass.class);
        for (Class<? extends LivingEntity> entityType : entityTypes) {
            int created = createAndDistribute(entityType);
            String typeName = getSimpleTypeName(entityType);
            statistics.put(typeName, created);
        }
        int grassCount = createAndDistribute(Grass.class);
        statistics.put("Grass", grassCount);
        printStatistics();
    }

    private <T extends LivingEntity> int createAndDistribute(Class<T> clazz) {
        int totalToCreate = random.nextInt(1000) + 100;
        if (clazz == Grass.class) {
            totalToCreate = island.getRows() * island.getCols() * config.grassPerCellMultiplier;
        }
        AtomicInteger placedCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < totalToCreate; i++) {
            Future<?> future = executor.submit(() -> {
                try {
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    if (entity instanceof Animal animal) {
                        animal.setMovementController(movementController);
                        animal.setFeedingController(feedingController);
                        animal.setReproductionController(reproductionController);
                    } else if (entity instanceof Grass grass) {
                        grass.setReproductionController(reproductionController);
                    }
                    String entityType = getEntityType(entity);
                    int row = random.nextInt(island.getRows());
                    int col = random.nextInt(island.getCols());
                    Cell cell = island.getCell(row, col);
                    int maxCount = -1;
                    if (entity instanceof Animal) {
                        maxCount = ((Animal) entity).getMaxCountInCell();
                    } else if (entity instanceof Grass) {
                        maxCount = ((Grass) entity).getMaxCountInCell();
                    }

                    if (maxCount == -1) {
                        return;
                    }
                    if (cell.tryAddEntity(entity, maxCount, (Class<? extends LivingEntity>) clazz)) {
                        placedCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            futures.add(future);
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return placedCount.get();
    }

    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return ((Grass) entity).getType();
        }
        return "Unknown";
    }

    private String getSimpleTypeName(Class<? extends LivingEntity> clazz) {
        return clazz.getSimpleName();
    }


    private void printStatistics() {
        System.out.println("\n Статистика размещённых сущностей:");
        System.out.println("=====================================");
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("=====================================");
    }

}
