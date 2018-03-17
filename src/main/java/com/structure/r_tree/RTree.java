package com.structure.r_tree;

import java.util.*;

import static com.structure.r_tree.RTree.Type.LINEAR;

//Todo add tests, all operations
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
            size--;
        }
        if (size == 0) {
            root = buildRoot(true);
        }
        return (removed != null);
    }

    public boolean delete(final float[] coords, final T entry) {
        return delete(coords, dimensionArray, entry);
    }

    private void condenseTree(Node removedTree) {
        Node currentNode = removedTree;
        Set<Node> nodeSet = new HashSet<>();
        while (currentNode != root) {
            if (currentNode.isLeaf() && (currentNode.getChildren().size() < minEntries)) {
                nodeSet.addAll(currentNode.getChildren());
                currentNode.getParent().getChildren().remove(currentNode);
            } else if (!currentNode.isLeaf() && (currentNode.getChildren().size() < minEntries)) {
                Deque<Node> toVisit = new LinkedList<>(currentNode.getChildren());
                while (!toVisit.isEmpty()) {
                    Node next = toVisit.pop();
                    if (next.isLeaf()) {
                        nodeSet.addAll(next.getChildren());
                    } else {
                        toVisit.addAll(next.getChildren());
                    }
                }
                currentNode.getParent().getChildren().remove(currentNode);
            } else {
                tighten(currentNode);
            }
            currentNode = currentNode.getParent();
        }

        if (root.getChildren().size() == 0) {
            root = buildRoot(true);
        } else if ((root.getChildren().size() == 1) && (!root.isLeaf())) {
            root = root.getChildren().get(0);
            root.setParent(null);
        } else {
            tighten(root);
        }

        for (Node ne : nodeSet) {
            @SuppressWarnings("unchecked")
            Entry<T> e = (Entry<T>) ne;
            insert(e.getCoords(), e.getDimensions(), e.entry);
        }
        size -= nodeSet.size();
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

    public void clear() {
        root = buildRoot(true);
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

    private void adjustTree(Node node, Node otherNode) {
        if (node == root) {
            if (otherNode != null) {
                root = buildRoot(false);
                root.getChildren().add(node);
                node.setParent(root);
                root.getChildren().add(otherNode);
                otherNode.setParent(root);
            }
            tighten(root);
            return;
        }
        tighten(node);

        if (otherNode != null) {
            tighten(otherNode);
            if (node.getParent().getChildren().size() > maxEntries) {
                Node[] splits = splitNode(node.getParent());
                adjustTree(splits[0], splits[1]);
            }
        }

        if (node.getParent() != null) {
            adjustTree(node.getParent(), null);
        }
    }

    @SuppressWarnings("unchecked")
    private Node[] splitNode(Node node) {
        Node[] nodeArray = new Node[]{node, new Node(node.getCoords(), node.getDimensions(), node.isLeaf())};
        nodeArray[1].setParent(node.getParent());
        if (nodeArray[1].getParent() != null) {
            nodeArray[1].getParent().getChildren().add(nodeArray[1]);
        }
        LinkedList<Node> nodeList = new LinkedList<>(node.getChildren());
        node.getChildren().clear();
        Node[] processedArray = type == LINEAR ? lPickSeeds(nodeList) : qPickSeeds(nodeList);
        nodeArray[0].getChildren().add(processedArray[0]);
        nodeArray[1].getChildren().add(processedArray[1]);
        tighten(nodeArray);
        while (!nodeList.isEmpty()) {
            if ((nodeArray[0].getChildren().size() >= minEntries)
                    && (nodeArray[1].getChildren().size() + nodeList.size() == minEntries)) {
                nodeArray[1].getChildren().addAll(nodeList);
                nodeList.clear();
                tighten(nodeArray); // Not sure this is required.
                return nodeArray;
            } else if ((nodeArray[1].getChildren().size() >= minEntries)
                    && (nodeArray[0].getChildren().size() + nodeList.size() == minEntries)) {
                nodeArray[0].getChildren().addAll(nodeList);
                nodeList.clear();
                tighten(nodeArray); // Not sure this is required.
                return nodeArray;
            }
            Node c = type == LINEAR ? lPickNext(nodeList) : qPickNext(nodeList, nodeArray);
            Node preferred;
            float e0 = getRequiredExpansion(nodeArray[0].getCoords(), nodeArray[0].getDimensions(), c);
            float e1 = getRequiredExpansion(nodeArray[1].getCoords(), nodeArray[1].getDimensions(), c);
            if (e0 < e1) {
                preferred = nodeArray[0];
            } else if (e0 > e1) {
                preferred = nodeArray[1];
            } else {
                float a0 = getArea(nodeArray[0].getDimensions());
                float a1 = getArea(nodeArray[1].getDimensions());
                if (a0 < a1) {
                    preferred = nodeArray[0];
                } else if (e0 > a1) {
                    preferred = nodeArray[1];
                } else {
                    if (nodeArray[0].getChildren().size() < nodeArray[1].getChildren().size()) {
                        preferred = nodeArray[0];
                    } else if (nodeArray[0].getChildren().size() > nodeArray[1].getChildren().size()) {
                        preferred = nodeArray[1];
                    } else {
                        preferred = nodeArray[(int) Math.round(Math.random())];
                    }
                }
            }
            preferred.getChildren().add(c);
            tighten(preferred);
        }
        return nodeArray;
    }

    @SuppressWarnings("unchecked")
    public void insert(final float[] coords, final float[] dimensions, final T entry) {
        checkInputParams(coords, dimensions);
        Entry newEntry = new Entry(coords, dimensions, true, entry);
        Node node = chooseLeaf(root, newEntry);
        node.getChildren().add(newEntry);
        size++;
        newEntry.setParent(node);
        if (node.getChildren().size() > maxEntries) {
            Node[] splits = splitNode(node);
            adjustTree(splits[0], splits[1]);
        } else {
            adjustTree(node, null);
        }
    }

    public void insert(final float[] coords, final T entry) {
        insert(coords, dimensionArray, entry);
    }

    @SuppressWarnings("unchecked")
    private Node[] lPickSeeds(List<Node> nodeList) {
        Node[] bestPair = new Node[2];
        boolean foundBestPair = false;
        float bestSep = 0.0f;
        for (int i = 0; i < dimensionSize; i++) {
            float dimLb = Float.MAX_VALUE, dimMinUb = Float.MAX_VALUE;
            float dimUb = -1.0f * Float.MAX_VALUE, dimMaxLb = -1.0f * Float.MAX_VALUE;
            Node nMaxLb = null, nMinUb = null;
            for (Node node : nodeList) {
                if (node.getCoords()[i] < dimLb) {
                    dimLb = node.getCoords()[i];
                }
                if (node.getDimensions()[i] + node.getCoords()[i] > dimUb) {
                    dimUb = node.getDimensions()[i] + node.getCoords()[i];
                }
                if (node.getCoords()[i] > dimMaxLb) {
                    dimMaxLb = node.getCoords()[i];
                    nMaxLb = node;
                }
                if (node.getDimensions()[i] + node.getCoords()[i] < dimMinUb) {
                    dimMinUb = node.getDimensions()[i] + node.getCoords()[i];
                    nMinUb = node;
                }
            }
            float sep = (nMaxLb == nMinUb) ? -1.0f
                    : Math.abs((dimMinUb - dimMaxLb) / (dimUb - dimLb));
            if (sep >= bestSep) {
                bestPair[0] = nMaxLb;
                bestPair[1] = nMinUb;
                bestSep = sep;
                foundBestPair = true;
            }
        }
        if (!foundBestPair) {
            bestPair = new Node[]{nodeList.get(0), nodeList.get(1)};
        }
        nodeList.remove(bestPair[0]);
        nodeList.remove(bestPair[1]);
        return bestPair;
    }

    private Node lPickNext(LinkedList<Node> nodeList) {
        return nodeList.pop();
    }

    @SuppressWarnings("unchecked")
    private Node[] qPickSeeds(LinkedList<Node> nodeList) {
        Node[] bestPair = new Node[2];
        float maxWaste = -1.0f * Float.MAX_VALUE;
        for (Node outerNode : nodeList) {
            for (Node innerNode : nodeList) {
                if (outerNode == innerNode) {
                    continue;
                }
                float n1a = getArea(outerNode.getDimensions());
                float n2a = getArea(innerNode.getDimensions());
                float ja = 1.0f;
                for (int i = 0; i < dimensionSize; i++) {
                    float jc0 = Math.min(outerNode.getCoords()[i], innerNode.getCoords()[i]);
                    float jc1 = Math.max(outerNode.getCoords()[i] + outerNode.getDimensions()[i], innerNode.getCoords()[i] + innerNode.getDimensions()[i]);
                    ja *= (jc1 - jc0);
                }
                float waste = ja - n1a - n2a;
                if (waste > maxWaste) {
                    maxWaste = waste;
                    bestPair[0] = outerNode;
                    bestPair[1] = innerNode;
                }
            }
        }
        nodeList.remove(bestPair[0]);
        nodeList.remove(bestPair[1]);
        return bestPair;
    }

    private Node qPickNext(LinkedList<Node> nodeList, Node[] nodeArr) {
        float maxDiff = -1.0f * Float.MAX_VALUE;
        Node nextC = null;
        for (Node c : nodeList) {
            float n0Exp = getRequiredExpansion(nodeArr[0].getCoords(), nodeArr[0].getDimensions(), c);
            float n1Exp = getRequiredExpansion(nodeArr[1].getCoords(), nodeArr[1].getDimensions(), c);
            float diff = Math.abs(n1Exp - n0Exp);
            if (diff > maxDiff) {
                maxDiff = diff;
                nextC = c;
            }
        }
        assert (nextC != null) : "No node selected from qPickNext";
        nodeList.remove(nextC);
        return nextC;
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
