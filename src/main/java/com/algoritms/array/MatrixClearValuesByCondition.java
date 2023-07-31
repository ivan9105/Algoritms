package com.algoritms.array;

public class MatrixClearValuesByCondition {
    private final static int ROWS = 4;
    private final static int COLS = 4;

    public static void main(String[] args) {
        int[][] matrix = new int[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = i + 10;
            }
        }

        matrix[2][2] = 0;
        matrix[3][3] = 0;

        System.out.println("Matrix before PROCESS: ");
        print(matrix);

        setZeros(matrix);

        System.out.println("Matrix after PROCESS: ");
        print(matrix);
    }

    private static void print(int[][] matrix) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void setZeros(int[][] matrix) {
        var rows_bool_table = new boolean[matrix.length];
        var cols_bool_table = new boolean[matrix[0].length];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (matrix[i][j] == 0) {
                    rows_bool_table[i] = true;
                    cols_bool_table[j] = true;
                }
            }
        }

        for (int index = 0; index < rows_bool_table.length; index++) {
            if (rows_bool_table[index]) {
                nullifyRow(matrix, index);
            }
        }

        for (int index = 0; index < cols_bool_table.length; index++) {
            if (cols_bool_table[index]) {
                nullifyColumn(matrix, index);
            }
        }
    }

    private static void nullifyRow(int[][] matrix, int row) {
        for (int index = 0; index < matrix.length; index++) {
            matrix[row][index] = 0;
        }
    }

    private static void nullifyColumn(int[][] matrix, int column) {
        for (int index = 0; index < matrix[0].length; index++) {
            matrix[index][column] = 0;
        }
    }
}
