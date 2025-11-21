package com.javarush.island.zybin.simulation;

import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.entities.LivingEntity;
import com.javarush.island.zybin.entities.plants.Grass;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Collects and manages statistical data about the island simulation.
 * <p>
 * This class is responsible for tracking various simulation metrics such as:
 * - Current population of each entity type
 * - Number of births, deaths, and feedings per turn
 * - Turn counter
 *
 * <p>Key features:
 * <ul>
 *   <li>Thread-safe statistics collection using concurrent data structures</li>
 *   <li>Real-time statistics tracking during simulation</li>
 *   <li>Periodic statistics output</li>
 *   <li>Support for dynamic entity types</li>
 *   <li>Automatic counter reset between turns</li>
 * </ul>
 *
 * <p>The collector maintains separate counters for:
 * <ul>
 *   <li>Entities born in the current turn</li>
 *   <li>Entities that were eaten in the current turn</li>
 *   <li>Entities that died of natural causes in the current turn</li>
 * </ul>
 *
 * @see Island
 * @see LivingEntity
 * @see java.util.concurrent.ConcurrentHashMap
 * @see java.util.concurrent.atomic.AtomicInteger
 */
public class StatisticsCollector {

    private final Island island;
    private final Map<String, AtomicInteger> bornThisTurn = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> eatenThisTurn = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> diedThisTurn = new ConcurrentHashMap<>();
    private final AtomicInteger currentTurn = new AtomicInteger(0);

    public StatisticsCollector(Island island) {
        this.island = island;
    }

    public void resetCounters() {
        bornThisTurn.clear();
        eatenThisTurn.clear();
        diedThisTurn.clear();
    }

    public void incrementBorn(String type) {
        bornThisTurn.computeIfAbsent(type, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementEaten(String type) {
        eatenThisTurn.computeIfAbsent(type, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementDied(String type) {
        diedThisTurn.computeIfAbsent(type, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void incrementTurn() {
        currentTurn.incrementAndGet();
    }

    public void printStats() {
        System.out.println("\n Tact: " + currentTurn.get());
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
            System.out.printf("  %s: %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("\n Per beat:");
        System.out.println("Born:");
        for (var entry : bornThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("Seated:");
        for (var entry : eatenThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("Died:");
        for (var entry : diedThisTurn.entrySet()) {
            System.out.printf(" %s: %d%n", entry.getKey(), entry.getValue().get());
        }
        System.out.println("=====================================");
    }

    private String getEntityType(LivingEntity entity) {
        if (entity instanceof Animal) {
            return ((Animal) entity).getType();
        } else if (entity instanceof Grass) {
            return "Grass";
        }
        return "Unknown";
    }
}
