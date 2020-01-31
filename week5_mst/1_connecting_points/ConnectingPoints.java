import java.util.*;
import java.lang.Math;

public class ConnectingPoints {

    public enum TestCase {
        EMPTY,
        MY_D,
        MY_3,
        MY_4,
        MY_MIN,
        MY_MAX,
        MY_NEG,
        BOOK_YES,
        BOOK_YES_1,
        BOOK_YES_2,
        BOOK_NO,
    }

    static class Vertices {
        public int index;

        private boolean visited = false;

        public int parent = -1;

        public int cost = Integer.MAX_VALUE;

        private Map<Integer, Vertices> neighbors = new HashMap<Integer, Vertices>();

        private Map<Integer, Double> parentsCost = new HashMap<Integer, Double>();

        public Vertices(int index) {
            this.index = index;
        }

        public void setParentCost(int parent, Double cost) {
            if (this.parentsCost.containsKey(parent)) {
                Double curCost = this.parentsCost.get(parent);
                if (curCost < cost) {
                    return;
                }
            }
            this.parentsCost.put(parent, cost);
        }

        public Double getParentCost(int parent) {
            return this.parentsCost.get(parent);
        }

        public void addNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            if (!this.neighbors.containsKey(index)) {
                this.neighbors.put(index, neighbor);
            }
        }

        public boolean hasNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            return this.neighbors.containsKey(index);
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

    static class Edge {
        public double cost = 0;

        public Vertices v1 = null;

        public Vertices v2 = null;

        public Edge(double cost, Vertices v1, Vertices v2) {
            this.cost = cost;
            this.v1 = v1;
            this.v2 = v2;
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

    private static String key(int x1, int y1, int x2, int y2) {
        return "" + x1 + "," + y1 + ";" + x2 + "," + y2;
    }

//    char hasCross(int p0_x, int p0_y, int p1_x, int p1_y,
//                  int p2_x, int p2_y, int p3_x, int p3_y)
//    {
//        float s1_x, s1_y, s2_x, s2_y;
//        s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
//        s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;
//
//        float s, t;
//        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
//        t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);
//
//        if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
//        {
//            // Collision detected
//            if (i_x != NULL)
//            *i_x = p0_x + (t * s1_x);
//            if (i_y != NULL)
//            *i_y = p0_y + (t * s1_y);
//            return 1;
//        }
//
//        return 0; // No collision
//    }

    private static void fillGraph(Map<Integer, Vertices> graph, ArrayList<Edge> edges, int[] xList, int[] yList) {
        int count = xList.length;
        Map<String, Integer> state = new HashMap<String, Integer>();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (i != j) {
                    int x1 = xList[i];
                    int y1 = yList[i];
                    int x2 = xList[j];
                    int y2 = yList[j];

                    if (!state.containsKey(key(x1, y1, x2, y2)) && !state.containsKey(key(x2, y2, x1, y1))) {
                        state.put(key(x1, y1, x2, y2), 1);
                        state.put(key(x2, y2, x1, y1), 1);

                        Vertices v1 = getOrCreate(graph, i);
                        Vertices v2 = getOrCreate(graph, j);

                        double cost = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
//                        v1.addNeighbor(v2);
//                        v2.addNeighbor(v1);
//                        v1.setParentCost(v2.index, cost);
//                        v2.setParentCost(v1.index, cost);
                        Edge edge = new Edge(cost, v1, v2);

                        edges.add(edge);
                    }
                }
            }
        }
    }

    static class EdgesComparator implements Comparator<Edge>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Edge a, Edge b) {
            if (a == b) {
                return 0;
            }
            return (a.cost - b.cost > 0) ? 1 : -1;
        }
    }

    static EdgesComparator edgesComparator = new EdgesComparator();

    private static int getGroup(Map<Integer, Integer> groups, int vIndex) {
        int group = groups.get(vIndex);
        if (group < 0) {
            return vIndex;
        }
        return getGroup(groups, group);
    }

    private static void mergeGroups(Map<Integer, Integer> groups, int group1, int group2) {
        if (group1 <= group2) {
            groups.put(group2, group1);
        } else {
            groups.put(group1, group2);
        }
    }

    private static void explore(Queue<Edge> edges, Map<Integer, Integer> groups) {
        Edge edge = edges.poll();
        if (edge == null) {
            return;
        }
        Vertices v1 = edge.v1;
        Vertices v2 = edge.v2;
        int group1 = getGroup(groups, v1.index);
        int group2 = getGroup(groups, v2.index);
        if (group1 != group2) {
            v1.addNeighbor(v2);
            v2.addNeighbor(v1);
            v1.setParentCost(v2.index, edge.cost);
            v2.setParentCost(v1.index, edge.cost);
            mergeGroups(groups, group1, group2);
        }
        explore(edges, groups);
    }

    private static double sumPath(Map<Integer, Vertices> graph, int start) {
        Vertices parent = getOrCreate(graph, start);
        parent.visited = true;
        double cost = 0;

        ArrayList<Vertices> childs = parent.getNeighbors();

        if (childs.size() == 0) {
            return cost;
        }

        for (Vertices item: childs) {
            cost += item.getParentCost(parent.index);

            cost += sumPath(graph, item.index);
        }
        return cost;
    }

    private static double minimumDistance(int[] x, int[] y) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();
        int count = x.length;

        ArrayList<Edge> edges = new ArrayList<>();

        fillGraph(graph, edges, x, y);

        Collections.sort(edges, edgesComparator);

        Queue<Edge> sortedEdges = new LinkedList<>(edges);

        Map<Integer, Integer> groups = new HashMap<Integer, Integer>();
        for (int i = 0; i < count; i++) {
            groups.put(i, -1);
        }

        explore(sortedEdges, groups);

        double result = sumPath(graph, 0);

        return result;
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
//        int[] x = new int[n];
//        int[] y = new int[n];
//        for (int i = 0; i < n; i++) {
//            x[i] = scanner.nextInt();
//            y[i] = scanner.nextInt();
//        }
//        System.out.println(minimumDistance(x, y));
    }

    public static void test() {
        expect(
                minimumDistance(prepare(TestCase.EMPTY, true), prepare(TestCase.EMPTY, false)),
                0,"Empty"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_MIN, true), prepare(TestCase.MY_MIN, false)),
                0,"My Min"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_MAX, true), prepare(TestCase.MY_MAX, false)),
                100000,"My Max"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_NEG, true), prepare(TestCase.MY_NEG, false)),
                1000,"My Neg"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_D, true), prepare(TestCase.MY_D, false)),
                2,"My D"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_3, true), prepare(TestCase.MY_3, false)),
                2,"My 3"
        );

        expect(
                minimumDistance(prepare(TestCase.MY_4, true), prepare(TestCase.MY_4, false)),
                3,"My 4"
        );

        double expected_b = 2 * Math.sqrt(2) + Math.sqrt(5) + 2;
        expect(
                minimumDistance(prepare(TestCase.BOOK_YES, true), prepare(TestCase.BOOK_YES, false)),
                expected_b,"Book yes"
        );
    };

    public static int[] prepare(TestCase testCase, boolean isX) {
        switch (testCase) {
            case EMPTY: {
                int vertices = 0;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                return isX ? x : y;
            }
            case MY_MIN: {
                int vertices = 5;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                x[0] = 0; y[0] = 0;
                x[1] = 0; y[1] = 0;
                x[2] = 0; y[2] = 0;
                x[3] = 0; y[3] = 0;
                x[4] = 0; y[4] = 0;
                return isX ? x : y;
            }
            case MY_MAX: {
                int vertices = 200;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                for (int i = 0; i < vertices; i++) {
                    x[i] = 100000; y[i] = 100000;
                }
                x[100] = 0; y[100] = 100000;
                return isX ? x : y;
            }
            case MY_NEG: {
                int vertices = 200;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                for (int i = 0; i < vertices; i++) {
                    x[i] = -1000; y[i] = -1000;
                }
                x[100] = 0; y[100] = -1000;
                return isX ? x : y;
            }
            case MY_D: {
                int vertices = 4;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                x[0] = 0; y[0] = 0;
                x[1] = 1; y[1] = 0;
                x[2] = 0; y[2] = 1;
                x[3] = 0; y[3] = 0;
                return isX ? x : y;
            }
            case MY_3: {
                int vertices = 3;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                x[0] = 0; y[0] = 0;
                x[1] = 1; y[1] = 0;
                x[2] = 0; y[2] = 1;
                return isX ? x : y;
            }
            case MY_4: {
                int vertices = 4;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                x[0] = 0; y[0] = 0;
                x[1] = 1; y[1] = 0;
                x[2] = 0; y[2] = 1;
                x[3] = 1; y[3] = 1;
                return isX ? x : y;
            }
            case BOOK_YES: {
                int vertices = 5;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                x[0] = 0; y[0] = 0;
                x[1] = 0; y[1] = 2;
                x[2] = 1; y[2] = 1;
                x[3] = 3; y[3] = 0;
                x[4] = 3; y[4] = 2;
                return isX ? x : y;
            }
            case BOOK_YES_1: {
                int vertices = 0;
                int[] x = new int[vertices];
                int[] y = new int[vertices];
                return isX ? x : y;
            }
        }
        return new int[0];
    }

    public static void expect(double actual, double expect, String messgae) {
        if (actual != expect) {
            throw new Error("Error: " + messgae + ", actual: " + actual);
        }
    }
}

