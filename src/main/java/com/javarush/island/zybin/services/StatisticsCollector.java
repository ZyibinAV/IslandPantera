package com.javarush.island.zybin.services;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.LivingEntity;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;
import com.javarush.island.zybin.entity.plants.Grass;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsCollector {

    private final Island island;
    private final Map<String, AtomicInteger> bornThisTurn = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> eatenThisTurn = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> diedThisTurn = new ConcurrentHashMap<>();

    public StatisticsCollector(Island island) {
        this.island = island;
    }

    public void resetCounters() {
        bornThisTurn.clear();
        eatenThisTurn.clear();
        diedThisTurn.clear();
    }

    public void incrementBorn(String type) {
        bornThisTurn.computeIfAbsent(type, k-> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementEaten(String type) {
        eatenThisTurn.computeIfAbsent(type, k-> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementDied(String type) {
        diedThisTurn.computeIfAbsent(type, k-> new AtomicInteger(0)).incrementAndGet();
    }

    public void printStats() {
        System.out.println("\n Статистика за текущий такт:");
        System.out.println("============================");

        Map<String, Integer> totals = new HashMap<>();
        for (int row = 0; row < island.getRows(); row++) {
            for (int col = 0; col < island.getCols(); col++) {
                Cell cell = island.getCell(row, col);
                for (LivingEntity entity : cell.getEntities()) {
                    String type = getEntityType(entity);
                    totals.merge(type, 1, Integer::sum);
                }
            }
        }
        for (var entry : totals.entrySet()) {
            System.out.printf("%-15s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.printf("\n За такт:");
        System.out.println("Рождено:");
        for (var entry : bornThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("Сьедено:");
        for (var entry : eatenThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("Умерло:");
        for (var entry : diedThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("=====================================");
    }

    private String getEntityType( LivingEntity entity ) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return "Grass";
        }
        return  "Unknown";
    }


}
