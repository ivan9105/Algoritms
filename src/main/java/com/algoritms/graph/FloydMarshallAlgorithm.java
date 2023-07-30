package com.algoritms.graph;

import java.util.ArrayList;
import java.util.List;

public class FloydMarshallAlgorithm {

    // Функция для вывода кратчайшей стоимости с информацией о пути между
    // все пары вершин
    private static void printSolution(int[][] path, int n) {
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                if (u != v && path[v][u] != -1) {
                    List<Integer> route = new ArrayList<>();
                    route.add(v);
                    printPath(path, v, u, route);
                    route.add(u);
                    System.out.printf("The shortest path from %d —> %d is %s\n",
                            v, u, route);
                }
            }
        }
    }

    private static void printPath(int[][] path, int v, int u, List<Integer> route) {
        if (path[v][u] == v) {
            return;
        }
        printPath(path, v, path[v][u], route);
        route.add(path[v][u]);
    }

    // Функция для запуска алгоритма Флойда-Уоршалла
    public static void floydWarshall(int[][] adjMatrix) {
        // базовый вариант
        if (adjMatrix == null || adjMatrix.length == 0) {
            return;
        }

        // общее количество вершин в `adjMatrix`
        int n = adjMatrix.length;

        // cost[] и path[] сохраняют кратчайший путь
        // информация (кратчайшая стоимость/кратчайший маршрут)
        int[][] cost = new int[n][n];
        int[][] path = new int[n][n];

        // инициализируем cost[] и path[]
        for (int v = 0; v < n; v++) {
            for (int u = 0; u < n; u++) {
                // изначально стоимость будет равна весу ребра
                cost[v][u] = adjMatrix[v][u];

                if (v == u) {
                    path[v][u] = 0;
                } else if (cost[v][u] != Integer.MAX_VALUE) {
                    path[v][u] = v;
                } else {
                    path[v][u] = -1;
                }
            }
        }

        // запускаем Флойда-Уоршалла
        for (int k = 0; k < n; k++) {
            for (int v = 0; v < n; v++) {
                for (int u = 0; u < n; u++) {
                    // Если вершина `k` находится на кратчайшем пути из `v` в `u`,
                    // затем обновить значение cost[v][u] и path[v][u]

                    if (cost[v][k] != Integer.MAX_VALUE
                            && cost[k][u] != Integer.MAX_VALUE
                            && (cost[v][k] + cost[k][u] < cost[v][u])) {
                        cost[v][u] = cost[v][k] + cost[k][u];
                        path[v][u] = path[k][u];
                    }
                }

                // если диагональные элементы становятся отрицательными,
                // graph содержит цикл отрицательного веса
                if (cost[v][v] < 0) {
                    System.out.println("Negative-weight cycle found!!");
                    return;
                }
            }
        }

        // Выводим кратчайший путь между всеми парами вершин
        printSolution(path, n);
    }

    public static void main(String[] args) {
        // определить бесконечность
        int I = Integer.MAX_VALUE;

        // заданное представление смежности матрицы
        int[][] adjMatrix = new int[][]
                {
                        {0, I, -2, I},
                        {4, 0, 3, I},
                        {I, I, 0, 2},
                        {I, -1, I, 0}
                };

        // Запуск алгоритма Флойда-Уоршалла
        floydWarshall(adjMatrix);
    }
}
