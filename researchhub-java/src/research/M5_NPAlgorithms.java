package research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class M5_NPAlgorithms {
    private M5_NPAlgorithms() {}

    public static boolean subsetSum(int[] values, int target) {
        boolean[] possible = new boolean[target + 1];
        possible[0] = true;
        for (int value : values) {
            for (int sum = target; sum >= value; sum--) possible[sum] |= possible[sum - value];
        }
        return possible[target];
    }

    public static int vertexCoverSize(int[][] graph) {
        int n = graph.length;
        for (int size = 0; size <= n; size++) {
            for (int mask = 0; mask < (1 << n); mask++) {
                if (Integer.bitCount(mask) != size) continue;
                boolean covers = true;
                for (int left = 0; left < n && covers; left++) {
                    for (int right : graph[left]) {
                        if ((mask & (1 << left)) == 0 && (mask & (1 << right)) == 0) covers = false;
                    }
                }
                if (covers) return size;
            }
        }
        return n;
    }

    public static boolean hamiltonianCycle(int[][] graph) {
        if (graph.length == 0) return false;
        boolean[] used = new boolean[graph.length];
        int[] path = new int[graph.length];
        path[0] = 0; used[0] = true;
        return hamiltonianDfs(graph, path, used, 1);
    }

    private static boolean hamiltonianDfs(int[][] graph, int[] path, boolean[] used, int position) {
        if (position == graph.length) return graph[path[position - 1]][path[0]] == 1;
        for (int candidate = 1; candidate < graph.length; candidate++) {
            if (!used[candidate] && graph[path[position - 1]][candidate] == 1) {
                used[candidate] = true; path[position] = candidate;
                if (hamiltonianDfs(graph, path, used, position + 1)) return true;
                used[candidate] = false;
            }
        }
        return false;
    }

    public static String cnfExample() {
        return "(x1 OR NOT x2) AND (x2 OR x3) AND (NOT x1 OR x3)";
    }

    public static void run() {
        int[][] graph = {{1, 2}, {0, 2}, {0, 1, 3}, {2}};
        int[][] cycle = {{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}};
        System.out.println("\nM5 - NP COMPLETENESS");
        System.out.println("0/1 knapsack value: " + M3_DPAlgorithms.knapsack01(
                new int[]{2, 3, 4}, new int[]{5, 7, 9}, 5));
        System.out.println("Subset sum [3, 7, 10] target 10: " + subsetSum(new int[]{3, 7, 10}, 10));
        System.out.println("Minimum vertex cover size: " + vertexCoverSize(graph));
        System.out.println("Hamiltonian cycle exists: " + hamiltonianCycle(cycle));
        System.out.println("SAT/CNF representation: " + cnfExample());
    }
}