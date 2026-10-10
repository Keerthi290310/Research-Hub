package research;

import java.util.Arrays;
import java.util.List;

/** Lightweight dependency-free regression tests for all DSA modules. */
public final class ResearchHubTest {
    private static int passed = 0;
    private static int failed = 0;

    private ResearchHubTest() {}

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static void checkEquals(String name, Object expected, Object actual) {
        check(name + " | expected=" + expected + ", actual=" + actual,
                expected == null ? actual == null : expected.equals(actual));
    }

    private static void testM1() {
        String text = "research paper screening detects similar concepts";
        String pattern = "screening";
        checkEquals("M1 Naive", 15, M1_StringAlgorithms.naiveSearch(text, pattern));
        checkEquals("M1 KMP", 15, M1_StringAlgorithms.kmpSearch(text, pattern));
        checkEquals("M1 Rabin-Karp", 15, M1_StringAlgorithms.rabinKarpSearch(text, pattern));
        checkEquals("M1 Z-function", 15, M1_StringAlgorithms.zFunctionSearch(text, pattern));
        checkEquals("M1 missing pattern", -1, M1_StringAlgorithms.kmpSearch(text, "xyz"));
        checkEquals("M1 LCS", 8, M1_StringAlgorithms.lcsLength("research", "researcher"));
    }

    private static void testM2() {
        String text = "analysis analytics algorithm";
        int[] suffix = M2_SuffixStructures.suffixArray(text);
        int[] expected = {18, 8, 19, 2, 11, 0, 9, 16, 21, 26, 15, 6, 24, 20, 3, 12, 27, 1, 10, 22, 23, 17, 7, 5, 25, 14, 4, 13};
        check("M2 suffix array ordering", Arrays.equals(expected, suffix));
        checkEquals("M2 LCP first value", 0, M2_SuffixStructures.lcpArray(text, suffix)[0]);
        check("M2 suffix search hit", M2_SuffixStructures.contains(text, "analytic"));
        check("M2 suffix search miss", !M2_SuffixStructures.contains(text, "database"));
    }

    private static void testM3() {
        checkEquals("M3 LCS", 3, M3_DPAlgorithms.lcsLength("screening", "research"));
        checkEquals("M3 Wagner-Fischer", 1, M3_DPAlgorithms.wagnerFischer("research", "reseach"));
        checkEquals("M3 edit-distance alias", 3, M3_DPAlgorithms.editDistance("kitten", "sitting"));
        checkEquals("M3 0/1 knapsack", 14,
                M3_DPAlgorithms.knapsack01(new int[]{2, 3, 4}, new int[]{6, 8, 9}, 5));
    }

    private static void testM4() {
        int[][] graph = {
                {0, 10, 8, 0}, {0, 0, 2, 8}, {0, 0, 0, 10}, {0, 0, 0, 0}
        };
        checkEquals("M4 Ford-Fulkerson", 18, M4_FlowAlgorithms.fordFulkerson(graph, 0, 3));
        checkEquals("M4 Edmonds-Karp", 18, M4_FlowAlgorithms.edmondsKarp(graph, 0, 3));
        checkEquals("M4 Dinic", 18, M4_FlowAlgorithms.dinic(graph, 0, 3));
        List<List<Integer>> matching = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1));
        int[] matched = M4_FlowAlgorithms.bipartiteMatching(matching, 2);
        check("M4 bipartite matching size 2", Arrays.stream(matched).filter(v -> v >= 0).count() == 2);
    }

    private static void testM5() {
        check("M5 subset sum", M5_NPAlgorithms.subsetSum(new int[]{3, 7, 10}, 10));
        int[][] graph = {{1, 2}, {0, 2}, {0, 1, 3}, {2}};
        checkEquals("M5 minimum vertex cover", 2, M5_NPAlgorithms.vertexCoverSize(graph));
        int[][] cycle = {{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}};
        check("M5 Hamiltonian cycle", M5_NPAlgorithms.hamiltonianCycle(cycle));
        check("M5 CNF generated", M5_NPAlgorithms.cnfExample().contains("AND"));
    }

    private static void testM6() {
        int[] values = {9, 2, 7, 1, 5};
        M6_RandomizedParallelAlgorithms.randomizedQuickSort(values);
        check("M6 randomized quicksort", Arrays.equals(new int[]{1, 2, 5, 7, 9}, values));
        int[] sample = M6_RandomizedParallelAlgorithms.reservoirSample(new int[]{1, 2, 3, 4, 5, 6}, 3, 42);
        checkEquals("M6 reservoir sample deterministic seed", "[6, 2, 4]", Arrays.toString(sample));
        check("M6 Miller-Rabin prime", M6_RandomizedParallelAlgorithms.millerRabin(104729, 6));
        check("M6 Miller-Rabin composite", !M6_RandomizedParallelAlgorithms.millerRabin(104728, 6));
        check("M6 Blelloch scan", Arrays.equals(new int[]{0, 1, 3, 6},
                M6_RandomizedParallelAlgorithms.blellochScan(new int[]{1, 2, 3, 4})));
        checkEquals("M6 parallel reduce", 15L,
                M6_RandomizedParallelAlgorithms.parallelReduce(new int[]{1, 2, 3, 4, 5}));
    }

    public static void main(String[] args) {
        System.out.println("ResearchHub automated regression tests");
        System.out.println("====================================");
        testM1(); testM2(); testM3(); testM4(); testM5(); testM6();
        System.out.println("====================================");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        if (failed != 0) throw new AssertionError("Regression tests failed: " + failed);
        System.out.println("ALL TESTS PASSED");
    }
}
