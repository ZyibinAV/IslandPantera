package com.javarush.island.zybin.controllers;

import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;



/**
 * Controller responsible for managing the movement mechanics of animals on the island.
 * <p>
 * This class handles the random movement of animals across the island's grid while ensuring
 * thread-safety through proper lock ordering to prevent deadlocks. It respects the maximum
 * number of animals allowed per cell and the movement speed of each animal.
 *
 * <p>Key features:
 * <ul>
 *   <li>Random movement within the animal's speed limit</li>
 *   <li>Boundary checking to prevent moving outside the island</li>
 *   <li>Thread-safe cell-to-cell transfers using lock ordering</li>
 *   <li>Population control per cell based on animal type</li>
 * </ul>
 *
 * @see Animal
 * @see Cell
 * @see Island
 */
public class MovementController {

    private final Island island;

    public MovementController(Island island) {
        this.island = island;
    }

    public void moveAnimal(Animal animal, Cell currentCell) {
        if (!animal.isAlive()) {
            return;
        }
        int speed = animal.getSpeed();
        int steps = ThreadLocalRandom.current().nextInt(0, speed + 1);
        if (steps == 0) {
            return;
        }
        int currentRow = currentCell.getRow();
        int currentCol = currentCell.getCol();
        int deltaRow = ThreadLocalRandom.current().nextInt(-steps, steps + 1);
        int deltaCol = ThreadLocalRandom.current().nextInt(-steps, steps + 1);
        int newRow = currentRow + deltaRow;
        int newCol = currentCol + deltaCol;
        if (newRow < 0 || newRow >= island.getRows() || newCol < 0 || newCol >= island.getCols()) {
            return;
        }
        Cell targetCell = island.getCell(newRow, newCol);
        if (targetCell.getCountByType(animal.getClass()) >= animal.getMaxCountInCell()) {
            return;
        }
        Lock firstLock = currentCell.getLock();
        Lock secondLock = targetCell.getLock();

        if (System.identityHashCode(currentCell) > System.identityHashCode(targetCell)) {
            firstLock = targetCell.getLock();
            secondLock = currentCell.getLock();
        }

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (currentCell.getEntities().contains(animal) &&
                        targetCell.getCountByType(animal.getClass()) < animal.getMaxCountInCell()) {
                    currentCell.removeEntity(animal);
                    targetCell.addEntity(animal);
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
