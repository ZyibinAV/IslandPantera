package com.javarush.island.zybin.config;

import java.util.Map;

/**
 * Класс, содержащий все настраиваемые параметры симуляции.
 * Позволяет легко изменять поведение симуляции без изменения основного кода.
 */
public class SimulationConfig {

    // --- Размеры острова ---
    public final int islandRows;
    public final int islandCols;

    // --- Параметры животных ---
    public final double hungerLossPercent; // Сколько % веса теряется за такт
    public final int initialAnimalAmount; // Базовое количество животных при расселении

    // --- Параметры травы ---
    public final int grassPerCellMultiplier; // Сколько травы создать на каждую ячейку при расселении
    public final double grassGrowthPercent; // На сколько % растёт трава каждый такт

    // --- Таблица шансов поедания ---
    public final Map<String, Map<String, Integer>> FEEDING_CHANCES;


    public SimulationConfig(int islandRows, int islandCols,
                            double hungerLossPercent, int initialAnimalAmount,
                            int grassPerCellMultiplier, double grassGrowthPercent,
                            Map<String, Map<String, Integer>> feedingChances) {
        this.islandRows = islandRows;
        this.islandCols = islandCols;
        this.hungerLossPercent = hungerLossPercent;
        this.initialAnimalAmount = initialAnimalAmount;
        this.grassPerCellMultiplier = grassPerCellMultiplier;
        this.grassGrowthPercent = grassGrowthPercent;
        this.FEEDING_CHANCES = feedingChances;
    }

    // --- Статический метод для получения конфига по умолчанию ---
    public static SimulationConfig getDefaultConfig() {
        Map<String, Map<String, Integer>> feedingChances = Map.ofEntries(
                // --- Таблица шансов как раньше ---
                Map.entry("Wolf", Map.ofEntries(
                        Map.entry("Horse", 10),
                        Map.entry("Deer", 15),
                        Map.entry("Rabbit", 60),
                        Map.entry("Mouse", 80),
                        Map.entry("Goat", 60),
                        Map.entry("Sheep", 70),
                        Map.entry("Boar", 15),
                        Map.entry("Bison", 10),
                        Map.entry("Duck", 40)
                )),
                Map.entry("Python", Map.ofEntries(
                        Map.entry("Fox", 15),
                        Map.entry("Rabbit", 20),
                        Map.entry("Mouse", 40),
                        Map.entry("Duck", 10)
                )),
                Map.entry("Fox", Map.ofEntries(
                        Map.entry("Rabbit", 70),
                        Map.entry("Mouse", 90),
                        Map.entry("Duck", 60),
                        Map.entry("Caterpillar", 40)
                )),
                Map.entry("Bear", Map.ofEntries(
                        Map.entry("Python", 80),
                        Map.entry("Horse", 40),
                        Map.entry("Deer", 80),
                        Map.entry("Rabbit", 80),
                        Map.entry("Mouse", 90),
                        Map.entry("Goat", 70),
                        Map.entry("Sheep", 70),
                        Map.entry("Boar", 50),
                        Map.entry("Bison", 20),
                        Map.entry("Duck", 10)
                )),
                Map.entry("Eagle", Map.ofEntries(
                        Map.entry("Fox", 10),
                        Map.entry("Rabbit", 90),
                        Map.entry("Mouse", 90),
                        Map.entry("Duck", 80)
                )),
                Map.entry("Horse", Map.of("Grass", 100)),
                Map.entry("Deer", Map.of("Grass", 100)),
                Map.entry("Rabbit", Map.of("Grass", 100)),
                Map.entry("Mouse", Map.ofEntries(
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Goat", Map.of("Grass", 100)),
                Map.entry("Sheep", Map.of("Grass", 100)),
                Map.entry("Boar", Map.ofEntries(
                        Map.entry("Mouse", 50),
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Bison", Map.of("Grass", 100)),
                Map.entry("Duck", Map.ofEntries(
                        Map.entry("Caterpillar", 90),
                        Map.entry("Grass", 100)
                )),
                Map.entry("Caterpillar", Map.of("Grass", 100))
        );
        return new SimulationConfig(
                120, // islandRows
                20,  // islandCols
                0.02, // hungerLossPercent (2%)
                1000, // initialAnimalAmount (рандом от этого + 10000)
                50,   // grassPerCellMultiplier
                0.10, // grassGrowthPercent (10%)
                feedingChances
        );
    }
}
