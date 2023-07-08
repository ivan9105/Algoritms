package com.algoritms.graph;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class BFS {
    public static void main(String[] args) {
        var graph = new Graph();
        graph.addNodes("0", List.of("1", "2"));
        graph.addNodes("1", List.of("2", "3", "0"));
        graph.addNodes("2", List.of("1", "4", "0"));
        graph.addNodes("3", List.of("1", "4"));
        graph.addNodes("4", List.of("2", "3"));

        System.out.println("Дано: " + graph.toString());

        //non recursive
        var passed = new ArrayList<Node>();
        Queue<Node> queue = new LinkedList<>();

        queue.offer(graph.getRoot());

        while (!queue.isEmpty()) {
            var current = queue.poll();

            if (!passed.contains(current)) {
                System.out.println("Пройдена node: " + current);
                passed.add(current);
            }

            for (Node adjacent : current.getAdjacent()) {
                if (!passed.contains(adjacent)) {
                    queue.offer(adjacent);
                }
            }
        }

    }

    @Data
    @NoArgsConstructor
    private static class Graph {
        private final Map<String, Node> nodes = new HashMap<>();
        private Node root = null;

        public void addEdge(String target, String adjacent) {
            addNodes(target, List.of(adjacent));
        }

        public void addNodes(String target, List<String> adjacent) {
            var targetNode = findOrCreateNode(target);

            if (adjacent != null && adjacent.size() > 0) {
                adjacent.forEach(it -> targetNode.getAdjacent().add(findOrCreateNode(it)));
            }
        }

        private Node findOrCreateNode(String value) {
            var targetNode = nodes.get(value);
            if (targetNode != null) {
                return targetNode;
            }

            targetNode = new Node(value, new LinkedHashSet<>());
            nodes.put(value, targetNode);

            if (root == null) {
                root = targetNode;
            }

            return targetNode;
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();

            var keys = nodes.keySet().stream().sorted().collect(toList());

            for (String key : keys) {
                sb.append(nodes.get(key).toString()).append("\n");
            }

            return sb.toString();
        }
    }


    @Getter
    @Setter
    @EqualsAndHashCode(exclude = "adjacent")
    @RequiredArgsConstructor
    @Builder
    private static class Node {
        private final String value;
        private final Set<Node> adjacent;

        @Override
        public String toString() {
            if (adjacent.isEmpty()) {
                return format("Node[value=%s]", value);
            }

            return format("Node[value=%s, adjacent=[%s]]",
                    value,
                    adjacent.stream().map(Node::getValue).collect(joining(","))
            );
        }
    }
}
