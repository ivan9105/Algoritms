package com.algoritms.graph;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class DependencyGraph {
    /**
     * Имеется список проектов и список зависимостей (список пар проектов, для
     * которых первый проект зависит от второго проекта). Проект может быть построен
     * только после построения всех его зависимостей. Найдите такой порядок
     * построения , который позволит построить все проекты. Если действительного
     * порядка не существует, верните признак ошибки.
     * При.мер :
     * В вод:
     * проекты : а, Ь, с, d, е, f
     * зависимости: (d, а), (Ь, f) , (d, Ь), (а, f), (с, d )
     * Вывод:
     * f, е , а, Ь, d , с
     */
    public static void main(String[] args) {
        var graph = new Graph();

        // given
        List<String> projects = List.of("a", "b", "c", "d", "e", "f", "g");
        projects.forEach(graph::addNode);

        graph.addEdge("d", "a");
        graph.addEdge("b", "f");
        graph.addEdge("d", "b");
        graph.addEdge("a", "f");
        graph.addEdge("c", "d");

        calculateOrderProjectsSolutionOne(graph);
        calculateOrderProjectsSolutionTwo(graph);
    }

    /**
     * Способ заключается в том что мы добавляем в начале "корни"
     * Далее "помечаем" их обработанными/"удаляем" их из списка обработки
     * Далее повторяем цикл до тех пока не обработаем весь список
     * Если найдена циклическая зависимость возвращаем ошибку
     */
    private static List<String> calculateOrderProjectsSolutionOne(Graph graph) {
        var result = new ArrayList<String>();

        //TODO не буду тратить время - текущая модель графа не сильно подойдет, нужно хранить зависимости и parent в нодах

        return result;
    }

    /**
     * Использование DFS (в глубину)
     *
     * берем случайную вершину - пробуем сделать поиск в глубину упираемся в посл элемент
     * этот элемент по факту можно добавить в конец результирующего списка и не учитывать больше в обходе
     * далее аналогичное действие -> последний элемент добавляется на позицию результирующего списка length - sizeOf(заполненных элементов)
     * и до тех пор пока не пройдем весь граф
     *
     * циклическая зависимость - можно реализовать через состояние - node + state
     * например перед обработкой помечать каким то признаком (в обработке)
     * и если мы при обработке элемента А напарываемся на элемент Б по факту в том же статусе - нужно кидать ошибку так как это цикл
     */
    private static List<String> calculateOrderProjectsSolutionTwo(Graph graph) {
        var result = new ArrayList<String>();

        //TODO

        return result;
    }


    @Data
    @NoArgsConstructor
    private static class Graph {
        private final Map<String, Node> nodes = new HashMap<>();
        private Node root = null;

        public void addEdge(String target, String adjacent) {
            addNode(target, List.of(adjacent));
        }

        public void addNode(String target) {
            findOrCreateNode(target);
        }

        public void addNode(String target, List<String> adjacent) {
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
