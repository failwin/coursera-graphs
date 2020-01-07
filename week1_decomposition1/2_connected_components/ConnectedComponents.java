import java.util.*;

public class ConnectedComponents {
    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_SIMPLE,
        MY_COMPLEX,
        MY_FULL,
        BOOK_SUCCESS,
    }

    static class Vertices {
        public int index;

        public Vertices parent;

        public boolean visited = false;

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

        public Vertices getUnvisitedNeighbor() {
            for (Integer index : this.neighbors.keySet()) {
                Vertices neighbor = this.neighbors.get(index);
                if (neighbor != null && !neighbor.visited) {
                    return neighbor;
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
                if (edge.size() == 2) {
                    int vehicleIndex1 = edge.get(0);
                    int vehicleIndex2 = edge.get(1);
                    Vertices vehicle1 = this.addVertices(vehicleIndex1);
                    Vertices vehicle2 = this.addVertices(vehicleIndex2);
                    vehicle1.addNeighbor(vehicle2);
                    vehicle2.addNeighbor(vehicle1);
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
            return test == null ? null : test;
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
                if (newVertices.visited && newVertices.index == startVertices.index) {
                    exit = true;
                    return 0;
                }

                newVertices.markVisited();

                Vertices newPossibleVertices = newVertices.getUnvisitedNeighbor();
                if (newPossibleVertices == null) {
                    newVertices = newVertices.parent;
                } else {
                    newPossibleVertices.setParent(newVertices);
                    newVertices = newPossibleVertices;

                }
            }
            return 0;
        }

        public int getCount() {
            Vertices test = null;

            boolean exit = false;
            while (!exit) {
                test = this.getNext();
                if (test == null) {
                    // end
                    exit = true;
                    return this.groups;
                } else {
                    this.explore(test.index);
                    this.groups++;
                }
            }
            return this.groups;
        }
    }
    private static int numberOfComponents(ArrayList<Integer>[] adj) {
        Engine engine = new Engine(adj);
        return engine.getCount();
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
//        System.out.println(numberOfComponents(adj));
    }

    public static void test() {
        // should return 0 if no Start vertices
        expect(prepare(TestCase.EMPTY).getCount(), 0, "Empty list");

        expect(prepare(TestCase.NO_EDGES).getCount(), 5, "No edges");

        expect(prepare(TestCase.MY_SIMPLE).getCount(), 3, "My simple");

        expect(prepare(TestCase.MY_COMPLEX).getCount(), 4, "My complex");

        expect(prepare(TestCase.MY_FULL).getCount(), 1, "My full");

        expect(prepare(TestCase.BOOK_SUCCESS).getCount(), 2, "Book success");
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
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                break;
            }
            case MY_COMPLEX: {
                vertices = 7;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(4);
                adj[1].add(6); adj[1].add(4);
                adj[2].add(7); adj[2].add(5);
                break;
            }
            case MY_FULL: {
                vertices = 7;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(4);
                adj[1].add(6); adj[1].add(4);
                adj[2].add(7); adj[2].add(5);
                adj[3].add(7); adj[3].add(4);
                adj[4].add(2); adj[4].add(4);
                adj[5].add(1); adj[5].add(3);
                adj[6].add(3); adj[6].add(6);
                break;
            }
            case BOOK_SUCCESS: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                adj[1].add(3); adj[1].add(2);
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

