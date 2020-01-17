import java.util.*;

public class BFS {

    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_COMPLEX,
        BOOK_YES,
        BOOK_NO,
    }

    static class Vertices {
        public int index;

        public int distance = -1;

        public boolean visited = false;

        public boolean taken = false;

        private Map<Integer, Vertices> neighbors = new HashMap<Integer, Vertices>();

        public Vertices(int index) {
            this.index = index;
        }

        public void setVisited() {
            this.visited = true;
        }

        public boolean isVisited() {
            return this.visited;
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
                if (item != null && !item.isVisited() && !item.taken) {
                    item.taken = true;
                    items.add(item);
                }
            }
            return items;
        }
    }

    private static Vertices getOrCreate(Map<Integer, Vertices> state, int index) {
        if (state.containsKey(index)) {
            return state.get(index);
        }
        Vertices newVertices = new Vertices(index);
        state.put(index, newVertices);
        return newVertices;
    }

    private static void fillGraph(ArrayList<Integer>[] adj, Map<Integer, Vertices> state) {
        for (int index = 0; index < adj.length; index++) {
            Vertices baseVertices = getOrCreate(state, index);

            ArrayList<Integer> neighbors = adj[index];

            if (neighbors.size() > 0) {
                for (int j = 0; j < neighbors.size(); j++) {
                    int next = neighbors.get(j);
                    if (j != 0) {
                        Vertices childVertices = getOrCreate(state, next);
                        baseVertices.addNeighbor(childVertices);
                        childVertices.addNeighbor(baseVertices);
                    }
                }
            }
        }
    }

    private static void bfs(ArrayList<Vertices> vertices, Vertices stopVertices, int distance) {
        if (vertices == null || vertices.size() == 0) {
            return;
        }
        ArrayList<Vertices> nexItems = new ArrayList<Vertices>();

        boolean ret = false;

        for (int i = 0; i < vertices.size(); i++) {
            Vertices item = vertices.get(i);
            item.distance = distance;

            if (item.index == stopVertices.index) {
                ret = true;
                return;
            }

            item.setVisited();

            ArrayList<Vertices> neighbors = item.getNeighbors();

            nexItems.addAll(neighbors);
        }

        if (ret) {
            return;
        }
        bfs(nexItems, stopVertices, distance + 1);
    }

    private static int distance(ArrayList<Integer>[] adj, int s, int t) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();

        fillGraph(adj, graph);

        Vertices startVertices = getOrCreate(graph, s);
        Vertices stopVertices = getOrCreate(graph, t);

        ArrayList<Vertices> items = new ArrayList<Vertices>();
        items.add(startVertices);
        bfs(items, stopVertices, 0);

        return stopVertices.distance;
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
//        for (int i = 0; i < n; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        for (int i = 0; i < m; i++) {
//            int x, y;
//            x = scanner.nextInt();
//            y = scanner.nextInt();
//            adj[x - 1].add(y - 1);
//            adj[y - 1].add(x - 1);
//        }
//        int x = scanner.nextInt() - 1;
//        int y = scanner.nextInt() - 1;
//        System.out.println(distance(adj, x, y));
    }

    public static void test() {
        expect(distance(prepare(TestCase.EMPTY), 0, 0), 0, "Empty");

        expect(distance(prepare(TestCase.NO_EDGES), 0, 3), -1, "No edges");

        expect(distance(prepare(TestCase.MY_COMPLEX), 0, 6), 4, "My complex");

        expect(distance(prepare(TestCase.BOOK_YES), 1, 3), 2, "Book yes");

        expect(distance(prepare(TestCase.BOOK_NO), 2, 4), -1, "Book no");
    };

    public static ArrayList<Integer>[] prepare(TestCase testCase) {
        int vertices;
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[1];

        switch (testCase) {
            case EMPTY: {
                vertices = 0;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                return adj;
            }
            case NO_EDGES: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                break;
            }
            case MY_COMPLEX: {
                vertices = 7;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(0); adj[0].add(1); adj[0].add(2);
                adj[1].add(1); adj[1].add(3);
                adj[2].add(2); adj[2].add(3);
                adj[3].add(3); adj[3].add(4); adj[3].add(5);
                adj[4].add(4); adj[4].add(5); adj[4].add(6);
                adj[5].add(5); adj[5].add(4); adj[5].add(6);
                return adj;
            }
            case BOOK_YES: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(0); adj[0].add(1);
                adj[1].add(1); adj[1].add(2);
                adj[2].add(2); adj[2].add(0);
                adj[3].add(3); adj[3].add(0);
                return adj;
            }
            case BOOK_NO: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(0); adj[0].add(2); adj[0].add(3);
                adj[2].add(2); adj[2].add(3);
                adj[4].add(4); adj[4].add(1);
                return adj;
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

