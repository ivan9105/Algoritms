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
        var zeroRows = new boolean[matrix.length];
        var zeroCols = new boolean[matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    zeroRows[i] = true;
                    zeroCols[j] = true;
                }
            }
        }

        for (int index = 0; index < zeroRows.length; index++) {
            if (zeroCols[index]) {
                for (int i = 0; i < matrix.length; i++) {
                    matrix[index][i] = 0;
                }
            }
        }

        for (int index = 0; index < zeroCols.length; index++) {
            if (zeroRows[index]) {
                for (int j = 0; j < matrix.length; j++) {
                    matrix[j][index] = 0;
                }
            }
        }
    }
}
