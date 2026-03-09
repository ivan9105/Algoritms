package com.algoritms.dijkstra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Node {
    private String name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    private Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(String name) {
        this.name = name;
    }
}
