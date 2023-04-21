package org.example.extramile.algorithms.tree;

import ch.qos.logback.classic.Logger;
import lombok.Getter;
import org.slf4j.LoggerFactory;

@Getter
public class BinaryTree {

    private Node root;
    private static final Logger log = (Logger) LoggerFactory.getLogger(BinaryTree.class);

    public Node add(int value) {
        root = addRecursively(root, value);
        return root;
    }

    private Node addRecursively(Node current, int value) {
        if (current == null) {
            return new Node(value);
        }

        if (value < current.value) {
            current.left = addRecursively(current.left, value);
        } else if (value > current.value) {
            current.right = addRecursively(current.right, value);
        }
        return current;
    }

    public void traverseInOrder(Node node) {
        if (node != null) {
            traverseInOrder(node.left);

            log.info("value is : {}", node.value);

            traverseInOrder(node.right);
        }
    }

}

