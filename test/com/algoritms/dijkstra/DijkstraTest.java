package com.algoritms.dijkstra;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;

public class DijkstraTest {
    @Test
    public void basicTest() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);
        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeC.addDestination(nodeE, 10);
        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeF.addDestination(nodeE, 5);

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);

        graph = graph.calculateShortestPathFromSource(graph, nodeA);

        for (Node node : graph.getNodes()) {
            checkPath(node,"F", "A, B, D");
            checkPath(node,"C", "A");
            checkPath(node, "D", "A, B");
            checkPath(node, "E", "A, B, D");
            checkPath(node, "B", "A");
            checkPath(node, "A", "");
        }
    }

    public void checkPath(Node node, String name, String expectedPath) {
        if (StringUtils.equals(node.getName(), name)) {
            String path = node.getShortestPath().stream().map(Node::getName).collect(Collectors.joining(", "));
            Assert.assertTrue(StringUtils.equals(path, expectedPath));
        }
    }
}
