import java.util.*;

public class Bipartite {
    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_COMPLEX,
        MY_COMPLEX_1,
        MY_COMPLEX_2,
        BOOK_YES,
        BOOK_NO,
    }

    static class BipartiteError extends Error {}

    static class Vertices {
        public int index;

        public int type = -1;

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
                if (item != null) {
                    items.add(item);
                }
            }
            return items;
        }

        public boolean hasNeighbors() {
            return this.neighbors.size() > 0;
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
                    Vertices childVertices = getOrCreate(state, next);
                    baseVertices.addNeighbor(childVertices);
                    childVertices.addNeighbor(baseVertices);
                }
            }
        }
    }

    private static void bfs(ArrayList<Vertices> vertices, int type) {
        if (vertices == null || vertices.size() == 0) {
            return;
        }
        ArrayList<Vertices> nexItems = new ArrayList<Vertices>();

        boolean ret = false;

        for (int i = 0; i < vertices.size(); i++) {
            Vertices item = vertices.get(i);
            if (item.isVisited()) {
                ret = true;
                return;
            }
            item.type = type;


            if (!item.hasNeighbors()) {
                throw new BipartiteError();
            }

            ArrayList<Vertices> neighbors = item.getNeighbors();
            for (int j = 0; j < neighbors.size(); j++) {
                Vertices neighbor = neighbors.get(j);
                if (neighbor.type > -1 && neighbor.type == item.type) {
                    throw new BipartiteError();
                }
                if (!neighbor.isVisited() && !neighbor.taken) {
                    neighbor.taken = true;
                    nexItems.add(neighbor);
                }
            }

            item.setVisited();
        }

        if (ret) {
            return;
        }
        int nextType = type == 1 ? 2 : 1;
        bfs(nexItems, nextType);
    }


    private static int bipartite(ArrayList<Integer>[] adj) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();

        fillGraph(adj, graph);

        int res = adj.length > 0 ? 1 : 0;
        try {
            for (int i = 0; i < adj.length; i++) {
                Vertices startVertices = getOrCreate(graph, i);
                ArrayList<Vertices> items = new ArrayList<Vertices>();
                items.add(startVertices);
                bfs(items, 1);
            }
        } catch (BipartiteError err) {
            res = 0;
        }
        return res;
    }

    public static void main(String[] args) {
//        try {
//            test();
//            System.out.println("Tests passed");
//        } catch (Error err) {
//            System.out.println(err.getMessage());
//        }

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        System.out.println(bipartite(adj));
    }

    public static void test() {
        expect(bipartite(prepare(TestCase.EMPTY)), 0, "Empty");

        expect(bipartite(prepare(TestCase.NO_EDGES)), 0, "No edges");

        expect(bipartite(prepare(TestCase.MY_COMPLEX)), 0, "My complex");

        expect(bipartite(prepare(TestCase.MY_COMPLEX_1)), 1, "My complex 1");

        expect(bipartite(prepare(TestCase.MY_COMPLEX_2)), 0, "My complex 2");

        expect(bipartite(prepare(TestCase.BOOK_YES)), 1, "Book yes");

        expect(bipartite(prepare(TestCase.BOOK_NO)), 0, "Book no");
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
                return adj;
            }
            case MY_COMPLEX: {
                vertices = 6;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(3);
                adj[2].add(3);
                adj[3].add(1);
                adj[4].add(1);
                return adj;
            }
            case MY_COMPLEX_1: {
                vertices = 6;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(3);
                adj[2].add(3);
                adj[3].add(1);
                adj[4].add(1); adj[4].add(5);
                return adj;
            }
            case MY_COMPLEX_2: {
                vertices = 6;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(3);
                adj[2].add(3);
                adj[3].add(1);
                adj[4].add(1); adj[4].add(5);
                adj[5].add(1);
                return adj;
            }
            case BOOK_YES: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(3);
                adj[2].add(3);
                adj[3].add(1);
                adj[4].add(1);
                return adj;
            }
            case BOOK_NO: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1);
                adj[1].add(2);
                adj[2].add(0);
                adj[3].add(0);
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

