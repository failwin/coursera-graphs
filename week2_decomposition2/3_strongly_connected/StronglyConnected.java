import java.util.*;

public class StronglyConnected {
    static int counter = 1;

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
        public boolean visited = false;

        public int pre = -1;
        public int post = -1;
    }

    private static void dfs(ArrayList<Integer>[] adj, int[] used, Map<Integer, Vertices> state, int start) {
        if (used[start] != 0) {
            return;
        }
        used[start] = 1;
        Vertices vertices = state.get(start);
        if (vertices == null) {
            vertices = new Vertices();
            state.put(start, vertices);
        }
        vertices.pre = counter;
        counter++;

        ArrayList<Integer> neighbors = findByIndex(adj, start);

        if (neighbors != null) {
            for (int i = 0; i < neighbors.size(); i++) {
                int next = neighbors.get(i);
                dfs(adj, used, state, next);
            }
        }

        vertices.post = counter;
        counter++;
    }

    private static ArrayList<Integer> findByIndex(ArrayList<Integer>[] adj, int index) {
        for (int i = 0; i < adj.length; i++) {
            ArrayList<Integer> item = adj[i];
            if (item != null && item.size() > 0 && item.get(0) == index) {
                ArrayList<Integer> res = new ArrayList<Integer>();
                for (int j = 1; j < item.size(); j++) {
                    res.add(item.get(j));
                }
                return res;
            }
        }
        return null;
    }

    private static int numberOfSCC(ArrayList<Integer>[] adj) {
        int used[] = new int[adj.length];
        Map<Integer, Vertices> state = new HashMap<Integer, Vertices>();

        int counter = 1;
        for (int i = 0; i < adj.length; i++) {
            if (used[i] == 0) {
                // not visited
                dfs(adj, used, state, i);
            }
        }

        return 0;
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
        // should return 0 if no Start vertices
        expect(numberOfSCC(prepare(TestCase.MY_COMPLEX)), 0, "Empty list");
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
            case MY_SIMPLE: {
                vertices= 3;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(2);
                adj[1].add(3); adj[1].add(2);
                adj[2].add(2); adj[2].add(3);
                return adj;
            }
            case MY_COMPLEX: {
                vertices = 5;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                adj[1].add(2); adj[1].add(3);
                adj[2].add(3); adj[2].add(4);
                adj[3].add(4); adj[3].add(2);
                return adj;
            }
            case MY_COMPLEX_2: {
                vertices= 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2); adj[0].add(2);
                adj[1].add(4); adj[1].add(3);
                return adj;
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
                return adj;
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
                return adj;
            }
            case BOOK_FAILED: {
                vertices = 4;
                adj = (ArrayList<Integer>[])new ArrayList[vertices];
                for (int i = 0; i < vertices; i++) {
                    adj[i] = new ArrayList<Integer>();
                }
                adj[0].add(1); adj[0].add(2);
                adj[1].add(3); adj[1].add(2);
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

