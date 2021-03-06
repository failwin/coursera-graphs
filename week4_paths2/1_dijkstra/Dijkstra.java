import java.util.*;

public class Dijkstra {
    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_COMPLEX,
        BOOK_YES,
        BOOK_YES_2,
        BOOK_NO,
    }

    static class Vertices {
        public int index;

        private boolean visited = false;

        public int parent = -1;

        public int cost = Integer.MAX_VALUE;

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

    private static Vertices findMinimum(Map<Integer, Vertices> graph) {
        Vertices min = null;
        for (Vertices item : graph.values()) {
            if (item.visited) {
                continue;
            }
            if (min == null) {
                min = item;
            }
            if (item.cost < min.cost) {
                min = item;
            }
        }
        return min;
    }

    private static void explore(Map<Integer, Vertices> graph) {
        Vertices parent = findMinimum(graph);

        if (parent == null) {
            return;
        }

        parent.visited = true;

        int parentCost = parent.cost;

        if (parentCost != Integer.MAX_VALUE) {
            ArrayList<Vertices> child = parent.getNeighbors();

            for (Vertices item : child) {
                int childCost = item.getParentCost(parent.index);
                if (item.cost > parentCost + childCost) {
                    item.cost = parentCost + childCost;
                    item.parent = parent.index;
                }
            }
        }

        explore(graph);
    }

    private static int distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();

        fillGraph(graph, adj, cost);

        Vertices start = getOrCreate(graph, s);

        start.cost = 0;

        explore(graph);

        Vertices stop = getOrCreate(graph, t);

        return stop.cost == Integer.MAX_VALUE ? -1 : stop.cost;
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
//        int x = scanner.nextInt() - 1;
//        int y = scanner.nextInt() - 1;
//        System.out.println(distance(adj, cost, x, y));
    }

    public static void test() {
        expect(
            distance(prepare(TestCase.EMPTY, false), prepare(TestCase.EMPTY, true), 0, 1),
                -1,"Empty"
        );

        expect(
                distance(prepare(TestCase.NO_EDGES, false), prepare(TestCase.NO_EDGES, true), 0, 1),
                -1,"No edges"
        );

        expect(
                distance(prepare(TestCase.NO_EDGES, false), prepare(TestCase.NO_EDGES, true), 0, 0),
                0,"Same point"
        );

        expect(
                distance(prepare(TestCase.MY_COMPLEX, false), prepare(TestCase.MY_COMPLEX, true), 1, 6),
                3,"My complex"
        );

        expect(
                distance(prepare(TestCase.BOOK_YES, false), prepare(TestCase.BOOK_YES, true), 0, 2),
                3,"Book yes"
        );

        expect(
                distance(prepare(TestCase.BOOK_YES_2, false), prepare(TestCase.BOOK_YES_2, true), 0, 4),
                6,"Book yes 2"
        );

        expect(
                distance(prepare(TestCase.BOOK_NO, false), prepare(TestCase.BOOK_NO, true), 2, 1),
                -1,"Book no"
        );
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
            case MY_COMPLEX: {
                vertices = 10;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                cost = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                    cost[i] = new ArrayList<Integer>();
                }
                adj[0].add(2); cost[0].add(1);
                adj[1].add(0); cost[1].add(1); adj[1].add(3); cost[1].add(1);
                adj[2].add(6); cost[2].add(1);
                adj[4].add(1); cost[4].add(1); adj[4].add(7); cost[4].add(1);
                adj[5].add(2); cost[5].add(1);
                adj[7].add(5); cost[7].add(1); adj[7].add(8); cost[7].add(1); adj[7].add(9); cost[7].add(1);
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
                adj[0].add(1); cost[0].add(1); adj[0].add(2); cost[0].add(5);
                adj[1].add(2); cost[1].add(2);
                adj[3].add(0); cost[3].add(2);
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

    public static void expect(int actual, int expect, String messgae) {
        if (actual != expect) {
            throw new Error("Error: " + messgae + ", actual: " + actual);
        }
    }
}

