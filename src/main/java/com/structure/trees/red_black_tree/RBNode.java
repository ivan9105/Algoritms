package com.structure.trees.red_black_tree;

import com.structure.trees.red_black_tree.enums.RBColor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.structure.trees.red_black_tree.enums.RBColor.RED;

@Builder
@Getter
@Setter
public class RBNode {
    private Integer key;
    private RBColor color;
    private RBNode parent;
    private RBNode left;
    private RBNode right;

    public RBNode(Integer key, RBColor color, RBNode left, RBNode right) {
        this.key = key;
        this.color = color;
        this.left = left;
        this.right = right;
    }

    public RBNode(Integer key, RBColor color, RBNode parent, RBNode left, RBNode right) {
        this.key = key;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    public boolean isRed() {
        return color == RED;
    }

    public boolean isRightChild(RBNode node) {
        return right == node;
    }

    public RBNode getBrother() {
        if (parent == null) {
            return null;
        }

        boolean isRightChild = parent.isRightChild(this);
        return isRightChild ? parent.getLeft() : parent.getRight();
    }
}
