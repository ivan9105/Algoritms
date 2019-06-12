package com.structure.trees.red_black_tree;

import com.google.common.annotations.VisibleForTesting;
import com.structure.trees.Tree;
import lombok.Getter;
import lombok.Setter;

import static com.structure.trees.red_black_tree.enums.RBColor.BLACK;
import static com.structure.trees.red_black_tree.enums.RBColor.RED;

/**
 * Утверждения:
 * <p>
 * 1) Каждый узел промаркирован красным или чёрным цветом
 * 2) Корень и конечные узлы (листья) дерева — чёрные
 * 3) У красного узла родительский узел — чёрный
 * 4) Все простые пути из любого узла x до листьев содержат одинаковое количество чёрных узлов
 * 5) Чёрный узел может иметь чёрного родителя
 */
@Setter
@Getter
public class RBTree implements Tree {
    private RBNode root;

    /*
    Todo тест на основе визуализации просто идем и чекаем состояние дерева прямо по цифрам нодам с parent-ами
    если что то пошло не так то ковыряем код
     */
    public void insert(Integer key) {
        //Root должен быть черным
        if (root == null) {
            root = new RBNode(key, BLACK, null, null);
            return;
        }

        //При новой вставке мы всегда вставляем красный узел если root уже проинициирован
        RBNode newNode = new RBNode(key, RED, null, null);
        doInsert(newNode);

        //валидация и поворот при необходимости
        doValidateAndTurnIfNeed(newNode);
    }

    //Todo отдельный тест на каждый вариант
    @VisibleForTesting
    protected void doValidateAndTurnIfNeed(RBNode newNode) {
        RBNode parent = newNode.getParent();
        //Вариант 1 50, 25 или 50, 75
        if (!parent.isRed()) {
            return;
        }

        //Вариант 2 простое переключение цветов 50, 25, 75, 12
        parent.setColor(BLACK);
        RBNode uncle = parent.getBrother();
        if (uncle != null) {
            //Todo проверяем размер черных верщин хотя это наверное не обязательно
            //Todo на каждый кейс по тесту на существующие
        } else {
            //Todo взависимости от положения правый или левый поворот со сменой цветов
        }

        //Вариант 3 50, 12, 6


        //Вариант 4 простое переключение цветов 50, 25, 75, 12, 6

    }

    /**
     * Подсчитаем кол-во черных вершин если оно не сходиться то меняем цвет дяде на черный тоже
     * считаем до дяди включая дядю тоже
     * @param node
     * @param anotherNode
     * @return true/false
     */
    @VisibleForTesting
    protected boolean hasBlackNodesSizeDiffInBranch(RBNode node, RBNode anotherNode) {
        int size = doCalculateBlackNodeSize(node);
        int anotherBranchSize = doCalculateBlackNodeSize(anotherNode);
        return size == anotherBranchSize;
    }

    private int doCalculateBlackNodeSize(RBNode node) {
        int parentBlackSize = 0;
        RBNode currentNode = node;
        while (currentNode != null) {
            if (!currentNode.isRed()) {
                parentBlackSize++;
            }
            currentNode = currentNode.getParent();
        }
        return parentBlackSize;
    }

    @VisibleForTesting
    protected void leftRotate(RBNode node) {
        RBNode parent = node.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Root couldn't be turned");
        }

        RBNode previousLeft = node.getLeft();
        RBNode grandfather = parent.getParent();
        parent.setParent(node);
        node.setParent(grandfather);
        node.setLeft(parent);

        if (grandfather == null) {
            parent.setRight(previousLeft);
            previousLeft.setParent(parent);
            return;
        }

        boolean isRightParent = grandfather.isRightChild(parent);
        if (isRightParent) {
            grandfather.setRight(node);
        } else {
            grandfather.setLeft(node);
        }
    }

    protected void rightRotate(RBNode node) {
        RBNode parent = node.getParent();
        if (parent == null) {
            throw new IllegalArgumentException("Root couldn't be turned");
        }

        RBNode previousRight = node.getRight();
        RBNode grandfather = parent.getParent();
        parent.setParent(node);
        node.setRight(parent);
        node.setParent(grandfather);

        if (grandfather == null) {
            parent.setLeft(previousRight);
            previousRight.setParent(parent);
            return;
        }

        boolean isRightParent = grandfather.isRightChild(parent);
        if (isRightParent) {
            grandfather.setRight(node);
        } else {
            grandfather.setLeft(node);
        }
    }

    @VisibleForTesting
    protected void doInsert(RBNode newNode) {
        RBNode currentNode = root;
        RBNode targetParentNode;

        while (currentNode != null) {
            targetParentNode = currentNode;

            if (currentNode.getKey() < newNode.getKey()) {
                currentNode = currentNode.getRight();
            } else {
                currentNode = currentNode.getLeft();
            }

            newNode.setParent(targetParentNode);

            if (targetParentNode.getKey() < newNode.getKey()) {
                targetParentNode.setRight(newNode);
            } else {
                targetParentNode.setLeft(newNode);
            }
        }
    }

    //Todo тесты на основе виазулизации свою версию накидать https://www.cs.usfca.edu/~galles/visualization/flash.html
    //Todo разобраться каким образом мы считаем кол-во черных вершин в ветви и определяем о необходимости поворота и смене цветов
    //Todo отдельно функция подсчета длины ветвей с черными вершинами на основе вставленного значения
    //Todo отдельно проверять является ли родитель и потомок красными
}
