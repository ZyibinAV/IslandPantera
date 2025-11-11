package com.javarush.island.zybin.services;

import com.javarush.island.zybin.entity.Animal;
import com.javarush.island.zybin.entity.island.Cell;
import com.javarush.island.zybin.entity.island.Island;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс, отвечающий за логику передвижения животных по острову.
 * Использует многопоточный Random (ThreadLocalRandom).
 */

public class MovementController {

    private final Island island;

    public MovementController(Island island) {
        this.island = island;
    }
    /**
     * Метод, который перемещает животное в случайную ячейку в пределах его скорости (speed).
     * @param animal Животное, которое нужно переместить.
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
        if (newRow < 0 || newRow >= island.getRows()|| newCol < 0 || newCol >= island.getCols()) {
            return; //животное не может выйти за границы - остается на месте
        }
        Cell targetCell = island.getCell(newRow, newCol);
        // Проверяем, не превышено ли максимальное количество животных в целевой ячейке
        if (targetCell.getCountByType(animal.getClass()) < animal.getMaxCountInCell()) {
            // Перемещаем животное: удаляем из старой ячейки, добавляем в новую
            synchronized (currentCell) {
                if (currentCell.getEntities().contains(animal)) {
                    currentCell.removeEntity(animal);
                }
            }
            synchronized (targetCell) {
                if (targetCell.getCountByType(animal.getClass()) < animal.getMaxCountInCell()) {
                    targetCell.addEntity(animal);
                } else {
                    // Если за время между проверкой и синхронизацией лимит превысили — возвращаем в старую
                    synchronized (currentCell) {
                        currentCell.addEntity(animal);
                    }
                }
            }
        }
    }
}
