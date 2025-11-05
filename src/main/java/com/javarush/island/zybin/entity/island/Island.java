package com.javarush.island.zybin.entity.island;

/**
 * Класс, представляющий остров — двумерную сетку ячеек.
 * Размер острова можно задать при создании и в будущем расширить.
 */

public class Island {
    private Cell[][] grid; //двухмерный массив ячеек типа Cell
    private int rows;// количество строк в острове
    private int cols;// количество столбцов в острове

    /**
     * Конструктор острова с заданными размерами.
     * @param rows Количество строк (например, 5).
     * @param cols Количество столбцов (например, 5).
     */
    public Island(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        // инициализация всех ячеек
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Cell(i,j);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        if (isValidCoordinates(row, col)) {
            return grid[row][col];
        }
        return  null;// возвращаем null если координаты вне диапазона
    }

    /**
     * Проверяет, находятся ли координаты внутри границ острова.
     * @param row Номер строки.
     * @param col Номер столбца.
     * @return true, если координаты корректны.
     */
    private boolean isValidCoordinates(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    @Override
    public String toString() {
        return "Island{" +
                "rows=" + rows +
                ", cols=" + cols +
                '}';
    }
}
