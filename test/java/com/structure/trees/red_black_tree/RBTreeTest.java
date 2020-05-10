package com.structure.trees.red_black_tree;

import com.structure.trees.red_black_tree.enums.RBColor;
import org.junit.Test;

import static com.structure.trees.red_black_tree.enums.RBColor.BLACK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RBTreeTest {
    @Test
    public void leftRotateBasicVariantTest() {
        RBTree tree = new RBTree();

        RBNode root = getNode(50);

        tree.setRoot(root);

        RBNode node_25 = getNode(root, 25);

        root.setLeft(node_25);

        RBNode node_77 = getNode(root, 77);

        root.setRight(node_77);

        RBNode node_78 = getNode(node_77, 78);

        node_77.setRight(node_78);

        RBNode node_79 = getNode(node_78, 79);

        node_78.setRight(node_79);

        tree.leftRotate(node_78);

        assertEquals(node_79.getParent(), node_78);
        assertEquals(node_77.getParent(), node_78);
        assertEquals(node_78.getParent(), root);
        assertEquals(root.getRight(), node_78);
        assertEquals(node_78.getRight(), node_79);
        assertEquals(node_78.getLeft(), node_77);
        assertEquals(root.getLeft(), node_25);
        assertEquals(node_25.getParent(), root);
    }

    @Test
    public void leftRotateParentIsRoot() {
        RBTree tree = new RBTree();

        RBNode root = getNode(50);

        tree.setRoot(root);

        RBNode node_25 = getNode(root, 25);

        root.setLeft(node_25);

        RBNode node_75 = getNode(root, 75);

        RBNode rolling_node = getNode(node_75, 70);

        node_75.setLeft(rolling_node);

        root.setRight(node_75);

        tree.leftRotate(node_75);

        assertNull(node_75.getParent());
        assertEquals(root, node_75.getLeft());
        assertEquals(root.getParent(), node_75);
        assertEquals(root.getLeft(), node_25);
        assertEquals(node_25.getParent(), root);
        assertEquals(rolling_node.getParent(), root);
        assertEquals(root.getRight(), rolling_node);
    }

    @Test
    public void rightRotateBasicVariantTest() {
        RBTree tree = new RBTree();

        RBNode root = getNode(50);

        tree.setRoot(root);

        RBNode node_25 = getNode(root, 25);

        root.setLeft(node_25);

        RBNode node_75 = getNode(root, 77);

        root.setRight(node_75);

        RBNode node_12 = getNode(node_25, 12);

        node_25.setLeft(node_12);

        RBNode node_6 = getNode(node_12, 6);

        node_12.setLeft(node_6);

        tree.rightRotate(node_12);

        assertEquals(node_6.getParent(), node_12);
        assertEquals(node_12.getLeft(), node_6);
        assertEquals(node_25.getParent(), node_12);
        assertEquals(node_12.getRight(), node_25);
        assertEquals(node_12.getParent(), root);
        assertEquals(root.getLeft(), node_12);
        assertEquals(root.getRight(), node_75);
        assertEquals(node_75.getParent(), root);
    }

    @Test
    public void rightRotateParentIsRoot() {
        RBTree tree = new RBTree();

        RBNode root = getNode(50);

        tree.setRoot(root);

        RBNode node_25 = getNode(root, 25);

        root.setLeft(node_25);

        RBNode node_75 = getNode(root, 75);

        root.setRight(node_75);

        RBNode rolling_node = getNode(node_25, 30);

        node_25.setRight(rolling_node);

        tree.rightRotate(node_25);

        assertNull(node_25.getParent());
        assertEquals(root, node_25.getRight());
        assertEquals(root.getParent(), node_25);
        assertEquals(root.getRight(), node_75);
        assertEquals(node_75.getParent(), root);
        assertEquals(rolling_node.getParent(), root);
        assertEquals(root.getLeft(), rolling_node);
    }

    @Test
    public void doInsertTest() {
        RBTree tree = new RBTree();
        RBNode root = getNode(null, 50);
        tree.setRoot(root);

        RBNode node_25 = getNode(25);
        tree.doInsert(node_25);

        assertEquals(node_25.getParent(), root);
        assertEquals(root.getLeft(), node_25);

        RBNode node_30 = getNode(30);
        tree.doInsert(node_30);

        assertEquals(node_30.getParent(), node_25);
        assertEquals(node_25.getRight(), node_30);

        RBNode node_27 = getNode(27);
        tree.doInsert(node_27);

        assertEquals(node_27.getParent(), node_30);
        assertEquals(node_30.getLeft(), node_27);
    }

    @Test
    public void hasBlackNodesSizeDiffInBranchTest() {
        RBTree tree = new RBTree();
        RBNode root = getNode(null, 50, BLACK);
        tree.setRoot(root);

        //Todo несколько вариантов


    }

    private RBNode getNode(int key) {
        return getNode(null, key);
    }

    private RBNode getNode(RBNode parent, int key) {
        return getNode(parent, key, BLACK);
    }

    private RBNode getNode(RBNode parent, int key, RBColor color) {
        return RBNode.builder()
                .key(key)
                .color(color)
                .parent(parent)
                .build();
    }
}
