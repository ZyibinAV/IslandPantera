package com.javarush.island.zybin.island;



/**
 * Represents the island in the simulation as a two-dimensional grid of cells.
 * <p>
 * This class serves as the main container for the simulation's environment,
 * providing access to individual cells and managing the spatial organization
 * of living entities. The island is initialized with a fixed number of rows
 * and columns, each containing a Cell instance.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>Managing the grid of cells that make up the island</li>
 *   <li>Providing access to cells by coordinates</li>
 *   <li>Validating coordinates to ensure they are within island boundaries</li>
 *   <li>Maintaining the spatial structure of the simulation</li>
 * </ul>
 *
 * @see Cell
 * @see com.javarush.island.zybin.entities.LivingEntity
 */
public class Island {
    private Cell[][] grid;
    private int rows;
    private int cols;

    public Island(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
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
        return  null;
    }

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
