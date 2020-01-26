import java.lang.reflect.Array;
import java.util.*;

public class ShortestPaths {

    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_SIMPLE,
        MY_COMPLEX,
        BOOK_YES,
        BOOK_YES_2,
        BOOK_NO,
    }

    static class Vertices {
        public int index;

        private boolean visited = false;

        public int parent = -1;

        public long cost = Long.MAX_VALUE;

        private Map<Integer, Vertices> neighbors = new HashMap<Integer, Vertices>();

        private Map<Integer, Integer> parentsCost = new HashMap<Integer, Integer>();

        public Vertices(int index) {
            this.index = index;
        }

        public void setParentCost(int parent, int cost) {
            if (this.parentsCost.containsKey(parent)) {
                int curCost = this.parentsCost.get(parent);
                if (curCost < cost) {
                    return;
                }
            }
            this.parentsCost.put(parent, cost);
        }

        public int getParentCost(int parent) {
            return this.parentsCost.get(parent);
        }

        public void addNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            if (!this.neighbors.containsKey(index)) {
                this.neighbors.put(index, neighbor);
            }
        }

        public ArrayList<Vertices> getNeighbors() {
            ArrayList<Vertices> items = new ArrayList<Vertices>();
            for (Vertices item : this.neighbors.values()) {
                if (item != null && !item.visited) {
                    items.add(item);
                }
            }
            return items;
        }

        public boolean hasNeighbors() {
            return this.neighbors.size() > 0;
        }
    }

    private static Vertices getOrCreate(Map<Integer, Vertices> graph, int index) {
        if (graph.containsKey(index)) {
            return graph.get(index);
        }
        Vertices newVertices = new Vertices(index);
        graph.put(index, newVertices);
        return newVertices;
    }

    private static void fillGraph(Map<Integer, Vertices> graph, ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        for (int index = 0; index < adj.length; index++) {
            Vertices baseVertices = getOrCreate(graph, index);

            ArrayList<Integer> neighbors = adj[index];
            ArrayList<Integer> costs = cost[index];

            if (neighbors.size() > 0) {
                for (int j = 0; j < neighbors.size(); j++) {
                    int next = neighbors.get(j);
                    Vertices childVertices = getOrCreate(graph, next);
                    childVertices.setParentCost(index, costs.get(j));
                    baseVertices.addNeighbor(childVertices);
                }
            }
        }
    }

    private static boolean explore(Map<Integer, Vertices> graph, int count) {
        boolean hasChanges = false;
        for (int i = 0; i < count; i++) {
            Vertices parent = getOrCreate(graph, i);
            long parentCost = parent.cost;

            if (parentCost != Long.MAX_VALUE) {
                ArrayList<Vertices> child = parent.getNeighbors();

                for (Vertices item : child) {
                    int childCost = item.getParentCost(parent.index);
                    if (item.cost > parentCost + childCost) {
                        item.cost = parentCost + childCost;
                        item.parent = parent.index;
                        hasChanges = true;
                    }
                }
            }
        }
        return hasChanges;
    }

    private static Map<Integer, Vertices> shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();

        fillGraph(graph, adj, cost);

        Vertices start = getOrCreate(graph, s);
        start.cost = 0;

        int count = adj.length;
        for (int i = 0; i < count - 1; i++) {
            explore(graph, count);
        }

        // explore(graph, count);

        return graph;
    }

    public static void main(String[] args) {
        try {
            test();
            System.out.println("Tests passed");
        } catch (Error err) {
            System.out.println(err.getMessage());
        }

//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        int m = scanner.nextInt();
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
//        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[n];
//        for (int i = 0; i < n; i++) {
//            adj[i] = new ArrayList<Integer>();
//            cost[i] = new ArrayList<Integer>();
//        }
//        for (int i = 0; i < m; i++) {
//            int x, y, w;
//            x = scanner.nextInt();
//            y = scanner.nextInt();
//            w = scanner.nextInt();
//            adj[x - 1].add(y - 1);
//            cost[x - 1].add(w);
//        }
//        int s = scanner.nextInt() - 1;
//
//        Map<Integer, Vertices> graph = shortestPaths(adj, cost, s);
//        int size = graph.size();
//
//        for (int i = 0; i < n; i++) {
//            Vertices item = getOrCreate(graph, i);
////            item.cost;
////            if (reachable[i] == 0) {
////                System.out.println('*');
////            } else if (shortest[i] == 0) {
////                System.out.println('-');
////            } else {
////                System.out.println(distance[i]);
////            }
//        }
    }

    public static String[] getList(Map<Integer, Vertices> graph) {
        String[] list = new String[graph.size()];
        for (Vertices item : graph.values()) {
            list[item.index] = "" + item.cost;
            if (item.cost == Long.MAX_VALUE) {
                list[item.index] = "*";
            }
            if (item.cost == -Long.MAX_VALUE) {
                list[item.index] = "-";
            }
        }
        return list;
    }

    public static void test() {
        Map<Integer, Vertices> graph;

        graph = shortestPaths(
            prepare(TestCase.MY_SIMPLE, false),
            prepare(TestCase.MY_SIMPLE, true),
            0
        );
        expect(getList(graph), new String[]{"0", "1", "3", "6"}, "My simple");

//        expect(
//                shortestPaths(prepare(TestCase.EMPTY, false), prepare(TestCase.EMPTY, true), 0),
//                0,"Empty"
//        );

//        expect(
//                negativeCycle(prepare(TestCase.NO_EDGES, false), prepare(TestCase.NO_EDGES, true)),
//                0,"No edges"
//        );
//
//        expect(
//                negativeCycle(prepare(TestCase.MY_COMPLEX, false), prepare(TestCase.MY_COMPLEX, true)),
//                3,"My complex"
//        );
//
//        expect(
//                negativeCycle(prepare(TestCase.BOOK_YES, false), prepare(TestCase.BOOK_YES, true)),
//                1,"Book yes"
//        );

//        expect(
//                negativeCycle(prepare(TestCase.BOOK_YES_2, false), prepare(TestCase.BOOK_YES_2, true)),
//                6,"Book yes 2"
//        );
//
//        expect(
//                negativeCycle(prepare(TestCase.BOOK_NO, false), prepare(TestCase.BOOK_NO, true)),
//                -1,"Book no"
//        );
    };

    public static ArrayList<Integer>[] prepare(TestCase testCase, boolean isCost) {
        int vertices;
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[1];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[])new ArrayList[1];

        switch (testCase) {
            case EMPTY: {
                vertices = 0;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                return isCost ? cost : adj;
            }
            case NO_EDGES: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                return isCost ? cost : adj;
            }
            case MY_SIMPLE: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); cost[0].add(1);
                adj[1].add(2); cost[1].add(2);
                adj[2].add(3); cost[2].add(3);
                return isCost ? cost : adj;
            }
            case MY_COMPLEX: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); cost[0].add(1);
                adj[1].add(2); cost[1].add(-5);
                adj[2].add(3); cost[2].add(1); adj[2].add(4); cost[2].add(2);
                adj[4].add(1); cost[4].add(1);
                return isCost ? cost : adj;
            }
            case BOOK_YES: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); cost[0].add(-5);
                adj[1].add(2); cost[1].add(2);
                adj[2].add(0); cost[2].add(1);
                //adj[3].add(0); cost[3].add(2);
                return isCost ? cost : adj;
            }
            case BOOK_YES_2: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); cost[0].add(4); adj[0].add(2); cost[0].add(2);
                adj[1].add(2); cost[1].add(2); adj[1].add(3); cost[1].add(2); adj[1].add(4); cost[1].add(3);
                adj[2].add(1); cost[2].add(1); adj[2].add(4); cost[2].add(4); adj[2].add(3); cost[2].add(4);
                adj[4].add(3); cost[4].add(1);
                return isCost ? cost : adj;
            }
            case BOOK_NO: {
                vertices = 3;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); cost[0].add(7); adj[0].add(2); cost[0].add(5);
                adj[1].add(2); cost[1].add(2);
                return isCost ? cost : adj;
            }
        }
        return adj;
    }

    public static void expect(String[] actual, String[] expect, String message) {
        String actualStr = "";
        String expectStr = "";
        for (int i = 0; i < actual.length; i++) {
            actualStr += actual[i] + "; ";
        }
        for (int i = 0; i < expect.length; i++) {
            expectStr += expect[i] + "; ";
        }
        if (!actualStr.equals(expectStr)) {
            throw new Error("Error: " + message + ", actual: " + actualStr + ", expected: " + expectStr);
        }
    }
}

