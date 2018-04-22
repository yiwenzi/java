package com.joker.interview.tree;

/**
 * Created by hunter on 2018/4/2.
 */
public class InOrder {
    public TreeNode next(TreeNode node) {
        if(node == null) {
            return null;
        }
        if(node.getRight() != null) {
            return first(node.getRight());
        } else {
            //循环里最好出现肯定的
            while (node.getParent() != null
                    && node.getParent().getRight() == node) {
                node = node.getParent();
            }
            //node.getParent() == null
            // || node is left child of its parent
            return node.getParent();
        }
    }

    private TreeNode first(TreeNode node) {
        if(node == null) {
            return null;
        }
        TreeNode curNode = node;
        while (curNode.getLeft() != null) {
            curNode = curNode.getLeft();
        }
        return curNode;
    }
}
