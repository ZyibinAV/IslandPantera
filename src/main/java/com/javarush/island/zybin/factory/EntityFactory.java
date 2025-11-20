package com.javarush.island.zybin.factory;

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
 * Класс-фабрика для создания и распределения сущностей по острову.
 * Использует паттерн "Фабрика" и многопоточную рандомизацию.
 * Учитывает поле maxCountInCell у каждой сущности.
 */

public class EntityFactory {
    private final Island island;
    private final Random random = new Random();
    private final MovementController movementController;
    private final FeedingController feedingController;
    private final ReproductionController reproductionController;

   private final Map<String, Integer> statistics = new HashMap<>(); // статистика по типу сущности

    /**
     * Конструктор фабрики.
     *
     * @param island Остров, на котором будут размещаться сущности.
     */
    public EntityFactory(Island island, MovementController movementController,
                         FeedingController feedingController, ReproductionController reproductionController) {
        this.island = island;
        this.movementController = movementController;
        this.feedingController = feedingController;
        this.reproductionController = reproductionController;
    }

    /**
     * Метод для запуска процесса создания и распределения сущностей по острову.
     * Создаёт и размещает животных и растения (траву) в случайных ячейках.
     */
    public void populateIsland() {
        List<Class<? extends LivingEntity>> entityTypes = Arrays.asList(Bison.class,
                Boar.class, Caterpillar.class, Deer.class, Duck.class, Goat.class,
                Horse.class, Mouse.class, Rabbit.class, Sheep.class, Bear.class,
                Eagle.class, Fox.class, Python.class, Wolf.class, Grass.class);
        // Запускаем создание животных
        for (Class<? extends LivingEntity> entityType : entityTypes) {
            int created = createAndDistribute(entityType);
            String typeName = getSimpleTypeName(entityType);
            statistics.put(typeName, created);
        }

        int grassCount = createAndDistribute(Grass.class);
        statistics.put("Grass", grassCount);

        // --- Вызовем вывод статистики ---
        printStatistics();
        // --- /Вызовем ---

    }

    /**
     * Метод, который создаёт сущности и распределяет их по ячейкам острова.
     * Использует многопоточную рандомизацию.
     *
     * @param clazz Класс сущности (например, Wolf.class).
     * @return Количество успешно созданных и размещенных сущностей.
     */
    private <T extends LivingEntity> int createAndDistribute(Class<T> clazz) {
        // создаем случайное количество сущностей для этого типа
        int totalToCreate = random.nextInt(10000) + 1000 ;
        if (clazz == Grass.class) {
            totalToCreate = island.getRows() * island.getCols() * 150;
        }
        AtomicInteger placedCount = new AtomicInteger(0);
        // Используем ExecutorService для многопоточного размещения
        ExecutorService executor = Executors.newFixedThreadPool(10);// 10 потоков
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < totalToCreate; i++) {
            Future<?> future = executor.submit(() -> {
                try {
                    // создаем обьект сущности
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    if (entity instanceof Animal animal) {
                        animal.setMovementController(movementController);
                        animal.setFeedingController(feedingController);
                        animal.setReproductionController(reproductionController);
                    } else if (entity instanceof  Grass grass) {
                        grass.setReproductionController(reproductionController);
                    }
                    // получаем тип сущности из поля type
                    String entityType = getEntityType(entity);
                    // выбираем случайную ячейку на острове
                    int row = random.nextInt(island.getRows());
                    int col = random.nextInt(island.getCols());
                    Cell cell = island.getCell(row, col);
                    // Проверяем, не превышено ли максимальное количество сущностей в ячейке
                    int maxCount = -1;
                    if (entity instanceof Animal) {
                        maxCount = ((Animal) entity).getMaxCountInCell();
                    } else if (entity instanceof Grass) {
                        maxCount = ((Grass) entity).getMaxCountInCell();
                    }

                    if (maxCount == -1) {
                        // Неизвестный тип сущности — пропускаем
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
        // ждем завершения всех задач
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return placedCount.get(); // возвращаем количество успешно размещенных сущностей
    }

    /**
     * Вспомогательный метод для получения типа сущности из поля type.
     *
     * @param entity Экземпляр сущности.
     * @return Значение поля type.
     */
    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return ((Grass) entity).getType();
        }
        return "Unknown";
    }

    /**
     * Возвращает простое имя класса (например, "Wolf", "Grass").
     */
    private String getSimpleTypeName(Class<? extends LivingEntity> clazz) {
        return clazz.getSimpleName();
    }

    /**
     * Выводит статистику по размещенным сущностям в консоль.
     */
    private void printStatistics() {
        System.out.println("\n Статистика размещённых сущностей:");
        System.out.println("=====================================");
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("=====================================");
    }

}
