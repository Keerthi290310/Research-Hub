package research;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public final class M4_FlowAlgorithms {
    private M4_FlowAlgorithms() {}

    public static int fordFulkerson(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = copy(capacity);
        int flow = 0;
        int[] parent;
        while ((parent = findPathDfs(residual, source, sink)) != null) {
            int path = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) path = Math.min(path, residual[parent[v]][v]);
            for (int v = sink; v != source; v = parent[v]) {
                residual[parent[v]][v] -= path;
                residual[v][parent[v]] += path;
            }
            flow += path;
        }
        return flow;
    }

    public static int edmondsKarp(int[][] capacity, int source, int sink) {
        int[][] residual = copy(capacity);
        int flow = 0;
        int[] parent;
        while ((parent = findPathBfs(residual, source, sink)) != null) {
            int path = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) path = Math.min(path, residual[parent[v]][v]);
            for (int v = sink; v != source; v = parent[v]) {
                residual[parent[v]][v] -= path;
                residual[v][parent[v]] += path;
            }
            flow += path;
        }
        return flow;
    }

    public static int dinic(int[][] capacity, int source, int sink) {
        int n = capacity.length, flow = 0;
        int[][] residual = copy(capacity);
        while (true) {
            int[] level = new int[n];
            Arrays.fill(level, -1);
            Queue<Integer> queue = new ArrayDeque<>();
            level[source] = 0; queue.add(source);
            while (!queue.isEmpty()) {
                int node = queue.remove();
                for (int next = 0; next < n; next++) {
                    if (residual[node][next] > 0 && level[next] < 0) {
                        level[next] = level[node] + 1; queue.add(next);
                    }
                }
            }
            if (level[sink] < 0) return flow;
            int[] nextEdge = new int[n];
            int pushed;
            while ((pushed = sendFlow(residual, level, nextEdge, source, sink, Integer.MAX_VALUE)) > 0) {
                flow += pushed;
            }
        }
    }

    public static int[] bipartiteMatching(List<List<Integer>> edges, int rightCount) {
        int[] matchedRight = new int[rightCount];
        Arrays.fill(matchedRight, -1);
        for (int left = 0; left < edges.size(); left++) {
            boolean[] visited = new boolean[rightCount];
            matchDfs(left, edges, matchedRight, visited);
        }
        return matchedRight;
    }

    public static String konigAnalysis(List<List<Integer>> edges, int rightCount) {
        int[] matched = bipartiteMatching(edges, rightCount);
        int size = 0;
        for (int value : matched) if (value >= 0) size++;
        return "Maximum matching = " + size
                + "; by König's theorem, minimum vertex cover size = " + size;
    }

    private static boolean matchDfs(int left, List<List<Integer>> edges, int[] matched, boolean[] visited) {
        for (int right : edges.get(left)) {
            if (right < 0 || right >= matched.length || visited[right]) continue;
            visited[right] = true;
            if (matched[right] == -1 || matchDfs(matched[right], edges, matched, visited)) {
                matched[right] = left;
                return true;
            }
        }
        return false;
    }

    private static int sendFlow(int[][] residual, int[] level, int[] nextEdge,
                                int node, int sink, int available) {
        if (node == sink) return available;
        for (; nextEdge[node] < residual.length; nextEdge[node]++) {
            int next = nextEdge[node];
            if (residual[node][next] > 0 && level[next] == level[node] + 1) {
                int pushed = sendFlow(residual, level, nextEdge, next, sink,
                        Math.min(available, residual[node][next]));
                if (pushed > 0) {
                    residual[node][next] -= pushed;
                    residual[next][node] += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    private static int[] findPathBfs(int[][] residual, int source, int sink) {
        int[] parent = new int[residual.length];
        Arrays.fill(parent, -1);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source); parent[source] = source;
        while (!queue.isEmpty()) {
            int node = queue.remove();
            for (int next = 0; next < residual.length; next++) {
                if (residual[node][next] > 0 && parent[next] == -1) {
                    parent[next] = node; queue.add(next);
                }
            }
        }
        return parent[sink] == -1 ? null : parent;
    }

    private static int[] findPathDfs(int[][] residual, int source, int sink) {
        int[] parent = new int[residual.length];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[residual.length];
        if (!dfs(source, sink, residual, visited, parent)) return null;
        return parent;
    }

    private static boolean dfs(int node, int sink, int[][] residual, boolean[] visited, int[] parent) {
        if (node == sink) return true;
        visited[node] = true;
        for (int next = 0; next < residual.length; next++) {
            if (residual[node][next] > 0 && !visited[next]) {
                parent[next] = node;
                if (dfs(next, sink, residual, visited, parent)) return true;
            }
        }
        return false;
    }

    private static int[][] copy(int[][] source) {
        int[][] result = new int[source.length][source.length];
        for (int i = 0; i < source.length; i++) result[i] = Arrays.copyOf(source[i], source[i].length);
        return result;
    }

    public static void run() {
        int[][] graph = {
                {0, 10, 8, 0}, {0, 0, 2, 8}, {0, 0, 0, 10}, {0, 0, 0, 0}
        };
        List<List<Integer>> assignment = new ArrayList<>();
        assignment.add(Arrays.asList(0, 1));
        assignment.add(Arrays.asList(1));
        System.out.println("\nM4 - NETWORK FLOW");
        System.out.println("Ford-Fulkerson max flow: " + fordFulkerson(graph, 0, 3));
        System.out.println("Edmonds-Karp max flow: " + edmondsKarp(graph, 0, 3));
        System.out.println("Dinic max flow: " + dinic(graph, 0, 3));
        System.out.println(konigAnalysis(assignment, 2));
    }
}