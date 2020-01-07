import java.util.*;

public class Reachability {
    public enum TestCase {
        EMPTY,
        MY_SIMPLE,
        MY_COMPLEX,
        MY_COMPLEX_2,
        MY_COMPLEX_3,
        BOOK_SUCCESS,
        BOOK_FAILED,
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

        public Engine (ArrayList<Integer>[] adj) {
            this.edges = adj;

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
                        base.addNeighbor(vehicle2);
                        vehicle2.addNeighbor(base);
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

        public int explore(int startIndex, int endIndex) {
            Vertices startVertices = this.vertices.get(startIndex);
            Vertices endVertices = this.vertices.get(endIndex);

            if (startVertices == null || endVertices == null) {
                return 0;
            }

            if (startVertices.index == endVertices.index) {
                return 1;
            }

            Vertices newVertices = startVertices;
            boolean exit = false;
            while (!exit) {
                if (newVertices == null) {
                    exit = true;
                    return 0;
                }
                if (newVertices.index == endVertices.index) {
                    exit = true;
                    return 1;
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
    }

    private static int reach(ArrayList<Integer>[] adj, int x, int y) {


        Engine engine = new Engine(adj);
        return engine.explore(x, y);
        //write your code here
//        return 0;
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
//        System.out.println(reach(adj, x, y));
    }

    public static void test() {
        // should return 0 if no Start vertices
        expect(prepare(TestCase.EMPTY).explore(10, 2), 0, "Empty list");

        // should return 0 if no Start vertices
        expect(prepare(TestCase.MY_SIMPLE).explore(10, 2), 0, "Not exist Start");
        expect(prepare(TestCase.MY_SIMPLE).explore(-1, 2), 0, "Not exist Start");

        // should return 0 if no End vertices
        expect(prepare(TestCase.MY_SIMPLE).explore(1, 20), 0, "Not exist End");
        expect(prepare(TestCase.MY_SIMPLE).explore(1, -2), 0, "Not exist End");

        // should return 1 if no Start == End vertices
        expect(prepare(TestCase.MY_SIMPLE).explore(2, 2), 1, "Same points");

        // should return 1 for 1 - 2
        expect(prepare(TestCase.MY_SIMPLE).explore(1, 2), 1, "Connect 1 - 2");

        // should return 1 for 1 - 3
        expect(prepare(TestCase.MY_SIMPLE).explore(1, 3), 1, "Connect 1 - 3");

        // should return 1 for 3 - 1
        expect(prepare(TestCase.MY_SIMPLE).explore(3, 1), 1, "Connect 3 - 1");

        // mix
        expect(prepare(TestCase.MY_COMPLEX).explore(1, 4), 0, "Mix 1 No Connect 1 - 4");
        expect(prepare(TestCase.MY_COMPLEX).explore(4, 1), 0, "Mix 2 No Connect 4 - 1");
        expect(prepare(TestCase.MY_COMPLEX_2).explore(4, 1), 0, "Mix 3 No Connect 4 - 1");
        expect(prepare(TestCase.MY_COMPLEX_2).explore(1, 3), 0, "Mix 4 No Connect 1 - 3");
        expect(prepare(TestCase.MY_COMPLEX_2).explore(2, 1), 1, "Mix 5 Connect 2 - 1");

        // should return 1 for 1 - 4
        expect(prepare(TestCase.BOOK_SUCCESS).explore(1, 4), 1, "Connect 1 - 4");

        // should return 0 for 1 - 4
        expect(prepare(TestCase.BOOK_FAILED).explore(1, 4), 0, "No Connect 1 - 4");

        expect(prepare(TestCase.MY_COMPLEX_3).explore(1, 4), 1, "1, 4");
        expect(prepare(TestCase.MY_COMPLEX_3).explore(5, 1), 1, "1, 5");
        expect(prepare(TestCase.MY_COMPLEX_3).explore(1, 6), 1, "1, 6");
        expect(prepare(TestCase.MY_COMPLEX_3).explore(4, 2), 1, "1");
        expect(prepare(TestCase.MY_COMPLEX_3).explore(2, 5), 1, "1");
        expect(prepare(TestCase.MY_COMPLEX_3).explore(6, 2), 1, "1");
    };

    public static Engine prepare(TestCase testCase) {
        int vertices;
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[1];

        switch (testCase) {
            case EMPTY: {
                vertices= 0;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                break;
            }
            case MY_SIMPLE: {
                vertices= 3;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(2);
                adj[1].add(3); adj[1].add(2);
                adj[2].add(2); adj[2].add(3);
                break;
            }
            case MY_COMPLEX: {
                vertices= 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                adj[1].add(2); adj[1].add(3);
                break;
            }
            case MY_COMPLEX_2: {
                vertices= 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(2);
                adj[1].add(4); adj[1].add(3);
                break;
            }
            case MY_COMPLEX_3: {
                vertices= 6;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(3);
                adj[1].add(2); adj[1].add(4); adj[1].add(5);
                adj[2].add(3); adj[2].add(5); adj[2].add(6);
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
                adj[2].add(4); adj[2].add(3);
                adj[3].add(1); adj[3].add(4);
                break;
            }
            case BOOK_FAILED: {
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

