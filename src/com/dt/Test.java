package com.dt;

import java.util.*;

public class Test {
    public static void main(String[] args) {
/*
        Node tnode = new Node.NodeBuilder().setColumn(3).setValue("tHello").build();
        Node fnode = new Node.NodeBuilder().setColumn(4).setValue("fHello").build();
        Node node = new Node.NodeBuilder().setColumn(2).setValue("Hello").setTrueBranch(tnode).setFalseBranch(fnode).build();
        System.out.println(node.getColumn());
        System.out.println(node.getFalseBranch().getValue());
        System.out.println(node.getValue());
        System.out.println();
        node.setFalseBranch(null);
        System.out.println(node.getFalseBranch());
*/

        DecisionTree dt = new DecisionTree();
        List<List<String>> data = dt.createData();
        Node root = dt.buildTree(data);
        dt.printTree(root, "");
        System.out.println();
        System.out.println();
//        System.out.println(dt.classify(new ArrayList<>(Arrays.asList("(direct)", "USA", "yes", "5")), root));
//        System.out.println(dt.classify(new ArrayList<>(Arrays.asList("google", null, "yes", null)), root));
//        System.out.println(dt.classify(new ArrayList<>(Arrays.asList("google", "France", null, null)), root));

        dt.prune(root, 1.0);
        dt.printTree(root, "");


//        System.out.println(dt.entropy(data));

/*
        HashMap<String, Double> results = dt.uniqueCounts(data);
        for (Map.Entry m : results.entrySet()) {
            System.out.println(m.getKey() + " : " + m.getValue());
        }
*/
/*
        DividedSet dividedSet = dt.divideSet(data, 2, "yes");
        System.out.println(dt.entropy(dividedSet.getMatched()));
        System.out.println(dividedSet.getMatched().size());
        System.out.println(dividedSet.getUnmatched().size());

        for (List<String> row : dividedSet.getMatched()) {
            System.out.println(row);
        }
*/
    }
}
