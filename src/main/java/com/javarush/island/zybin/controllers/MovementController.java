package com.javarush.island.zybin.controllers;

import com.javarush.island.zybin.entities.Animal;
import com.javarush.island.zybin.island.Cell;
import com.javarush.island.zybin.island.Island;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;

/**
 * Класс, отвечающий за логику передвижения животных по острову.
 * Использует многопоточный Random (ThreadLocalRandom).
 * * Предотвращает дедлок при захвате мониторов ячеек.
 */

public class MovementController {

    private final Island island;

    public MovementController(Island island) {
        this.island = island;
    }

    /**
     * Метод, который перемещает животное в случайную ячейку в пределах его скорости (speed).
     *
     * @param animal      Животное, которое нужно переместить.
     * @param currentCell Ячейка, в которой животное находится сейчас.
     */
    public void moveAnimal(Animal animal, Cell currentCell) {
        if (!animal.isAlive()) {
            return; // Если животное не живо, то оно не может двигаться.
        }
        int speed = animal.getSpeed();
        // Случайное количество ячеек, на которое животное может пройти (от 0 до speed)
        int steps = ThreadLocalRandom.current().nextInt(0, speed + 1);
        if (steps == 0) {
            return; // Если животное не двигается, то оно остается на месте.
        }
        // Получаем текущие координаты
        int currentRow = currentCell.getRow();
        int currentCol = currentCell.getCol();
        // Генерируем смещение: от -steps до +steps
        int deltaRow = ThreadLocalRandom.current().nextInt(-steps, steps + 1);
        int deltaCol = ThreadLocalRandom.current().nextInt(-steps, steps + 1);
        // Вычисляем новую позицию
        int newRow = currentRow + deltaRow;
        int newCol = currentCol + deltaCol;
        // Проверяем, не выходит ли новая позиция за границы острова
        if (newRow < 0 || newRow >= island.getRows() || newCol < 0 || newCol >= island.getCols()) {
            return; //животное не может выйти за границы - остается на месте
        }
        Cell targetCell = island.getCell(newRow, newCol);
        // Проверяем, не превышено ли максимальное количество животных в целевой ячейке
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
