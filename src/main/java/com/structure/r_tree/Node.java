package com.structure.r_tree;

import java.util.LinkedList;
import java.util.List;

public class Node {
    private float[] coords;
    private float[] dimensions;
    private List<Node> children;
    private boolean leaf;
    private Node parent;

    private Node(){
    }

    Node(float[] coords, float[] dimensions, boolean leaf) {
        if (coords == null) {
            throw new IllegalArgumentException("Coords can't null");
        }

        if (dimensions == null) {
            throw new IllegalArgumentException("Dimensions can't null");
        }

        this.coords = new float[coords.length];
        this.dimensions = new float[dimensions.length];
        System.arraycopy(coords, 0, this.coords, 0, coords.length);
        System.arraycopy(dimensions, 0, this.dimensions, 0, dimensions.length);
        this.leaf = leaf;
        this.children = new LinkedList<>();
    }

    public float[] getCoords() {
        return coords;
    }

    public void setCoords(float[] coords) {
        this.coords = coords;
    }

    public float[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(float[] dimensions) {
        this.dimensions = dimensions;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
