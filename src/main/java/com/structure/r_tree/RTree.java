package com.structure.r_tree;

import java.util.*;

import static com.structure.r_tree.RTree.Type.LINEAR;

//Todo add tests
public class RTree<T> {
    private static final float DIM_FACTOR = -2.0f;
    private static final float FUDGE_FACTOR = 1.001f;

    private int maxEntries;
    private int minEntries;
    private int dimensionSize;
    private float[] dimensionArray;
    private Type type;
    private Node root;
    private int size;

    public RTree() {
        this(50, 2, 2, LINEAR);
    }

    public RTree(int maxEntries, int minEntries, int dimensionSize, Type type) {
        if (!(minEntries <= (maxEntries / 2))) {
            throw new IllegalStateException("!(minEntries <= (maxEntries / 2))");
        }

        this.maxEntries = maxEntries;
        this.minEntries = minEntries;
        this.type = type;
        this.dimensionArray = new float[dimensionSize];
        root = buildRoot(true);
    }

    private Node buildRoot(boolean asLeaf) {
        float[] initCoords = new float[dimensionSize];
        float[] initDimensions = new float[dimensionSize];
        for (int i = 0; i < this.dimensionSize; i++) {
            initCoords[i] = (float) Math.sqrt(Float.MAX_VALUE);
            initDimensions[i] = DIM_FACTOR * (float) Math.sqrt(Float.MAX_VALUE);
        }
        return new Node(initCoords, initDimensions, asLeaf);
    }

    public List<T> search(float[] coords, float[] dimensions) {
        checkInputParams(coords, dimensions);

        List<T> results = new LinkedList<>();
        doSearch(coords, dimensions, root, results);
        return results;
    }

    private void checkInputParams(float[] coords, float[] dimensions) {
        if (coords.length != dimensionSize) {
            throw new IllegalArgumentException("Coords length must be equal dimension size");
        }

        if (dimensions.length != dimensionSize) {
            throw new IllegalArgumentException("Dimensions length must be equal dimension size");
        }
    }

    @SuppressWarnings("unchecked")
    private void doSearch(float[] coords, float[] dimensions, Node currentNode, List<T> results) {
        if (currentNode.isLeaf()) {
            for (Node node : currentNode.getChildren()) {
                if (isOverlap(coords, dimensions, node.getCoords(), node.getDimensions())) {
                    results.add(((Entry<T>) node).getEntry());
                }
            }
        } else {
            for (Node node : currentNode.getChildren()) {
                if (isOverlap(coords, dimensions, node.getCoords(), node.getDimensions())) {
                    doSearch(coords, dimensions, node, results);
                }
            }
        }
    }

    //Todo log it
    private boolean isOverlap(float[] scoords, float[] sdimensions, float[] coords, float[] dimensions) {
        for (int i = 0; i < scoords.length; i++) {
            boolean overlapInThisDimension = false;
            if (scoords[i] == coords[i]) {
                overlapInThisDimension = true;
            } else if (scoords[i] < coords[i]) {
                if (scoords[i] + FUDGE_FACTOR * sdimensions[i] >= coords[i]) {
                    overlapInThisDimension = true;
                }
            } else if (scoords[i] > coords[i]) {
                if (coords[i] + FUDGE_FACTOR * dimensions[i] >= scoords[i]) {
                    overlapInThisDimension = true;
                }
            }

            if (!overlapInThisDimension) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean delete(float[] coords, float[] dimensions, T entry) {
        checkInputParams(coords, dimensions);

        Node findNode = findLeaf(root, coords, dimensions, entry);
        if (findNode == null) {
            throw new RuntimeException("Leaf not found for entry " + entry);
        }

        ListIterator<Node> it = findNode.getChildren().listIterator();
        T removed = null;

        while (it.hasNext()) {
            Entry<T> next = (Entry<T>) it.next();
            if (next.entry.equals(entry)) {
                removed = next.entry;
                it.remove();
                break;
            }
        }

        if (removed != null) {
            condenseTree(findNode);
//TOdo
        }
    }

    private void condenseTree(Node removedTree) {
        Node currentNode = removedTree;
        Set<Node> nodeSet = new HashSet<>();
        while (currentNode != root) { //TOdo
            if (currentNode.isLeaf() && (currentNode.getChildren().size() < minEntries)) {
                nodeSet.addAll(currentNode.getChildren());
                currentNode.getParent().getChildren().remove(currentNode);
            } else if (!currentNode.isLeaf() && (currentNode.getChildren().size() < minEntries)) {
                List<Node> toVisit = new LinkedList<>(currentNode.getChildren());
                while (!toVisit.isEmpty()) {
                    //Todo
                }
            } else {

            }
        }
    }

    private void tighten(Node... nodes) {
        for (Node node : nodes) {
            float[] minCoords = new float[dimensionSize];
            float[] maxCoords = new float[dimensionSize];

            for (int i = 0; i < dimensionSize; i++) {
                minCoords[i] = Float.MAX_VALUE;
                maxCoords[i] = Float.MIN_VALUE;

                for (Node child : node.getChildren()) {
                    //Todo strange logic refactoring
                    child.setParent(node);

                    if (child.getCoords()[i] < minCoords[i]) {
                        minCoords[i] = child.getCoords()[i];
                    }

                    if ((child.getCoords()[i] + child.getDimensions()[i]) > maxCoords[i]) {
                        maxCoords[i] = (child.getCoords()[i] + child.getDimensions()[i]);
                    }
                }
            }

            for (int i = 0; i < dimensionSize; i++) {
                maxCoords[i] -= minCoords[i];
            }

            System.arraycopy(minCoords, 0, node.getCoords(), 0, dimensionSize);
            System.arraycopy(maxCoords, 0, node.getDimensions(), 0, dimensionSize);
        }
    }

    private Node chooseLeaf(Node node, Entry<T> entry) {
        if (node.isLeaf()) {
            return node;
        }

        float minInc = Float.MAX_VALUE;
        Node next = null;
        for (Node child : node.getChildren()) {
            float inc = getRequiredExpansion(child.getCoords(), child.getDimensions(), entry);

            if (inc < minInc) {
                minInc = inc;
                next = child;
            } else if (inc == minInc) {
                float curArea = 1.0f;
                float thisArea = 1.0f;
                for (int i = 0; i < child.getDimensions().length; i++) {
                    assert next != null : "It's impossible";
                    curArea *= next.getDimensions()[i];
                    thisArea *= child.getDimensions()[i];
                }
                if (thisArea < curArea) {
                    next = child;
                }
            }
        }
        return chooseLeaf(next, entry);
    }

    private float getRequiredExpansion(float[] coords, float[] dimensions, Node currentNode) {
        float area = getArea(dimensions);
        float[] deltas = new float[dimensions.length];
        for (int i = 0; i < deltas.length; i++) {
            if (coords[i] + dimensions[i] < currentNode.getCoords()[i] + currentNode.getDimensions()[i]) {
                deltas[i] = currentNode.getCoords()[i] + currentNode.getDimensions()[i] - coords[i] - dimensions[i];
            } else if (coords[i] + dimensions[i] > currentNode.getCoords()[i] + currentNode.getDimensions()[i]) {
                deltas[i] = coords[i] - currentNode.getCoords()[i];
            }
        }
        float expanded = 1.0f;
        for (int i = 0; i < dimensions.length; i++) {
            area *= dimensions[i] + deltas[i];
        }
        return (expanded - area);
    }

    private float getArea(float[] dimensions) {
        float area = 1.0f;
        for (float dimension : dimensions) {
            area *= dimension;
        }
        return area;
    }

    private Node findLeaf(Node currentNode, float[] coords, float[] dimensions, T entry) {
        if (currentNode.isLeaf()) {
            for (Node node : currentNode.getChildren()) {
                if (((Entry) node).entry.equals(entry)) {
                    return node;
                }
            }
            return null;
        } else {
            for (Node node : currentNode.getChildren()) {
                if (isOverlap(node.getCoords(), node.getDimensions(), coords, dimensions)) {
                    Node result = findLeaf(node, coords, dimensions, entry);
                    if (result != null) {
                        return result;
                    }
                }
            }
            return null;
        }
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public int getMinEntries() {
        return minEntries;
    }

    public void setMinEntries(int minEntries) {
        this.minEntries = minEntries;
    }

    public int getDimensionSize() {
        return dimensionSize;
    }

    public void setDimensionSize(int dimensionSize) {
        this.dimensionSize = dimensionSize;
    }

    public float[] getDimensionArray() {
        return dimensionArray;
    }

    public void setDimensionArray(float[] dimensionArray) {
        this.dimensionArray = dimensionArray;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public enum Type {
        LINEAR, QUADRATIC
    }

    private static class Entry<T> extends Node {
        private T entry;

        public Entry(float[] coords, float[] dimensions, boolean leaf, T entry) {
            super(coords, dimensions, leaf);
            this.entry = entry;
        }

        public T getEntry() {
            return entry;
        }

        public void setEntry(T entry) {
            this.entry = entry;
        }

        @Override
        public String toString() {
            return "Entry: " + entry;
        }
    }
}
