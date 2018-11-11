// jungle
// Jerred Shepherd

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class jungle {
    public static void main(String[] args) throws IOException {
        List<Input> input = getInput();
        List<Solution> solutions = solveInputs(input);
        printSolutions(solutions);
    }

    static void printSolutions(List<Solution> solutions) throws IOException {
        File outputFile = new File("jungle.out");
        PrintWriter printWriter = new PrintWriter(outputFile);
        for (Solution solution : solutions) {
            printSolution(solution, printWriter);
        }
        printWriter.close();
    }

    static void printSolution(Solution solution, Writer writer) throws IOException {
        System.out.println(solution);
        writer.write(solution + "\n");
    }

    static List<Solution> solveInputs(List<Input> inputs) {
        List<Solution> solutions = new ArrayList<>();
        inputs.forEach(input -> solutions.add(solveInput(input)));
        return solutions;
    }

    static Solution solveInput(Input input) {
        int minWeight = 0;
        DisjointSet disjointSet = new DisjointSet(input.numVertices);
        for (Edge edge : input.edges) {
            int sourceSet = disjointSet.find(edge.source);
            int destinationSet = disjointSet.find(edge.destination);
            if (sourceSet != destinationSet) {
                disjointSet.union(sourceSet, destinationSet);
                minWeight += edge.cost;
            }
        }
        return new Solution(minWeight);
    }

    static class Solution {
        int minWeight;

        Solution(int minWeight) {
            this.minWeight = minWeight;
        }

        @Override
        public String toString() {
            return String.valueOf(minWeight);
        }
    }

    static List<Input> getInput() throws FileNotFoundException {
        File inputFile = new File("jungle.in");

        Scanner scanner = new Scanner(inputFile);

        List<Input> input = new ArrayList<>();

        while (scanner.hasNextLine()) {
            int numberOfNodes = scanner.nextInt();
            if (numberOfNodes == 0) {
                break;
            }

            List<Edge> edges = new ArrayList<>();

            for (int i = 0; i < numberOfNodes - 1; i++) {
                char source = scanner.next().toCharArray()[0];
                int numberOfEdges = scanner.nextInt();

                for (int j = 0; j < numberOfEdges; j++) {
                    char dest = scanner.next().toCharArray()[0];
                    int cost = scanner.nextInt();
                    edges.add(new Edge(source, dest, cost));
                }
            }
            edges.sort(new EdgeComparator());
            input.add(new Input(numberOfNodes, edges));
        }

        return input;
    }

    static class Input {
        int numVertices;
        List<Edge> edges;

        Input(int numVertices, List<Edge> edges) {
            this.numVertices = numVertices;
            this.edges = edges;
        }
    }

    static class DisjointSet {
        int size;
        int[] set;

        DisjointSet(int size) {
            this.size = size;
            set = new int[size];
            for (int i = 0; i < size; i++) {
                set[i] = -1;
            }
        }

        int find(char c) {
            return find(c - 65);
        }

        int size(char c) {
            return size(c - 65);
        }

        void union(char l, char r) {
            union(l - 65, r - 65);
        }

        int find(int i) {
            int currNode = i;
            while (set[currNode] != -1) {
                currNode = set[currNode];
            }
            return currNode;
        }

        int size(int i) {
            int size = 0;
            int currNode = i;
            while (set[currNode] != -1) {
                size += 1;
                currNode = set[currNode];
            }
            return size;
        }

        void union(int l, int r) {
            int leftSize = size(l);
            int rightSize = size(r);

            int lParent = find(l);
            int rParent = find(r);

            if (leftSize < rightSize) {
                // union left -> right
                set[lParent] = rParent;
            } else {
                // union right -> left
                set[rParent] = lParent;
            }
        }
    }

    static class EdgeComparator implements Comparator<Edge> {
        @Override
        public int compare(Edge l, Edge r) {
            return l.compareTo(r);
        }
    }

    static class Edge implements Comparable<Edge> {
        char source;
        char destination;
        int cost;

        public Edge(char source, char destination, int cost) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "source=" + source +
                    ", destination=" + destination +
                    ", cost=" + cost +
                    '}';
        }

        @Override
        public int compareTo(Edge e) {
            return this.cost - e.cost;
        }
    }
}
