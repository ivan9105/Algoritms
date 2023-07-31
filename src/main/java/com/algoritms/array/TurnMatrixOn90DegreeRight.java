package com.algoritms.array;

public class TurnMatrixOn90DegreeRight {
    private final static int ROWS = 4;
    private final static int COLS = 4;

    public static void main(String[] args) {
        int[][] matrix = new int[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                matrix[i][j] = i;
            }
        }

        System.out.println("Matrix before rotation: ");
        print(matrix);

        rotate(matrix);

        System.out.println("Matrix after rotation: ");
        print(matrix);
    }

    private static void rotate(int[][] matrix) {
        var layersCount = ROWS / 2;
        for (int layer = 0; layer < layersCount; layer++) {
            int first = layer;
            int last = ROWS - layer - 1;
            for (int index = first; index < last; index++) {
                int offset = index - first;

                int top = matrix[first][index];

                //left to top
                matrix[first][index] = matrix[last - offset][first];

                //bottom to left
                matrix[last - offset][first] = matrix[last][last - offset];

                //right to bottom
                matrix[last][last - offset] = matrix[index][last];


                //top to right
                matrix[index][last] = top;
            }
        }
    }

    private static void print(int[][] matrix) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
