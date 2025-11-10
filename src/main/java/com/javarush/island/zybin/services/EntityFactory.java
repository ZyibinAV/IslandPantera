package com.javarush.island.zybin.services;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.herbivore.*;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.entity.plants.Grass;
import com.javarush.island.zybin.entity.predator.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Класс-фабрика для создания и распределения сущностей по острову.
 * Использует паттерн "Фабрика" и многопоточную рандомизацию.
 */

public class EntityFactory {
    private final Island island;
    private final Random random = new Random();
    private final Map<String, Integer> statistics = new HashMap<>(); // статистика по типу сущности

    /**
     * Конструктор фабрики.
     *
     * @param island Остров, на котором будут размещаться сущности.
     */
    public EntityFactory(Island island) {
        this.island = island;
    }

    /**
     * Метод для запуска процесса создания и распределения сущностей по острову.
     * Создаёт и размещает животных и растения (траву) в случайных ячейках.
     */
    public void populateIsland() {
        List<Class<? extends LivingEntity>> entityTypes = Arrays.asList(
                Bison.class, Boar.class, Caterpillar.class, Deer.class, Duck.class, Goat.class,
                Horse.class, Mouse.class, Rabbit.class, Sheep.class, Bear.class, Eagle.class,
                Fox.class, Python.class, Wolf.class, Grass.class
        );
        // Запускаем создание животных
        for (Class<? extends LivingEntity> entityType : entityTypes) {
            int created = createAndDistribute(entityType);
            String typeName = getSimpleTypeName(entityType);
            statistics.put(typeName, created);
        }
        printStatistics();

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
        int totalToCreate = random.nextInt(1000) + 50;
        AtomicInteger placedCount = new AtomicInteger(0);
        // Используем ExecutorService для многопоточного размещения
        ExecutorService executor = Executors.newFixedThreadPool(8);// 8 потоков
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < totalToCreate; i++) {
            Future<?> future = executor.submit(() -> {
                try {
                    // создаем обьект сущности
                    T entity = clazz.getDeclaredConstructor().newInstance();
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
                    if (cell.getCountByType(clazz) < maxCount) {
                            if (cell.getCountByType(clazz) < maxCount) {
                                cell.addEntity(entity);
                                placedCount.incrementAndGet();
                            }
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
     * @param entity Экземпляр сущности.
     * @return Значение поля type.
     */
    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal)entity).getType();
        } else if (entity instanceof Grass) {
            return ((Grass)entity).getType();
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
