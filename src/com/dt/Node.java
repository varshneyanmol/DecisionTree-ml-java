package com.dt;

import java.util.HashMap;

public class Node {
    private int column;
    private String value;
    private HashMap<String, Double> results;
    private Node trueBranch;
    private Node falseBranch;

    private Node(NodeBuilder builder) {
        this.column = builder.column;
        this.value = builder.value;
        this.results = builder.results;
        this.trueBranch = builder.trueBranch;
        this.falseBranch = builder.falseBranch;
    }

    public int getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public HashMap<String, Double> getResults() {
        return results;
    }

    public Node getTrueBranch() {
        return trueBranch;
    }

    public Node getFalseBranch() {
        return falseBranch;
    }

    public static class NodeBuilder {
        private int column;
        private String value;
        private HashMap<String, Double> results;
        private Node trueBranch;
        private Node falseBranch;

        public NodeBuilder() {
            this.column = -1;
            this.value = null;
            this. results = null;
            this.trueBranch = null;
            this.falseBranch = null;
        }

        public NodeBuilder setColumn(int column) {
            this.column = column;
            return this;
        }

        public NodeBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        public NodeBuilder setResults(HashMap<String, Double> results) {
            this.results = results;
            return this;
        }

        public NodeBuilder setTrueBranch(Node trueBranch) {
            this.trueBranch = trueBranch;
            return this;
        }

        public NodeBuilder setFalseBranch(Node falseBranch) {
            this.falseBranch = falseBranch;
            return this;
        }

        public Node build() {
            return new Node(this);
        }

    }
}
