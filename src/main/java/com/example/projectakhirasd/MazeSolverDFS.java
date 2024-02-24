package com.example.projectakhirasd;

import java.util.ArrayList;
import java.util.List;

public class MazeSolverDFS {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    int[][] maze;
    boolean[][] visited;
    int endCol;
    int endRow;
    private List<Point> path;
    boolean result;

    public MazeSolverDFS(int[][] maze) {
        this.maze = maze;
        this.visited = new boolean[ROWS][COLS];
    }

    public List<Point> getPath() {
        return path;
    }

    public void solveMaze(int startRow, int startCol, int endRow, int endCol) {
        path = new ArrayList<>();
        if (dfs(startRow, startCol, endRow, endCol)) {
            System.out.println("Maze solved!");
            result = true;
            printSolution();
        } else {
            System.out.println("No solution found.");
            result = false;
        }
    }

    private boolean dfs(int row, int col, int endRow, int endCol) {
        this.endRow = endRow;
        this.endCol = endCol;

        if (row < 0 || row >= ROWS || col < 0 || col >= COLS || maze[row][col] == 1 || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;

        path.add(new Point(row, col));

        if (row == endRow && col == endCol) {
            return true;
        }

        // 4 possible movements: up, down, left, right
        int[] rowMovement = {-1, 1, 0, 0};
        int[] colMovement = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + rowMovement[i];
            int newCol = col + colMovement[i];

            if (dfs(newRow, newCol, endRow, endCol)) {
                return true;
            }
        }

        path.remove(path.size() - 1);

        return false;
    }

    private void printSolution() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (i == this.endRow && j == this.endCol) {
                    System.out.print("2 ");
                } else if (visited[i][j]) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

}

