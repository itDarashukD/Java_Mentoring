package org.example.extramile.algorithms.tree;

import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.example.logAppender.MemoryAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;


@DisplayName("Tests for BinaryTree class")
@ExtendWith(MockitoExtension.class)
class BinaryTreeTest {

    @InjectMocks
    private BinaryTree binaryTree;
    private BinaryTree treeWithNodes;
    private MemoryAppender memoryAppender;

    @BeforeEach
    void setUp() {
        prepareNode();

        Logger logger = (Logger) LoggerFactory.getLogger(BinaryTree.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.INFO);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }

    private void prepareNode() {
          treeWithNodes = createBinaryTree();
    }

    private BinaryTree createBinaryTree() {
        BinaryTree tree = new BinaryTree();
         tree.add(1);
         tree.add(13);
         tree.add(53);
         tree.add(56);
         tree.add(0);
         tree.add(152);
         tree.add(45);
         tree.add(87);
         tree.add(99);
         tree.add(99);
         tree.add(-99);
         tree.add(-1);

        return tree;
    }

    @DisplayName("traverseInOrder() Is whole binary tree was traversed")
    @Test
    void traverseInOrder_traversingWholeBinaryTreeByOrder_eachElementWasTraversed() {
        binaryTree.traverseInOrder(treeWithNodes.getRoot());
        assertTrue(memoryAppender.contains("value is : -99", Level.INFO));
        assertTrue(memoryAppender.contains("value is : 0", Level.INFO));
        assertTrue(memoryAppender.contains("value is : 45", Level.INFO));
        assertTrue(memoryAppender.contains("value is : 56", Level.INFO));
        assertTrue(memoryAppender.contains("value is : 99", Level.INFO));
        assertTrue(memoryAppender.contains("value is : 152", Level.INFO));

    }
}