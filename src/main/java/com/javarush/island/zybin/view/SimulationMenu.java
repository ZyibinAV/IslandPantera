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
                    System.out.println("Exit the program.");
                    return;
                }
                default -> System.out.println("Wrong choice. Try again.");
            }
        }
    }

    private  void printMenu() {
        System.out.println("\n===Simulation Setup Menu ===");
        System.out.println("1. Show current settings");
        System.out.println("2. Customize island size");
        System.out.println("3. Adjust weight loss from fasting (in %)");
        System.out.println("4. Set up initial number of animals");
        System.out.println("5. Adjust grass parameters (multiplier)");
        System.out.println("6. Adjust grass growth (in %)");
        System.out.println("7. Run simulation");
        System.out.println("0. Exit");
        System.out.print("Select action: ");
    }

    private int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }


    private void showCurrentConfig() {
        System.out.println("\n--- Current settings ---");
        System.out.println("Island size: " + config.islandRows + " x " + config.islandCols);
        System.out.println("Weight loss percentage: " + (config.hungerLossPercent * 100) + "%");
        System.out.println("Initial number of animals: from" + config.initialAnimalAmount + "to " + (config.initialAnimalAmount + 10000));
        System.out.println("Grass multiplier per cell: " + config.grassPerCellMultiplier);
        System.out.println("Grass growth per clock: " + (config.grassGrowthPercent * 100) + "%");
    }

    private void configureIslandSize() {
        System.out.print("Enter the number of rows (default) " + config.islandRows + "): ");
        int rows = readIntWithDefault(config.islandRows);

        System.out.print("Enter the number of columns (default " + config.islandCols + "): ");
        int cols = readIntWithDefault(config.islandCols);

        this.config = new SimulationConfig(
                rows, cols,
                config.hungerLossPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Island size changed to: " + rows + " x " + cols);
    }

    private void configureHungerLoss() {
        System.out.print("Enter the percentage of weight loss per cycle (default " + (config.hungerLossPercent * 100) + "%): ");
        double percent = readDoubleWithDefault(config.hungerLossPercent * 100);
        double newPercent = percent / 100.0;

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                newPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Weight loss percentage changed to: " + percent + "%");
    }

    private void configureInitialAnimals() {
        System.out.print("Enter the base number of animals when moving (default " + config.initialAnimalAmount + "): ");
        int amount = readIntWithDefault(config.initialAnimalAmount);

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, amount,
                config.grassPerCellMultiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Base number of animals changed to: " + amount);
    }

    private void configureGrassSettings() {
        System.out.print("Enter a multiplier for the amount of grass per cell (default " + config.grassPerCellMultiplier + "): ");
        int multiplier = readIntWithDefault(config.grassPerCellMultiplier);

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, config.initialAnimalAmount,
                multiplier, config.grassGrowthPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Grass multiplier changed to: " + multiplier);
    }

    private void configureGrassGrowth() {
        System.out.print("Enter the percentage of grass growth per tick (default" + (config.grassGrowthPercent * 100) + "%): ");
        double percent = readDoubleWithDefault(config.grassGrowthPercent * 100);
        double newPercent = percent / 100.0;

        this.config = new SimulationConfig(
                config.islandRows, config.islandCols,
                config.hungerLossPercent, config.initialAnimalAmount,
                config.grassPerCellMultiplier, newPercent,
                config.FEEDING_CHANCES
        );
        System.out.println("Grass growth percentage changed to:" + percent + "%");
    }

    private void startSimulation() {
        System.out.println("\nRunning the simulation with current settings...");
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
            System.out.println("Invalid input. The default value is used: " + defaultValue);
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
            System.out.println("Invalid input. The default value is used: " + defaultValue);
            return defaultValue;
        }
    }
}
