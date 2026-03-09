package com.algoritms.dijkstra;

import java.util.*;

public class Graph {
    private Set<Node> nodes = new HashSet<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0); //инициируем дистанцию

        var visited = new HashSet<Node>();
        var unvisited = new HashSet<Node>();

        unvisited.add(source); // инициируем обработку
        while (!unvisited.isEmpty()) {
            var lowestDistanceNode = getShortestDistanceAdjacent(unvisited); // пройтись по всем нодам получить не посещенную с минимальным значением
            unvisited.remove(lowestDistanceNode);

            for (var adjacencyEntry : lowestDistanceNode.getAdjacentNodes().entrySet()) { //bfs обход в ширину
                var adjacentNode = adjacencyEntry.getKey();
                if (!visited.contains(adjacentNode)) {
                    var weight = adjacencyEntry.getValue();
                    calculateShortestDistance(adjacentNode, weight, lowestDistanceNode);
                    unvisited.add(adjacentNode);
                }
            }

            visited.add(lowestDistanceNode);
        }

        return graph;
    }

    private Node getShortestDistanceAdjacent(Set<Node> unvisited) {
        Node result = null;
        var lowestDistance = Integer.MAX_VALUE;
        for (var node : unvisited) {
            if (lowestDistance > node.getDistance()) {
                lowestDistance = node.getDistance();
                result = node;
            }
        }
        return result;
    }

    // к примеру мы уже сделали расчет до F он например 25 через B, но оказывается что через D до F добраться быстрее - 23 - дистанция до D + вес до F
    // а F мы уже посетили, перезаписываем shortest path
    private void calculateShortestDistance(Node adjacentNode, Integer weight, Node lowestDistanceNode) {
        var lowestDistance = lowestDistanceNode.getDistance();
        if (lowestDistance + weight < adjacentNode.getDistance()) {
            adjacentNode.setDistance(lowestDistance + weight);
            var shortestPath = new LinkedList<>(lowestDistanceNode.getShortestPath());
            shortestPath.add(lowestDistanceNode);
            adjacentNode.setShortestPath(shortestPath);
        }
    }
}
