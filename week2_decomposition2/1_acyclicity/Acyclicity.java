import java.util.*;

public class Acyclicity {

    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_SIMPLE,
        MY_SIMPLE_NO,
        MY_FULL,
        MY_TREE,
        MY_TREE_LOOP,
        BOOK_HAS,
        BOOK_HASNOT,
    }

    static class LoopError extends Error {}

    static class Vertices {
        public int index;

        public Vertices parent;

        public boolean visited = false;

        public boolean reversed = false;

        private Map<Integer, Vertices> neighbors = new HashMap<Integer, Vertices>();

        public Vertices(int index) {
            this.index = index;
        }

        public void addNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            if (!this.neighbors.containsKey(index)) {
                this.neighbors.put(index, neighbor);
            }
        }

        public void markVisited() {
            this.visited = true;
        }

        public void markReversed() {
            this.reversed = true;
        }

        public void setParent(Vertices parent) {
            this.parent = parent;
        }

        public Vertices getUnvisitedNeighbor() {
            boolean hasVisitedNotReversed = false;
            for (Integer index : this.neighbors.keySet()) {
                Vertices neighbor = this.neighbors.get(index);
                if (neighbor != null) {
                    if (!neighbor.visited) {
                        return neighbor;
                    }
                    if (!neighbor.reversed) {
                        hasVisitedNotReversed = true;
                    }
                }
            }
            if (hasVisitedNotReversed) {
                throw new LoopError();
            }
            return null;
        }
    }

    static class Engine {
        private Map<Integer, Vertices> vertices = new HashMap<Integer, Vertices>();

        private ArrayList<Integer>[] edges;
        private int edgesLength;

        int lastChecked = 1;

        int groups = 0;

        public Engine (ArrayList<Integer>[] adj) {
            this.edges = adj;
            this.edgesLength = this.edges.length;

            this.fillMap();
        }

        private void fillMap() {
            int verticesCount = this.edges.length;
            for (int start = 0; start < verticesCount; start++) {
                ArrayList<Integer> edge = this.edges[start];
                Vertices base = this.addVertices(start);
                for (Integer stop : edge) {
                    Vertices vehicle2 = this.addVertices(stop);
                    base.addNeighbor(vehicle2);
                }
            }
        }

        private Vertices addVertices(int index) {
            if (this.vertices.containsKey(index)) {
                return this.vertices.get(index);
            }
            Vertices newVertices = new Vertices(index);
            this.vertices.put(index, newVertices);
            return newVertices;
        }

        private Vertices getNext() {
            Vertices test = null;

            boolean exit = false;
            while (!exit) {
                if (this.lastChecked > this.edgesLength - 1) {
                    exit = true;
                    return null;
                }
                test = this.vertices.get(this.lastChecked);
                if (test == null) {
                    // no vertices mention
                    this.groups++;
                    this.lastChecked++;
                } else {
                    if (!test.visited) {
                        exit = true;
                        return test;
                    } else {
                        this.lastChecked++;
                    }
                }
            }
            return test;
        }

        public int explore(Vertices startVertices) {
            if (startVertices == null) {
                return 0;
            }

            Vertices newVertices = startVertices;
            boolean exit = false;
            while (!exit) {
                if (newVertices == null) {
                    exit = true;
                    return 0;
                }
                if (newVertices.visited && newVertices.index == startVertices.index) {
                    newVertices.markReversed();
                    exit = true;
                    return 0;
                }

                newVertices.markVisited();

                Vertices newPossibleVertices = newVertices.getUnvisitedNeighbor();
                if (newPossibleVertices == null) {
                    newVertices.markReversed();
                    newVertices = newVertices.parent;
                } else {
                    newPossibleVertices.setParent(newVertices);
                    newVertices = newPossibleVertices;

                }
            }
            return 0;
        }

        public int hasCycle() {
            Vertices test = null;

            try {
                boolean exit = false;
                while (!exit) {
                    test = this.getNext();
                    if (test == null) {
                        // end
                        exit = true;
                        return 0;
                    } else {
                        this.explore(test);
                        this.groups++;
                    }
                }
            } catch (LoopError err) {
                return 1;
            }
            return 0;
        }
    }


    private static int acyclic(ArrayList<Integer>[] adj) {
        //write your code here
        Engine engine = new Engine(adj);
        return engine.hasCycle();
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
        }
        System.out.println(acyclic(adj));
    }

    public static void test() {
        expect(prepare(TestCase.EMPTY).hasCycle(), 0, "Empty list");

        expect(prepare(TestCase.NO_EDGES).hasCycle(), 0, "No edges");

        expect(prepare(TestCase.MY_SIMPLE).hasCycle(), 1, "My simple");

        expect(prepare(TestCase.MY_SIMPLE_NO).hasCycle(), 0, "My simple no");

//        expect(prepare(TestCase.MY_FULL).hasCycle(), 1, "My full");
//
//        expect(prepare(TestCase.MY_TREE).hasCycle(), 0, "My tree");
//
//        expect(prepare(TestCase.MY_TREE_LOOP).hasCycle(), 1, "My tree loop");

        expect(prepare(TestCase.BOOK_HAS).hasCycle(), 1, "Book has");

         expect(prepare(TestCase.BOOK_HASNOT).hasCycle(), 0, "Book has not");
    };

    public static Engine prepare(TestCase testCase) {
        int vertices;
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[1];

        switch (testCase) {
            case EMPTY: {
                vertices = 0;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                break;
            }
            case NO_EDGES: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                break;
            }
            case MY_SIMPLE: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[1].add(2);
                adj[2].add(3);
                adj[3].add(4);
                adj[4].add(2);
                break;
            }
            case MY_SIMPLE_NO: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(4);
                adj[1].add(2);
                adj[1].add(3);
                break;
            }
            case MY_FULL: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(4); adj[0].add(3);
                adj[1].add(3); adj[1].add(2); adj[1].add(4);
                adj[2].add(4); adj[2].add(2);
                adj[3].add(2); adj[3].add(1);
                break;
            }
            case MY_TREE: {
                vertices = 9;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(8);
                adj[1].add(2); adj[1].add(3); adj[1].add(4);
                adj[2].add(4); adj[2].add(5);
                adj[3].add(5); adj[3].add(6); adj[3].add(7);
                adj[4].add(8); adj[4].add(5); adj[4].add(9);
                break;
            }
            case MY_TREE_LOOP: {
                vertices = 9;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(8);
                adj[1].add(2); adj[1].add(3); adj[1].add(4);
                adj[2].add(4); adj[2].add(5);
                adj[3].add(5); adj[3].add(6); adj[3].add(7);
                adj[4].add(8); adj[4].add(5); adj[4].add(9);
                adj[5].add(7); adj[5].add(8);
                break;
            }
            case BOOK_HAS: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1);
                adj[1].add(2);
                adj[2].add(0);
                adj[3].add(0);
                break;
            }
            case BOOK_HASNOT: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(3);
                adj[1].add(2); adj[1].add(4);
                adj[2].add(3); adj[2].add(4);
                break;
            }
        }
        return new Engine(adj);
    }

    public static void expect(int actual, int expect, String messgae) {
        if (actual != expect) {
            throw new Error("Error: " + messgae);
        }
    }
}

