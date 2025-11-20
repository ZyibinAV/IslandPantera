package com.javarush.island.zybin.simulation;

import com.javarush.island.zybin.config.SimulationConfig;
import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.factory.EntityFactory;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;
import com.javarush.island.zybin.controllers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The main simulation class that orchestrates the island ecosystem simulation.
 * <p>
 * This class manages the simulation lifecycle, including initialization, execution of simulation turns,
 * and resource cleanup. It coordinates the interaction between different components such as
 * movement, feeding, and reproduction of entities across the island.
 *
 * <p>Key features:
 * <ul>
 *   <li>Manages the simulation loop with configurable turn timing</li>
 *   <li>Coordinates parallel execution of entity behaviors</li>
 *   <li>Handles simulation state and termination conditions</li>
 *   <li>Manages thread pools for concurrent operations</li>
 *   <li>Collects and reports simulation statistics</li>
 * </ul>
 *
 * <p>The simulation proceeds in discrete turns, with each turn consisting of the following phases:
 * <ol>
 *   <li>Hunger application to all animals</li>
 *   <li>Feeding phase (parallel execution)</li>
 *   <li>Movement phase (parallel execution)</li>
 *   <li>Reproduction phase (parallel execution)</li>
 *   <li>Reset of food counters</li>
 * </ol>
 *
 * @see Island
 * @see EntityFactory
 * @see MovementController
 * @see FeedingController
 * @see ReproductionController
 * @see StatisticsCollector
 */
public class Simulation {

    private final Island island;
    private EntityFactory factory;
    private final MovementController movementController;
    private final FeedingController feedingController;
    private  ReproductionController reproductionController;
    private final StatisticsCollector statisticsCollector;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final SimulationConfig config;
    private volatile boolean isRunning = true;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.island = new Island(config.islandRows, config.islandCols);
        this.statisticsCollector = new StatisticsCollector(island);
        this.movementController = new MovementController(island);
        this.feedingController = new FeedingController(island, statisticsCollector, config.FEEDING_CHANCES);
        this.reproductionController = new ReproductionController(island, statisticsCollector,
                movementController, feedingController, config.grassGrowthPercent);
        this.factory = new EntityFactory(island, movementController, feedingController, reproductionController, config);

        this.executorService = Executors.newFixedThreadPool(10);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        factory.populateIsland();
        System.out.println("Симуляция начата. Остров заполнен сущностями.");
        System.out.println("Начинаем цикл симуляции...\n");

        scheduledExecutorService.scheduleAtFixedRate(
                statisticsCollector::printStats,
                0, 1, TimeUnit.SECONDS
        );

        while (isRunning) {
            runTurn();
            try {
                Thread.sleep(1000);// Пауза между тактами (1 секунда)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            if (areAllAnimalsDead()) {
                System.out.println("\nВсе животные умерли. Симуляция завершена.");
                isRunning = false;
            }
        }
        shutdown();
    }

    private void runTurn() {
        statisticsCollector.incrementTurn();
        statisticsCollector.resetCounters();

        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal) {
                        animal.reduceWeight(config.hungerLossPercent);

                    }
                }
            }
        }
        List<Future<?>> eatFutures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal && animal.isAlive()) {
                       Future<?> future = executorService.submit(() -> animal.eat(cell));
                       eatFutures.add(future);
                    }
                }
            }
        }
        waitForFutures(eatFutures);

        List<Future<?>> moveFutures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal && animal.isAlive()) {
                        Future<?> future = executorService.submit(() -> animal.move(cell));
                        moveFutures.add(future);
                    }
                }
            }
        }
        waitForFutures(moveFutures);

        List<Future<?>> reproduceFutures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                Future<?> future = executorService.submit(() -> reproductionController.reproduceInCell(cell));
                reproduceFutures.add(future);
            }
        }
        waitForFutures(reproduceFutures);

        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal) {
                        animal.resetFoodCount();
                    }
                }
            }
        }

    }

    private void waitForFutures(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean areAllAnimalsDead() {
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal && animal.isAlive()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void shutdown() {
        executorService.shutdown();
        scheduledExecutorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}


