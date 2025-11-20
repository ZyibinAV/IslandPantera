package com.javarush.island.zybin.island;

import com.javarush.island.zybin.entities.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс, представляющий одну ячейку на острове.
 * Содержит список живых сущностей, находящихся в этой ячейке.
 */

public class Cell {
    private final List<LivingEntity> entities; // список живых сущностей в ячейке
    private final int row;// номер строки ячейки на острове
    private final int col; // номер столбца ячейки на острове
    private  final ReentrantLock lock = new ReentrantLock();

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
    /**
     * Возвращает копию списка сущностей для безопасной итерации.
     */
    public List<LivingEntity> getEntities() {
         lock.lock();//Защита при возврате списка
        try{
          return  new ArrayList<>(entities); // Возвращаем копию
        } finally {
            lock.unlock();
        }


    }

    /**
     * Добавляет сущность в ячейку, если количество не превышено.
     * Проверка лимита происходит отдельно в симуляции или в методе добавления.
     *
     * @param entity Сущность для добавления.
     */
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

    /**
     * Подсчитывает количество сущностей определённого типа (или его подтипов) в текущей ячейке.
     * Использует стримы для фильтрации: оставляет только те сущности, тип которых соответствует clazz.
     *
     * @param clazz Класс сущности, количество которой нужно подсчитать (например, Grass.class).
     * @return Количество сущностей этого типа в ячейке.
     */
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
    /**
     * Атомарно проверяет лимит и добавляет сущность, если лимит не превышен.
     * @return true, если добавление прошло успешно.
     */
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
