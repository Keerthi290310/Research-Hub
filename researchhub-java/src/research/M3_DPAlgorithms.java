package research;

import java.util.Arrays;

public final class M3_DPAlgorithms {
    private M3_DPAlgorithms() {}

    public static int lcsLength(String first, String second) {
        return M1_StringAlgorithms.lcsLength(first, second);
    }

    /**
     * Wagner-Fischer edit distance with rolling rows.
     * The recurrence is unchanged from the classic Wagner-Fischer algorithm;
     * only the space usage is reduced from O(nm) to O(m).
     */
    public static int wagnerFischer(String first, String second) {
        int[] previous = new int[second.length() + 1];
        for (int j = 0; j <= second.length(); j++) previous[j] = j;
        for (int i = 1; i <= first.length(); i++) {
            int[] current = new int[second.length() + 1];
            current[0] = i;
            for (int j = 1; j <= second.length(); j++) {
                current[j] = first.charAt(i - 1) == second.charAt(j - 1)
                        ? previous[j - 1]
                        : 1 + Math.min(previous[j - 1], Math.min(previous[j], current[j - 1]));
            }
            previous = current;
        }
        return previous[second.length()];
    }

    // Backward-compatible name used by the rest of the project.
    public static int editDistance(String first, String second) {
        return wagnerFischer(first, second);
    }

    public static int knapsack01(int[] weights, int[] values, int capacity) {
        int[] dp = new int[capacity + 1];
        for (int item = 0; item < weights.length; item++) {
            for (int limit = capacity; limit >= weights[item]; limit--) {
                dp[limit] = Math.max(dp[limit], dp[limit - weights[item]] + values[item]);
            }
        }
        return dp[capacity];
    }

    public static void run() {
        System.out.println("\nM3 - DYNAMIC PROGRAMMING");
        System.out.println("LCS length: " + lcsLength("screening", "research"));
        System.out.println("Wagner-Fischer edit distance (research, reseach): "
                + wagnerFischer("research", "reseach"));
        System.out.println("Best resource value: " + knapsack01(
                new int[]{2, 3, 4}, new int[]{6, 8, 9}, 5));
        System.out.println("Reference weights: " + Arrays.toString(new int[]{2, 3, 4}));
    }
}
