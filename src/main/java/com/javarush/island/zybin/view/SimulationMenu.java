package com.javarush.island.zybin.view;

import com.javarush.island.zybin.config.SimulationConfig;
import com.javarush.island.zybin.simulation.Simulation;

import java.util.Scanner;

/**
 * Provides a console-based user interface for configuring and controlling the simulation.
 * <p>
 * This class implements an interactive menu system that allows users to:
 * - View current simulation settings
 * - Configure various simulation parameters
 * - Start the simulation with the chosen settings
 *
 * <p>Configuration options include:
 * <ul>
 *   <li>Island dimensions (rows x columns)</li>
 *   <li>Hunger loss percentage per turn</li>
 *   <li>Initial animal population</li>
 *   <li>Grass growth and distribution parameters</li>
 * </ul>
 *
 * <p>Key features:
 * <ul>
 *   <li>Interactive console-based menu</li>
 *   <li>Input validation with default values</li>
 *   <li>Real-time configuration preview</li>
 *   <li>Safe input handling</li>
 * </ul>
 *
 * @see SimulationConfig
 * @see Simulation
 */
public class SimulationMenu {

    private SimulationConfig config;
    private final Scanner scanner;

    public SimulationMenu() {
        this.config = SimulationConfig.getDefaultConfig();
        this.scanner = new Scanner(System.in);
    }

    public void startMenu() {
        while (true) {
            printMenu();
            int choice = readChoice();

            switch (choice) {
                case 1 -> showCurrentConfig();
                case 2 -> configureIslandSize();
                case 3 -> configureHungerLoss();
                case 4 -> configureInitialAnimals();
                case 5 -> configureGrassSettings();
                case 6 -> configureGrassGrowth();
                case 7 -> startSimulation();
                case 0 -> {
                    System.out.println("Выход из программы.");
                    return;
                }
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private  void printMenu() {
        System.out.println("\n=== Меню настройки симуляции ===");
        System.out.println("1. Показать текущие настройки");
        System.out.println("2. Настроить размер острова");
        System.out.println("3. Настроить потерю веса от голода (в %)");
        System.out.println("4. Настроить начальное количество животных");
        System.out.println("5. Настроить параметры травы (множитель)");
        System.out.println("6. Настроить рост травы (в %)");
        System.out.println("7. Запустить симуляцию");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private void showCurrentConfig() {
        System.out.println("\n--- Текущие настройки ---");
        System.out.println("Размер острова: " + config.islandRows + " x " + config.islandCols);
        System.out.println("Процент потери веса: " + (config.hungerLossPercent * 100) + "%");
        System.out.println("Начальное кол-во животных: от " + config.initialAnimalAmount + " до " + (config.initialAnimalAmount + 10000));
        System.out.println("Множитель травы на ячейку: " + config.grassPerCellMultiplier);
        System.out.println("Рост травы за такт: " + (config.grassGrowthPercent * 100) + "%");
    }

    private void configureIslandSize() {
        System.out.print("Введите количество строк (по умолчанию " + config.islandRows + "): ");
        int rows = readIntWithDefault(config.islandRows);

        System.out.print("Введите количество столбцов (по умолчанию " + config.islandCols + "): ");
        int cols = readIntWithDefault(config.islandCols);

        this.config = new SimulationConfig(
                rows, cols,
                config.hungerLossPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Размер острова изменён на: " + rows + " x " + cols);
    }

    private void configureHungerLoss() {
        System.out.print("Введите процент потери веса за такт (по умолчанию " + (config.hungerLossPercent * 100) + "%): ");
        double percent = readDoubleWithDefault(config.hungerLossPercent * 100);
        double newPercent = percent / 100.0;

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                newPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Процент потери веса изменён на: " + percent + "%");
    }

    private void configureInitialAnimals() {
        System.out.print("Введите базовое количество животных при расселении (по умолчанию " + config.initialAnimalAmount + "): ");
        int amount = readIntWithDefault(config.initialAnimalAmount);

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, amount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Базовое количество животных изменено на: " + amount);
    }

    private void configureGrassSettings() {
        System.out.print("Введите множитель количества травы на ячейку (по умолчанию " + config.grassPerCellMultiplier + "): ");
        int multiplier = readIntWithDefault(config.grassPerCellMultiplier);

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, config.initialAnimalAmount,
                multiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Множитель травы изменён на: " + multiplier);
    }

    private void configureGrassGrowth() {
        System.out.print("Введите процент роста травы за такт (по умолчанию " + (config.grassGrowthPercent * 100) + "%): ");
        double percent = readDoubleWithDefault(config.grassGrowthPercent * 100);
        double newPercent = percent / 100.0;

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, newPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Процент роста травы изменён на: " + percent + "%");
    }

    private void startSimulation() {
        System.out.println("\nЗапуск симуляции с текущими настройками...");
        Simulation simulation = new Simulation(config);
        simulation.start();
    }

    private int readIntWithDefault(int defaultValue) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод. Используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }

    private double readDoubleWithDefault(double defaultValue) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод. Используется значение по умолчанию: " + defaultValue);
            return defaultValue;
        }
    }
}
