package research;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.IntFunction;
import java.util.function.LongSupplier;

/** Dependency-free benchmark used to document measured algorithm performance. */
public final class PerformanceBenchmark {
    private static final int WARMUP = 2;
    private static final int REPEATS = 7;

    private PerformanceBenchmark() {}

    private static long median(long[] values) {
        long[] copy = Arrays.copyOf(values, values.length);
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }

    private static long measure(LongSupplier operation) {
        for (int i = 0; i < WARMUP; i++) operation.getAsLong();
        long[] samples = new long[REPEATS];
        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            operation.getAsLong();
            samples[i] = System.nanoTime() - start;
        }
        return median(samples);
    }

    private static String row(String algorithm, int inputSize, long nanos, String complexity) {
        return String.format(Locale.US, "| %s | %d | %.3f | %s |", algorithm, inputSize,
                nanos / 1_000_000.0, complexity);
    }

    private static String repeatedText(int n) {
        String unit = "research screening algorithm analytics ";
        StringBuilder b = new StringBuilder(n);
        while (b.length() < n) b.append(unit);
        return b.substring(0, n);
    }

    private static void benchmarkSearchAlgorithms() {
        System.out.println(row("KMP", 5000, measure(() -> M1_StringAlgorithms.kmpSearch(repeatedText(5000), "screening")), "O(n+m)"));
        System.out.println(row("Rabin-Karp", 5000, measure(() -> M1_StringAlgorithms.rabinKarpSearch(repeatedText(5000), "screening")), "avg O(n+m)"));
        System.out.println(row("Z-Function", 5000, measure(() -> M1_StringAlgorithms.zFunctionSearch(repeatedText(5000), "screening")), "O(n+m)"));
        System.out.println(row("LCS", 400, measure(() -> M1_StringAlgorithms.lcsLength(repeatedText(400), repeatedText(400))), "O(nm)"));
    }

    private static void benchmarkDP() {
        System.out.println(row("Edit Distance", 400, measure(() -> M3_DPAlgorithms.wagnerFischer(repeatedText(400), repeatedText(400))), "O(nm), O(m) space"));
        int[] weights = new int[200];
        int[] values = new int[200];
        for (int i = 0; i < 200; i++) { weights[i] = (i % 10) + 1; values[i] = (i % 17) + 2; }
        System.out.println(row("0/1 Knapsack", 200, measure(() -> M3_DPAlgorithms.knapsack01(weights, values, 500)), "O(nC)"));
    }

    private static void benchmarkSuffix() {
        for (int n : new int[]{100, 250, 500}) {
            final int size = n;
            String text = repeatedText(size);
            long suffixTime = measure(() -> { M2_SuffixStructures.suffixArray(text); return 0L; });
            long lcpTime = measure(() -> {
                int[] sa = M2_SuffixStructures.suffixArray(text);
                return M2_SuffixStructures.lcpArray(text, sa).length;
            });
            System.out.println(row("Suffix Array", size, suffixTime, "current impl approx O(n^2 log n)"));
            System.out.println(row("LCP Array", size, lcpTime, "current impl O(n^2) worst case"));
        }
    }

    private static void benchmarkFlow() {
        for (int n : new int[]{20, 40, 80}) {
            int[][] graph = new int[n][n];
            for (int i = 0; i < n - 1; i++) graph[i][i + 1] = 10;
            graph[0][n - 1] = 5;
            final int source = 0, sink = n - 1;
            System.out.println(row("Edmonds-Karp", n, measure(() -> M4_FlowAlgorithms.edmondsKarp(graph, source, sink)), "O(VE^2)"));
            System.out.println(row("Dinic", n, measure(() -> M4_FlowAlgorithms.dinic(graph, source, sink)), "O(V^2E) general"));
        }
    }

    private static void benchmarkRandomParallel() {
        for (int n : new int[]{10000, 50000, 100000}) {
            int[] input = new int[n];
            for (int i = 0; i < n; i++) input[i] = n - i;
            int[] scanInput = Arrays.copyOf(input, input.length);
            System.out.println(row("Randomized QuickSort", n, measure(() -> { M6_RandomizedParallelAlgorithms.randomizedQuickSort(Arrays.copyOf(input, input.length)); return 0L; }), "expected O(n log n)"));
            // Blelloch scan uses the next power-of-two internally; keep sizes power-of-two for stable measurement.
            int scanSize = Integer.highestOneBit(n);
            if (scanSize < n) scanSize <<= 1;
            int[] scan = new int[scanSize];
            Arrays.fill(scan, 1);
            System.out.println(row("Blelloch Scan", scanSize, measure(() -> { M6_RandomizedParallelAlgorithms.blellochScan(scan); return 0L; }), "O(n) work, O(log n) span"));
            System.out.println(row("Parallel Reduce", n, measure(() -> M6_RandomizedParallelAlgorithms.parallelReduce(input)), "O(n) work, O(log n) span"));
        }
    }

    public static void main(String[] args) {
        System.out.println("# ResearchHub Performance Evaluation");
        System.out.println("Median of " + REPEATS + " timed repetitions after " + WARMUP + " warm-up repetitions.");
        System.out.println("Times are environment-dependent and should be treated as comparative evidence, not universal benchmarks.\n");
        System.out.println("| Algorithm | Input Size | Median Time (ms) | Complexity |");
        System.out.println("|---|---:|---:|---|");
        benchmarkSearchAlgorithms();
        benchmarkDP();
        benchmarkSuffix();
        benchmarkFlow();
        benchmarkRandomParallel();
    }
}
