import java.util.*;

public class Toposort {

    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        int used[] = new int[adj.length];
        ArrayList<Integer> order = new ArrayList<Integer>();
        //write your code here
        return order;
    }

    private static void dfs(ArrayList<Integer>[] adj, int[] used, ArrayList<Integer> order, int start) {
      //write your code here
        for (int i = 0; i < adj.length; i++) {
            ArrayList<Integer> vertices = adj[i];

        }

        ArrayList<Integer> vertices = adj[start];

        // mark visit
        used[start] = 1;

        for (int i = 0; i < vertices.size() - 1; i++) {
            int next = vertices.get(i + 1);
            if (used[next] != 0) {
                dfs(adj, used, order, start);
            }
        }
    }

    public static void main(String[] args) {
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
}

