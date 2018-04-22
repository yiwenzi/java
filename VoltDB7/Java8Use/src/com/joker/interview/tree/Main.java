package com.joker.interview.tree;

/**
 * Created by hunter on 2018/3/31.
 */
public class Main {
    //根据前序遍历和中序遍历构造二叉树
    public TreeNode createTree(String preOrder,String inOrder) {
        //边界条件
        if(preOrder.isEmpty()) {
            return null;
        }
        char rootValue = preOrder.charAt(0);
        int rootIndex = inOrder.indexOf(rootValue);

        TreeNode root = new TreeNode(rootValue);
        root.setLeft(
                createTree(
                        preOrder.substring(1,1 + rootIndex),
                        inOrder.substring(0, rootIndex))
        );

        root.setRight(
                createTree(
                        preOrder.substring(1 + rootIndex),
                        inOrder.substring(1 + rootIndex)
                )
        );
        return root;
    }

    //根据根据前序遍历和中序遍历直接输出二叉树的后序遍历
    public String postOrder(String preOrder,String inOrder) {
        //边界条件
        if(preOrder.isEmpty()) {
            return "";
        }
        char rootValue = preOrder.charAt(0);
        int rootIndex = inOrder.indexOf(rootValue);

       return
               postOrder(
                    preOrder.substring(1, 1 + rootIndex),
                    inOrder.substring(0, rootIndex)) +
               postOrder(
                    preOrder.substring(1 + rootIndex),
                    inOrder.substring(1 + rootIndex)) +
               rootValue;
    }
}
