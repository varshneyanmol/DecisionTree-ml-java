package com.dt;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionTree {

    private List<List<String>> data;

    public DecisionTree() {
        data = new ArrayList<>();
    }

    public List<List<String>> createData() {
        data.add(Arrays.asList("slashdot", "USA", "yes", "18", "None"));
        data.add(Arrays.asList("google", "France", "yes", "23", "Premium"));
        data.add(Arrays.asList("digg", "USA", "yes", "24", "Basic"));
        data.add(Arrays.asList("kiwitobes", "France", "yes", "23", "Basic"));
        data.add(Arrays.asList("google", "UK", "no", "21", "Premium"));
        data.add(Arrays.asList("(direct)", "New Zealand", "no", "12", "None"));
        data.add(Arrays.asList("(direct)", "UK", "no", "21", "Basic"));
        data.add(Arrays.asList("google", "USA", "no", "24", "Premium"));
        data.add(Arrays.asList("slashdot", "France", "yes", "19", "None"));
        data.add(Arrays.asList("digg", "USA", "no", "18", "None"));
        data.add(Arrays.asList("google", "UK", "no", "18", "None"));
        data.add(Arrays.asList("kiwitobes", "UK", "no", "19", "None"));
        data.add(Arrays.asList("digg", "New Zealand", "yes", "12", "Basic"));
        data.add(Arrays.asList("slashdot", "UK", "no", "21", "None"));
        data.add(Arrays.asList("google", "UK", "yes", "18", "Basic"));
        data.add(Arrays.asList("kiwitobes", "France", "yes", "19", "Basic"));

        return data;
    }

    public List<List<String>> getData() {
        return data;
    }

    private DividedSet divideSet(List<List<String>> rows, int column, String value) {
        List<List<String>> matched = new ArrayList<>();
        List<List<String>> unmatched = new ArrayList<>();

        int type = getType(value);

        for (List<String> row : rows) {
            if (splitFunction(row.get(column), type, value)) {
                matched.add(row);
            } else {
                unmatched.add(row);
            }
        }

        DividedSet dividedSet = new DividedSet();
        dividedSet.setMatched(matched);
        dividedSet.setUnmatched(unmatched);
        return dividedSet;
    }

    private int getType(String value) {
        String regexInt = "[+-]?[0-9][0-9]*";
        String regexFloat = "[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?";
        Pattern pInt = Pattern.compile(regexInt);
        Matcher mInt = pInt.matcher(value);
        Pattern pFloat= Pattern.compile(regexFloat);
        Matcher mFloat = pFloat.matcher(value);

        int type = 0; // for String
        if(mInt.matches()) {
            type = 1; // for integer
        } else if (mFloat.matches()) {
            type = 2; // for float
        }

        return type;
    }

    private boolean splitFunction(String toCheck, int type, String value) {
        if (type == 1) {
            return Integer.parseInt(toCheck) >= Integer.parseInt(value);
        } else if (type == 2) {
            return Double.parseDouble(toCheck) >= Double.parseDouble(value);
        } else {
            return toCheck.equals(value);
        }
    }

    private HashMap<String, Double> uniqueCounts(List<List<String>> rows) {
        HashMap<String, Double> results = new HashMap<>();
        for (List<String> row : rows) {
            String r = row.get(row.size() - 1);
            if (!results.containsKey(r)) {
                results.put(r, 0.0);
            }
            results.put(r, results.get(r) + 1);
        }
        return results;
    }

    private double entropy(List<List<String>> rows) {
        double ent = 0.0;

        HashMap<String, Double> results = uniqueCounts(rows);
        for (Map.Entry entry : results.entrySet()) {
            double p = (double) entry.getValue() / rows.size();
            ent = ent - (p*log2(p));
        }
        return ent;
    }

    private double log2(double a) {
        return Math.log(a)/Math.log(2);
    }

    public Node buildTree(List<List<String>> rows) {
        Node.NodeBuilder nodeBuilder = new Node.NodeBuilder();
        if (rows.size() == 0) {
            return nodeBuilder.build();
        }
        double currentEntropy = entropy(rows);
        double bestGain = 0.0;
        int bestColumn = 0;
        String bestColumnValue = null;
//        String[] bestCriteria = new String[2];
        DividedSet bestDividedSet= new DividedSet();


        int columnCount = rows.get(0).size() - 1;
        for (int col = 0; col < columnCount; col++) { // runs for every column except the last one
            // generate a set of different values in this column
            Set<String> columnValues = new HashSet<>();
            for (List<String> row : rows) {
                columnValues.add(row.get(col));
            }

            // dividing the row up for each value in this column
            Iterator<String> itr = columnValues.iterator();
            while (itr.hasNext()) {
                String value = itr.next();
                DividedSet dividedSet = divideSet(rows, col, value);

                // Information gain
                double p = (double) dividedSet.getMatched().size() / rows.size();
                double gain = currentEntropy - p * entropy(dividedSet.getMatched()) - (1 - p) * entropy(dividedSet.getUnmatched());
                if (gain > bestGain && dividedSet.getMatched().size() > 0 && dividedSet.getUnmatched().size() > 0) {
                    bestGain = gain;
                    bestColumn = col;
                    bestColumnValue = value;
                    bestDividedSet = dividedSet;
                }
            }
        }

        // Create subbranches
        if (bestGain > 0.0) {
            Node trueBranch = buildTree(bestDividedSet.getMatched());
            Node falseBranch = buildTree(bestDividedSet.getUnmatched());
            return nodeBuilder.setColumn(bestColumn).setValue(bestColumnValue).setTrueBranch(trueBranch).setFalseBranch(falseBranch).build();
        } else {
            return nodeBuilder.setResults(uniqueCounts(rows)).build();
        }
    }

    public void printTree(Node node, String indent ){

        if (node.getResults() != null) {
            for (Map.Entry entry : node.getResults().entrySet()) {
                System.out.print("{" + entry.getKey() + " : " + entry.getValue() + "}");
                System.out.println();
            }
        } else {
            System.out.println(node.getColumn() + ":" + node.getValue() + "?");
            System.out.print(indent + "T->");
            printTree(node.getTrueBranch(), indent + "  ");
            System.out.print(indent + "F->");
            printTree(node.getFalseBranch(), indent + "  ");
        }
    }

    public HashMap<String, Double> classify(List<String> observation, Node tree) {
        if (tree.getResults() != null) {
            return tree.getResults();
        }

        String v = observation.get(tree.getColumn());
        if (v == null) {
            HashMap<String, Double> trueResult = classify(observation, tree.getTrueBranch());
            HashMap<String, Double> falseResult = classify(observation, tree.getFalseBranch());

            double tCount = 0.0;
            for (Map.Entry entry : trueResult.entrySet()) {
                tCount += (double) entry.getValue();
            }
            double fCount = 0.0;
            for (Map.Entry entry : falseResult.entrySet()) {
                fCount += (double) entry.getValue();
            }
            double tWeight = tCount / (tCount + fCount);
            double fWeight = fCount / (tCount + fCount);

            HashMap<String, Double> result = new HashMap<>();
            for (Map.Entry entry : trueResult.entrySet()) {
                result.put((String)entry.getKey(), (double)entry.getValue()*tWeight);
            }
            for (Map.Entry entry : falseResult.entrySet()) {
                result.put((String)entry.getKey(), (double)entry.getValue()*fWeight);
            }

            return result;

        } else {
            Node branch;
            int type = getType(v);
            if (splitFunction(v, type, tree.getValue())) {
                branch = tree.getTrueBranch();
            } else {
                branch = tree.getFalseBranch();
            }
            return classify(observation, branch);
        }

    }

    public void prune(Node tree, double minGain) {
        if (tree.getTrueBranch().getResults() == null) {
            prune(tree.getTrueBranch(), minGain);
        }
        if (tree.getFalseBranch().getResults() == null) {
            prune(tree.getFalseBranch(), minGain);
        }

        if (tree.getTrueBranch().getResults() != null && tree.getFalseBranch().getResults() != null) {

            Node trueBranch = tree.getTrueBranch();
            Node falseBranch = tree.getFalseBranch();

            List<List<String>> tb = new ArrayList<>();
            List<List<String>> fb = new ArrayList<>();
            List<List<String>> cb = new ArrayList<>();
            for (Map.Entry entry : trueBranch.getResults().entrySet()) {
                for (double i = 0; i < (double) entry.getValue(); i++) {
                    tb.add(Arrays.asList((String) entry.getKey()));
                    cb.add(Arrays.asList((String) entry.getKey()));
                }
            }
            for (Map.Entry entry : falseBranch.getResults().entrySet()) {
                for (double i = 0; i < (double) entry.getValue(); i++) {
                    fb.add(Arrays.asList((String) entry.getKey()));
                    cb.add(Arrays.asList((String) entry.getKey()));
                }
            }

            double delta = entropy(cb) - (entropy(tb) + entropy(fb));
            if (delta < minGain) {
                tree.setTrueBranch(null);
                tree.setFalseBranch(null);
                tree.setResults(uniqueCounts(cb));
            }

        }
    }

}
