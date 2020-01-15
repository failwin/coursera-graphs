import java.util.*;

public class Toposort {

    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        int used[] = new int[adj.length];
        ArrayList<Integer> order = new ArrayList<Integer>();

        for (int i = 0; i < adj.length; i++) {
            if (used[i] == 0) {
                // not visited
                dfs(adj, used, order, i);
            }
        }
        Collections.reverse(order);
        return order;
    }

    private static void dfs(ArrayList<Integer>[] adj, int[] used, ArrayList<Integer> order, int start) {
        if (used[start] != 0) {
            return;
        }
        used[start] = 1;
        ArrayList<Integer> vertices = findByIndex(adj, start);

        if (vertices != null) {
            for (int i = 0; i < vertices.size(); i++) {
                int next = vertices.get(i);
                dfs(adj, used, order, next);
            }
        }
        order.add(start);
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
        ArrayList<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }

    public static void test() {
//        int vertices = 9;
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        adj[0].add(1); adj[0].add(2); adj[0].add(6);
//        adj[1].add(2); adj[1].add(3); adj[1].add(7);
//        adj[2].add(3); adj[2].add(4); adj[2].add(5);
//        adj[3].add(7); adj[3].add(8); adj[3].add(9);
//
//        int used[] = new int[adj.length];
//        ArrayList<Integer> order = new ArrayList<Integer>();
//
//        dfs(adj, used, order, 1);

//        int vertices = 4;
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        adj[0].add(0); adj[0].add(1);
//        adj[1].add(3); adj[1].add(0);
//        adj[2].add(2); adj[2].add(0);
//
//        ArrayList<Integer> order = toposort(adj); // 4 3 1 2

//        int vertices = 5;
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        adj[0].add(2); adj[0].add(1);
//        adj[1].add(3); adj[1].add(2); adj[1].add(1);
//        adj[2].add(4); adj[2].add(3); adj[2].add(1);
//        adj[3].add(5); adj[3].add(2); adj[3].add(3);
//
//        ArrayList<Integer> order = toposort(adj); // 5 4 3 2 1


//        int vertices = 8;
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        adj[0].add(1); adj[0].add(4);
//        adj[1].add(2); adj[1].add(4); adj[1].add(6); adj[1].add(3);
//        adj[2].add(3); adj[2].add(6);
//
//        ArrayList<Integer> order = toposort(adj); // 8 7 5 2 3 6 1 4


//        int vertices = 8;
//        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[vertices];
//        for (int i = 0; i < vertices; i++) {
//            adj[i] = new ArrayList<Integer>();
//        }
//        adj[0].add(1); adj[0].add(3); adj[0].add(5);
//        adj[1].add(7); adj[1].add(8);
//
//        ArrayList<Integer> order = toposort(adj); // 7 8 6 4 2 1 5 3

//        int b = 10;
    }

    public static void expect(int actual, int expect, String messgae) {
        if (actual != expect) {
            throw new Error("Error: " + messgae);
        }
    }
}

