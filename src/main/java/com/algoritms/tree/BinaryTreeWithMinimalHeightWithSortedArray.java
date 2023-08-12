package com.algoritms.tree;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class BinaryTreeWithMinimalHeightWithSortedArray {
    public static void main(String[] args) {
        var tree = new Tree();
        var arr = new int[]{0, 1, 2, 3, 10, 12, 31, 34, 55, 66, 101, 123, 150, 220};

        fillTree(tree, arr);

        tree.print();

        System.out.println("The tree is balanced: " + isTreeIsBalanced(tree));
        System.out.println("The tree is binary search tree: " + isBinarySearchTree(tree));
    }

    /**
     * Все узлы слева должны быть меньше или равны текущему узлу
     * Текущий узел должен быть меньше или равен всем узлам справа
     */
    private static boolean isBinarySearchTree(Tree tree) {
        var root = tree.getRoot();
        return isBinarySearchTree(root, null, null);
    }

    private static boolean isBinarySearchTree(TreeNode node, Integer min, Integer max) {
        if (node == null) {
            return true;
        }

        if (min != null && node.getValue() <= min) {
            return false;
        }

        if (max != null && node.getValue() >= max) {
            return false;
        }

        if (!isBinarySearchTree(node.left, min, node.getValue())) {
            return false;
        }

        return isBinarySearchTree(node.right, node.getValue(), max);
    }

    private static boolean isTreeIsBalanced(Tree tree) {
        var root = tree.getRoot();
        return isTreeIsBalanced(root);
    }

    // разница между ветвями небольше 1
    private static boolean isTreeIsBalanced(TreeNode node) {
        if (node == null) {
            return true;
        }

        int diff = Math.abs(getHeight(node.left) - getHeight(node.right));
        if (diff > 1) {
            return false;
        } else {
            return isTreeIsBalanced(node.left) && isTreeIsBalanced(node.right);
        }
    }

    private static int getHeight(TreeNode node) {
        if (node == null) {
            return -1;
        }

        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    private static void fillTree(Tree tree, int[] arr) {
        var length = arr.length;
        fillTree(tree, arr, 0, length - 1);
    }

    /**
     * "Сбалансированное дерево" получиться при условии что мы будем добавлять элементы
     * с центральной части отсортированного массива слева постепенное уменьшая значение а справа увеличивая
     */
    private static TreeNode fillTree(Tree tree, int[] arr, int start, int end) {
        if (end < start) {
            return null;
        }

        int middle = (end + start) / 2;
        TreeNode node = new TreeNode(arr[middle]);

        if (tree.getRoot() == null) {
            tree.setRoot(node);
        }

        node.setLeft(fillTree(tree, arr, start, middle - 1));
        node.setRight(fillTree(tree, arr, middle + 1, end));

        return node;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class Tree {
        private TreeNode root;

        public void print() {
            System.out.println(toString());
        }

        @Override
        public String toString() {
            if (root == null) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            sb.append(root.getValue());

            String pointerRight = "└──";
            String pointerLeft = (root.getRight() != null) ? "├──" : "└──";

            traverseNodes(sb, "", pointerLeft, root.getLeft(), root.getRight() != null);
            traverseNodes(sb, "", pointerRight, root.getRight(), false);

            return sb.toString();
        }

        public void traverseNodes(StringBuilder sb, String padding, String pointer, TreeNode node,
                                  boolean hasRightSibling) {
            if (node != null) {
                sb.append("\n");
                sb.append(padding);
                sb.append(pointer);
                sb.append(node.getValue());

                var paddingBuilder = new StringBuilder(padding);
                if (hasRightSibling) {
                    paddingBuilder.append("│  ");
                } else {
                    paddingBuilder.append("   ");
                }

                var paddingForBoth = paddingBuilder.toString();
                var pointerRight = "└──";
                var pointerLeft = (node.getRight() != null) ? "├──" : "└──";

                traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeft(), node.getRight() != null);
                traverseNodes(sb, paddingForBoth, pointerRight, node.getRight(), false);
            }
        }
    }

    @RequiredArgsConstructor
    @Getter
    @Setter
    private static class TreeNode {
        private final int value;
        private TreeNode left;
        private TreeNode right;
    }
}
