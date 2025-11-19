package com.javarush.island.zybin.simulation;


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
 * Класс, управляющий симуляцией жизни на острове.
 * Запускает фазы: голод, еда, движение, размножение в детерминированном порядке.
 * Использует многопоточность для параллельного выполнения операций внутри фазы.
 * Завершает симуляцию, когда умирают все животные.
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

    private volatile boolean isRunning = true;

    public Simulation(Island island) {
        this.island = island;
        this.statisticsCollector = new StatisticsCollector(island);
        this.movementController = new MovementController(island);
        this.feedingController = new FeedingController(island, statisticsCollector);
        this.reproductionController = new ReproductionController(island, statisticsCollector,
                movementController, feedingController);
        this.factory = new EntityFactory(island, movementController, feedingController, reproductionController);

        this.executorService = Executors.newFixedThreadPool(10);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        // Инициализация симуляции
        factory.populateIsland();
        System.out.println("Симуляция начата. Остров заполнен сущностями.");
        System.out.println("Начинаем цикл симуляции...\n");

        // Запуск вывода статистики каждую секунду (например)
        scheduledExecutorService.scheduleAtFixedRate(
                statisticsCollector::printStats,
                0, 1, TimeUnit.SECONDS
        );

        // Основной цикл симуляции
        while (isRunning) {
            runTurn();
            try {
                Thread.sleep(1000);// Пауза между тактами (1 секунда)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            // Проверка окончания симуляции
            if (areAllAnimalsDead()) {
                System.out.println("\nВсе животные умерли. Симуляция завершена.");
                isRunning = false;
            }
        }
        shutdown(); // проверить
    }
    /**
     * Выполняет один полный такт симуляции.
     * Гарантирует детерминированный порядок фаз.
     */

    private void runTurn() {
        // Увеличиваем счётчик тактов
        statisticsCollector.incrementTurn();
        // Сброс счётчиков статистики в начале такта
        statisticsCollector.resetCounters();

        // 1. Животные уменьшают вес (голод)
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal) {
                        animal.reduceWeight();

                    }
                }
            }
        }
        // 2. Животные едят (параллельно, но дожидаемся завершения)
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

        // 3. Животные перемещаются (параллельно, но дожидаемся завершения)
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

        // 4. Размножение (параллельно, но дожидаемся завершения)
        List<Future<?>> reproduceFutures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                Future<?> future = executorService.submit(() -> reproductionController.reproduceInCell(cell));
                reproduceFutures.add(future);
            }
        }
        waitForFutures(reproduceFutures);

        // 5. Сброс счётчиков насыщения (только после завершения фазы "есть")
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
    /**
     * Дожидается завершения всех задач в списке.
     */
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
                        return false; // Найдено хотя бы одно живое животное
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


