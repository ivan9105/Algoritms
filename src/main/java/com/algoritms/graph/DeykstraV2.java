package com.algoritms.graph;


import java.util.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// Класс для хранения ребра Graph
@RequiredArgsConstructor
class Edge {
    final int weight;
    final Node source, dest;
}

// Класс Node
@RequiredArgsConstructor
class Node {
    final int vertex;
}

// Класс для хранения узла кучи
@RequiredArgsConstructor
class CalculatedNode {
    final int vertex, weight;
}

@Getter
@RequiredArgsConstructor
class ShortestPath {
    private final int source;
    private final int dest;
    private final int cost;
    private final List<Integer> route;
}

// Класс для представления graphического объекта
@NoArgsConstructor
@Data
class Graph {
    // Список списков для представления списка смежности
    private List<Edge> edges = new ArrayList<>();
    private Map<Node, List<Edge>> adjacent = new HashMap<>();
    private Map<Integer, Node> nodes = new HashMap<>();

    public void addEdge(int source, int dest, int weight) {
        var sourceNode = findOrCreateNode(source);
        var destNode = findOrCreateNode(dest);

        var edge = new Edge(weight, sourceNode, destNode);
        var adjacentEdges = Optional.ofNullable(this.adjacent.get(sourceNode)).orElse(new ArrayList<>());
        adjacentEdges.add(edge);

        edges.add(edge);
        adjacent.put(sourceNode, adjacentEdges);
    }

    private Node findOrCreateNode(int source) {
        var currentNode = nodes.get(source);
        if (currentNode == null) {
            var newNode = new Node(source);
            nodes.put(source, newNode);
            return newNode;
        }

        return currentNode;
    }

    public List<Edge> getAdjacent(int source) {
        var node = nodes.get(source);
        return Optional.ofNullable(adjacent.get(node)).orElse(new ArrayList<>());
    }
}

class DeykstraV2 {
    private static void getRoute(int[] prev, int i, List<Integer> route) {
        if (i >= 0) {
            getRoute(prev, prev[i], route);
            route.add(i);
        }
    }

    //TODO выпил и сдклать возвращение метрицы
    public static int[][] findShortestPaths(Graph graph) {
        var nodes = graph.getNodes().keySet();
        var result = new int[nodes.size()][nodes.size()];

        for (int node : nodes) {
            var paths = findShortestPaths(graph, node);
            paths.forEach(path -> result[path.getSource()][path.getDest()] = path.getCost());
        }

        return result;
    }

    // Запускаем алгоритм Дейкстры на заданном Graph
    private static List<ShortestPath> findShortestPaths(Graph graph, int source) {
        var size = graph.getNodes().size() + 1;
        // создаем мини-кучу и проталкиваем исходный узел с расстоянием 0
        var minHeap = new PriorityQueue<CalculatedNode>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new CalculatedNode(source, 0));

        // устанавливаем начальное расстояние от источника на `v` как бесконечность
        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(size, Integer.MAX_VALUE));

        // расстояние от источника до себя равно нулю
        dist.set(source, 0);

        // логический массив для отслеживания вершин, для которых минимум
        // стоимость уже найдена
        boolean[] done = new boolean[size];
        done[source] = true;

        // сохраняет предыдущую вершину (в путь печати)
        int[] prev = new int[size];
        prev[source] = -1;

        // работать до тех пор, пока мини-куча не станет пустой
        while (!minHeap.isEmpty()) {
            // Удалить и вернуть лучшую вершину
            var node = minHeap.poll();

            // получаем номер вершины
            int u = node.vertex;

            // делаем для каждого соседа `v` из `u`
            for (Edge edge : graph.getAdjacent(u)) {
                int v = edge.dest.vertex;
                int weight = edge.weight;

                // Шаг релаксации
                if (!done[v] && (dist.get(u) + weight) < dist.get(v)) {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new CalculatedNode(v, dist.get(v)));
                }
            }

            // помечаем вершину `u` как выполненную, чтобы она больше не поднималась
            done[u] = true;
        }

        List<Integer> route = new ArrayList<>();
        var result = new ArrayList<ShortestPath>();
        for (int i = 0; i < size; i++) {
            if (i != source && dist.get(i) != Integer.MAX_VALUE) {
                getRoute(prev, i, route);
                System.out.printf("Path (%d —> %d): Minimum cost = %d, Route = %s\n",
                        source, i, dist.get(i), route);
                result.add(new ShortestPath(source, i, dist.get(i), route));
                route.clear();
            }
        }

        return result;
    }

    //TODO refactoring model and algorithm
    public static void main(String[] args) {
        // общее количество узлов в Graph (от 0 до 4)
        int n = 5;

        // построить Graph
        var graph = new Graph();
        graph.addEdge(0, 2, -2);
        graph.addEdge(1, 0, 4);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);
        graph.addEdge(3, 1, -1);

        // запускаем алгоритм Дейкстры с каждого узла
        findShortestPaths(graph);
    }
}
