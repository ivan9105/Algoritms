package com.patterns.behavioral.iterator;


import java.util.*;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class IteratorUsage {
    public static void main(String[] args) {
        IteratorUsage executor = new IteratorUsage();
        executor.execute();
    }

    /**
     * A -> B -> F -> H
     * A -> C
     * A -> D -> G -> I
     * A -> E
     */
    private void execute() {
        Graph<String> graph = new Graph<>();
        graph.add("A", "B");
        graph.add("B", "F");
        graph.add("F", "H");
        graph.add("A", "C");
        graph.add("A", "D");
        graph.add("D", "G");
        graph.add("G", "I");
        graph.add("A", "E");

        GraphBreadthIterator<String> iterator = new GraphBreadthIterator<>(graph, "A");
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

/**
 * Реализация обхода в ширину (обход в глубину на основе стека приблизительно такой же)
 * паттерн итератор полезен если мы используем для обхода нетривиальных структур графов деревьев
 * чтобы инкапусулировать логику обхода
 */
class GraphBreadthIterator<T> implements Iterator<T> {
    Set<T> visited = new HashSet<>();
    Queue<T> queue = new LinkedList<>();
    Graph<T> graph;

    public GraphBreadthIterator(Graph<T> graph, T startVertex) {
        if (!graph.isExist(startVertex)) {
            throw new IllegalArgumentException("Vertex does not exits");
        }

        this.graph = graph;
        this.queue.add(startVertex);
        this.visited.add(startVertex);
    }

    @Override
    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        T next = queue.remove();
        //берем всех премыкающих к текущей вершине, которая является в начале очереди
        //посещаем их
        //не помеченных соседей не осталось
        //ставим текущим первого в очереди
        //ищем до тех пор пока есть не посещенные соседи
        //т.е. очередь пустая
        //все очень просто
        for (T adjacent : this.graph.getAdjacentSet(next)) {
            if (!this.visited.contains(adjacent)) {
                this.queue.add(adjacent);
                this.visited.add(adjacent);
            }
        }
        return next;
    }
}

//простая реализация графа
class Graph<T> {
    private Map<T, Set<T>> map = new HashMap<>();

    void add(T src, T destination) {
        if (src == null) {
            throw new IllegalArgumentException("Source can not be null");
        }

        if (src == destination || src.equals(destination)) {
            throw new IllegalArgumentException("Source and Destination can not be same");
        }

        Set<T> desitinations = map.get(src);
        if (desitinations == null) {
            desitinations = new HashSet<>();
        }
        if (destination != null) {
            desitinations.add(destination);
            map.computeIfAbsent(destination, k -> new HashSet<>());
        }
        map.put(src, desitinations);
    }

    Set<T> getAdjacentSet(T vertex) {
        Set<T> adjacentSet = this.map.get(vertex);
        if (adjacentSet == null || adjacentSet.isEmpty()) {
            return emptySet();
        }
        return unmodifiableSet(adjacentSet);
    }

    boolean isExist(T vertex) {
        return map.containsKey(vertex);
    }
}
