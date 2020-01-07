import java.util.*;

public class Acyclicity {

    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_SIMPLE,
        MY_SIMPLE_NO,
        MY_FULL,
        BOOK_HAS,
        BOOK_HASNOT,
    }

    static class LoopError extends Error {}

    static class Vertices {
        public int index;

        public boolean hasInput = false;

        public Vertices parent;

        public boolean visited = false;

        public boolean asked = false;

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

        public void setParent(Vertices parent) {
            this.parent = parent;
        }

        public boolean hasChilds() {
            return this.neighbors.size() > 0;
        }

        public Vertices getUnvisitedNeighbor() {
            for (Integer index : this.neighbors.keySet()) {
                Vertices neighbor = this.neighbors.get(index);
                if (neighbor != null) {
                    if (!neighbor.visited) {
                        return neighbor;
                    }
                }
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
            for (int i = 0; i < verticesCount; i++) {
                ArrayList<Integer> edge = this.edges[i];
                int index = 0;
                Vertices base = null;
                for (Integer key : edge) {
                    if (index == 0) {
                        base = this.addVertices(key);
                    } else if (base != null) {
                        Vertices vehicle2 = this.addVertices(key);
                        vehicle2.hasInput = true;
                        base.addNeighbor(vehicle2);
                    }
                    index++;
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
                if (this.lastChecked > this.edgesLength) {
                    exit = true;
                    if (test != null && test.asked && !test.visited) {
                        throw new LoopError();
                    }
                    return null;
                }
                test = this.vertices.get(this.lastChecked);
                if (test == null) {
                    // no vertices mention
                    this.groups++;
                    this.lastChecked++;
                } else {
                    if (test.asked) {
                        throw new LoopError();
                    }
                    test.asked = true;
                    this.lastChecked++;
                    if (!test.visited) { // !test.hasInput &&
                        exit = true;
                        return test;
                    }
                }
            }
            return test;
        }

        public int explore(int startIndex) {
            Vertices startVertices = this.vertices.get(startIndex);

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
                if (newVertices.visited) {
                    exit = true;
                    throw new LoopError();
                }

                newVertices.markVisited();

                boolean hasChilds = newVertices.hasChilds();

                if (hasChilds) {
                    Vertices newPossibleVertices = newVertices.getUnvisitedNeighbor();
                    if (newPossibleVertices != null) {
                        newPossibleVertices.setParent(newVertices);
                        newVertices = newPossibleVertices;
                    } else {
                        exit = true;
                        throw new LoopError();
//                        return 0;
                    }
                } else {
                    exit = true;
                    return 0;
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
                        this.explore(test.index);
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
//        }
//        System.out.println(acyclic(adj));
    }

    public static void test() {
//        expect(prepare(TestCase.EMPTY).hasCycle(), 0, "Empty list");
//
//        expect(prepare(TestCase.NO_EDGES).hasCycle(), 0, "No edges");
//
//        expect(prepare(TestCase.MY_SIMPLE).hasCycle(), 1, "My simple");
//
        expect(prepare(TestCase.MY_SIMPLE_NO).hasCycle(), 0, "My simple no");
//
//        expect(prepare(TestCase.MY_FULL).hasCycle(), 1, "My full");
//
//        expect(prepare(TestCase.BOOK_HAS).hasCycle(), 1, "Book has");

        // expect(prepare(TestCase.BOOK_HASNOT).hasCycle(), 0, "Book has not");
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
                adj[0].add(2); adj[0].add(3);
                adj[1].add(3); adj[1].add(4);
                adj[2].add(4); adj[2].add(5);
                adj[3].add(5); adj[3].add(3);
                break;
            }
            case MY_SIMPLE_NO: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(5);
                adj[1].add(3); adj[1].add(2);
                adj[2].add(2); adj[2].add(4);
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
            case BOOK_HAS: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                adj[1].add(4); adj[1].add(1);
                adj[2].add(2); adj[2].add(3);
                adj[3].add(3); adj[3].add(1);
                break;
            }
            case BOOK_HASNOT: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(3); adj[0].add(4);
                adj[1].add(2); adj[1].add(3); adj[1].add(5);
                adj[2].add(3); adj[2].add(4); adj[2].add(5);
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

