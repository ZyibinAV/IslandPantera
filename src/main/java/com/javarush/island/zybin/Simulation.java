package com.javarush.island.zybin;


import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


/**
 * Основной класс симуляции жизни на острове.
 * Использует ExecutorService для многопоточного выполнения move, eat, reproduce.
 * Использует ScheduledExecutorService для периодического вывода статистики.
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

       // this.factory = new EntityFactory(island, movementController, feedingController, reproductionController);

        this.executorService = Executors.newFixedThreadPool(10);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        // Создаём ReproductionController
        this.reproductionController = new ReproductionController(island, statisticsCollector,
                movementController, feedingController);
        // Устанавливаем self-reference
        this.reproductionController.setSelfReference(this.reproductionController);
        // Создаём EntityFactory
        this.factory = new EntityFactory(island, movementController, feedingController, reproductionController);

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

                    }
                }
            }
        }
        // --- ФАЗА 1: ЕДА ---
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
        // Ждём завершения всех задач "есть"
        for (Future<?> future : eatFutures) {
            try{
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // --- ФАЗА 2: ПЕРЕМЕЩЕНИЕ ---
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
        // Ждём завершения всех задач "двигаться"
        for (Future<?> future : moveFutures) {
            try{
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // --- ФАЗА 3: РАЗМНОЖЕНИЕ ---
        List<Future<?>> reproduceFutures = new ArrayList<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                Future<?> future = executorService.submit(() -> reproductionController.reproduceInCell(cell));
                reproduceFutures.add(future);
            }
        }
        // Ждём завершения всех задач "размножаться"
        for (Future<?> future : reproduceFutures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // --- СБРОС СЫТОСТИ ---
        // Теперь можно безопасно сбросить, т.к. фаза "есть" уже завершена
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


