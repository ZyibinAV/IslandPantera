package com.javarush.island.zybin.island;

import com.javarush.island.zybin.entities.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Represents a single cell on the island grid in the simulation.
 * <p>
 * This class is responsible for managing the living entities within a specific cell
 * of the island. It provides thread-safe operations for adding, removing, and querying
 * entities while maintaining data consistency through reentrant locks.
 *
 * <p>Key features:
 * <ul>
 *   <li>Thread-safe entity management using ReentrantLock</li>
 *   <li>Support for concurrent access from multiple threads</li>
 *   <li>Type-based entity counting and filtering</li>
 *   <li>Bounded capacity control for entity population</li>
 *   <li>Safe iteration over entities</li>
 * </ul>
 *
 * @see LivingEntity
 * @see java.util.concurrent.locks.ReentrantLock
 */
public class Cell {
    private final List<LivingEntity> entities;
    private final int row;
    private final int col;
    private final ReentrantLock lock = new ReentrantLock();

    public Cell(int row, int col) {
        this.entities = new ArrayList<>();
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public List<LivingEntity> getEntities() {
        lock.lock();
        try {
            return new ArrayList<>(entities);
        } finally {
            lock.unlock();
        }
    }

    public void addEntity(LivingEntity entity) {
        lock.lock();
        try {
            entities.add(entity);
        } finally {
            lock.unlock();
        }
    }

    public void removeEntity(LivingEntity entity) {
        lock.lock();
        try {
            entities.remove(entity);
        } finally {
            lock.unlock();
        }
    }

    public int getCountByType(Class<?> clazz) {
        lock.lock();
        try {
            return (int) entities.stream()
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .count();
        } finally {
            lock.unlock();
        }
    }

    public boolean tryAddEntity(LivingEntity entity, int maxCount, Class<? extends LivingEntity> clazz) {
        lock.lock();
        try {
            int currentCount = (int) entities.stream()
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .count();
            if (currentCount < maxCount) {
                entities.add(entity);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", entities=" + entities.size() +
                '}';
    }
}
