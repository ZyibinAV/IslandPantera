package com.javarush.island.zybin;


import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.services.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Основной класс симуляции жизни на острове.
 * Использует ExecutorService для многопоточного выполнения move, eat, reproduce.
 * Использует ScheduledExecutorService для периодического вывода статистики.
 */

public class Simulation {

    private final Island island;
    private final EntityFactory factory;
    private final MovementController movementController;
    private final FeedingController feedingController;
    private final ReproductionController reproductionController;
    private final StatisticsCollector statisticsCollector;

    private final ExecutorService executorService;
    private final ScheduledExecutorService scheduledExecutorService;

    private volatile boolean isRunning = true;

    public Simulation(Island island) {
        this.island = island;
        this.factory - new EntityFactory(island);
        this.statisticsCollector = new StatisticsCollector(island);
        this.movementController = new MovementController(island);
        this.feedingController = new FeedingController(island, statisticsCollector);
        this.reproductionController = new ReproductionController(island, statisticsCollector);

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

    private void runTurn() {
        // Сброс счётчиков статистики в начале такта
        statisticsCollector.resetCounters();

        // 1. Животные уменьшают вес (голод)
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal) {
                        animal.reduceWeight();
                        e
                    }
                }
            }
        }
        // 2. Животные едят
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal && animal.isAlive()) {
                        animal.setFeedingController(feedingController);
                        executorService.submit(() -> animal.eat(cell));
                    }
                }
            }
        }
        // 3. Животные перемещаются
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    if (entity instanceof Animal animal && animal.isAlive()) {
                        animal.setMovementController(movementController);
                        executorService.submit(() -> animal.move(cell));
                    }
                }
            }
        }
        // 4. Размножение
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                executorService.submit(() -> reproductionController.reproduceInCell(cell));
            }
        }
        // 5. Сброс счётчиков насыщения
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


