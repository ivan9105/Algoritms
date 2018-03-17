package com.structure.r_tree;

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

    private Node buildRoot(final boolean asLeaf) {
        float[] initCoords = new float[dimensionSize];
        float[] initDimensions = new float[dimensionSize];
        for (int i = 0; i < this.dimensionSize; i++) {
            initCoords[i] = (float) Math.sqrt(Float.MAX_VALUE);
            initDimensions[i] = DIM_FACTOR * (float) Math.sqrt(Float.MAX_VALUE);
        }
        return new Node(initCoords, initDimensions, asLeaf);
    }

    //Todo

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
        T entry;

        public Entry(float[] coords, float[] dimensions, boolean leaf, T entry) {
            super(coords, dimensions, leaf);
            this.entry = entry;
        }

        @Override
        public String toString() {
            return "Entry: " + entry;
        }
    }
}
