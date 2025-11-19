package com.javarush.island.zybin.island;

import com.javarush.island.zybin.entities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий одну ячейку на острове.
 * Содержит список живых сущностей, находящихся в этой ячейке.
 */

public class Cell {
    private final List<LivingEntity> entities; // список живых сущностей в ячейке
    private final int row;// номер строки ячейки на острове
    private final int col; // номер столбца ячейки на острове

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
        synchronized (entities) { //Защита при возврате списка
            return new ArrayList<>(entities); // Возвращаем копию
        }
    }

    /**
     * Добавляет сущность в ячейку, если количество не превышено.
     * Проверка лимита происходит отдельно в симуляции или в методе добавления.
     *
     * @param entity Сущность для добавления.
     */
    public void addEntity(LivingEntity entity) {
        synchronized (entities) {
            entities.add(entity);
        }
    }

    public void removeEntity(LivingEntity entity) {
        synchronized (entities) {
            entities.remove(entity);
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
        synchronized (entities) {
            return (int) entities.stream()
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .count();
        }
    }
    /**
     * Атомарно проверяет лимит и добавляет сущность, если лимит не превышен.
     * @return true, если добавление прошло успешно.
     */
    public boolean tryAddEntity(LivingEntity entity, int maxCount, Class<? extends LivingEntity> clazz) {
        synchronized (entities) {
            int currentCount = (int) entities.stream()
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .count();
            if (currentCount < maxCount) {
                entities.add(entity);
                return true;
            }
            return false;
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
