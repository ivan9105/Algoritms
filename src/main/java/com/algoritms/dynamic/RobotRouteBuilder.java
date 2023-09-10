package com.algoritms.dynamic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Робот стоит в левом верхнем углу сетки, состоящей из r строк и с столбцов.
 * Робот может перемещаться в двух направлениях: вправо и вниз, но некоторые
 * ячейки сетки заблокированы, то есть робот через них проходить не может.
 * Разработайте алгоритм построения маршрута от левого верхнего до правого
 * нижнего угла.
 * 357
 */
public class RobotRouteBuilder {
    public static void main(String[] args) {
        // given
        int x = 15;
        int y = 15;

        boolean[][] maze = new boolean[x + 1][y + 1];
        maze[0][6] = true;
        maze[1][4] = true;
        maze[3][7] = true;
        maze[6][10] = true;
        maze[8][8] = true;
        maze[14][10] = true;

        var cache = new HashMap<Point, Boolean>();
        var path = new ArrayList<Point>();

        getRoute(path, x, y, maze, cache);

        System.out.printf("The path for x: %s, y: %s is %s %n", x, y, path);
    }

    private static boolean getRoute(List<Point> path, int x, int y, boolean[][] maze, HashMap<Point, Boolean> cache) {
        if (x < 0 || y < 0 || maze[x][y]) {
            return false;
        }

        var point = new Point(x, y);
        if (cache.containsKey(point)) {
            return cache.get(point);
        }

        boolean isOrigin = (x == 0) && (y == 0);
        boolean success = false;

        if (isOrigin || getRoute(path, x, y - 1, maze, cache) || getRoute(path, x - 1, y, maze, cache)) {
            path.add(point);
            success = true;
        }

        cache.put(point, success);
        return success;
    }

    @Data
    @RequiredArgsConstructor
    private static class Point {
        private final int x;
        private final int y;
    }
}
