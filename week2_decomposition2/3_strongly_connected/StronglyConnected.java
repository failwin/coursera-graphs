import java.util.*;

public class StronglyConnected {
    static int counter = 1;

    public enum TestCase {
        EMPTY,
        NO_EDGES,
        MY_COMPLEX,
        BOOK_YES,
        BOOK_NO,
    }

    static class Vertices {
        public int index;

        public boolean visited = false;
        public boolean visitedR = false;

        public int pre = -1;
        public int post = -1;

        public int preR = -1;
        public int postR = -1;

        private Map<Integer, Vertices> neighbors = new HashMap<Integer, Vertices>();

        private Map<Integer, Vertices> reverseNeighbors = new HashMap<Integer, Vertices>();

        public Vertices(int index) {
            this.index = index;
        }

        public void setVisited(boolean forReverse) {
            if (forReverse) {
                this.visitedR = true;
            } else {
                this.visited = true;
            }
        }

        public boolean isVisited(boolean forReverse) {
            if (forReverse) {
                return this.visitedR;
            } else {
                return this.visited;
            }
        }

        public void addNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            if (!this.neighbors.containsKey(index)) {
                this.neighbors.put(index, neighbor);
            }
        }

        public void addReversNeighbor(Vertices neighbor) {
            int index = neighbor.index;
            if (!this.reverseNeighbors.containsKey(index)) {
                this.reverseNeighbors.put(index, neighbor);
            }
        }

        public ArrayList<Vertices> getNeighbors(boolean forReverse) {
            if (forReverse) {
                return new ArrayList<Vertices>(this.reverseNeighbors.values());
            }
            return new ArrayList<Vertices>(this.neighbors.values());
        }

        public void setPre(int pre, boolean forReverse) {
            if (forReverse) {
                this.preR = pre;
            } else {
                this.pre = pre;
            }
        }

        public int getPre(boolean forReverse) {
            if (forReverse) {
                return this.preR;
            } else {
                return this.pre;
            }
        }

        public void setPost(int post, boolean forReverse) {
            if (forReverse) {
                this.postR = post;
            } else {
                this.post = post;
            }
        }

        public int getPost(boolean forReverse) {
            if (forReverse) {
                return this.postR;
            } else {
                return this.post;
            }
        }
    }

    private static void dfs(Vertices vertices, boolean forReverse) {
        if (vertices.isVisited(forReverse)) {
            return;
        }
        vertices.setVisited(forReverse);

        vertices.setPre(counter, forReverse);
        counter++;

        ArrayList<Vertices> neighbors = vertices.getNeighbors(forReverse);

        if (neighbors != null) {
            for (int i = 0; i < neighbors.size(); i++) {
                Vertices next = neighbors.get(i);
                dfs(next, forReverse);
            }
        }

        vertices.setPost(counter, forReverse);
        counter++;
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

                        childVertices.addReversNeighbor(baseVertices);
                    }
                }
            }
        }
    }

    static class VerticesPostRComparator implements Comparator<Vertices>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Vertices a, Vertices b)
        {
            return b.postR - a.postR;
        }
    }

    static VerticesPostRComparator verticesPostRComparator = new VerticesPostRComparator();

    private static int numberOfSCC(ArrayList<Integer>[] adj) {
        Map<Integer, Vertices> graph = new HashMap<Integer, Vertices>();

        fillGraph(adj, graph);

        // DFS for revert graph
        int counter = 1;
        for (int i = 0; i < adj.length; i++) {
            Vertices vertices = getOrCreate(graph, i);
            dfs(vertices, true);
        }

        // sort
        ArrayList<Vertices> list = new ArrayList<Vertices>(graph.values());
        //Vertices[] sorted = new Vertices[graph.size()];
        Collections.sort(list, verticesPostRComparator);

        int SCCcount = 0;
        for (int i = 0; i < list.size(); i++) {
            Vertices vertices = list.get(i);
            if (!vertices.isVisited(false)) {
                SCCcount++;
            }
            dfs(vertices, false);
        }

        return SCCcount;
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
//        System.out.println(numberOfSCC(adj));
    }

    public static void test() {
        expect(numberOfSCC(prepare(TestCase.EMPTY)), 0, "Empty");

        expect(numberOfSCC(prepare(TestCase.NO_EDGES)), 5, "No edges");

        expect(numberOfSCC(prepare(TestCase.MY_COMPLEX)), 3, "My complex");

        expect(numberOfSCC(prepare(TestCase.BOOK_YES)), 2, "Book yes");

        expect(numberOfSCC(prepare(TestCase.BOOK_NO)), 5, "Book no");
    };

    public static ArrayList<Integer>[] prepare(TestCase testCase) {
        int vertices;
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[1];

        switch (testCase) {
            case EMPTY: {
                vertices= 0;
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
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(0); adj[0].add(1);
                adj[1].add(1); adj[1].add(2);
                adj[2].add(2); adj[2].add(3);
                adj[3].add(3); adj[3].add(1);
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
                adj[1].add(1); adj[1].add(0);
                adj[2].add(2); adj[2].add(1); adj[2].add(0);
                adj[3].add(3); adj[3].add(2); adj[3].add(0);
                adj[4].add(4); adj[4].add(1); adj[3].add(2);
                return adj;
            }
        }
        return adj;
    }

    public static void expect(int actual, int expect, String messgae) {
        if (actual != expect) {
            throw new Error("Error: " + messgae);
        }
    }
}

