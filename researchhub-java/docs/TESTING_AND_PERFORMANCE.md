# ResearchHub — Testing and Performance Evidence

## Automated regression testing

The project includes a dependency-free Java regression suite at `tests/ResearchHubTest.java`.
It covers all six DSA modules and checks both normal and negative cases where applicable.

Run it with:

```bash
./run_tests.sh
```

Expected summary:

```text
Passed: 28
Failed: 0
ALL TESTS PASSED
```

### Coverage

| Module | Checks | Examples |
|---|---:|---|
| M1 String Algorithms | 6 | Naive, KMP, Rabin-Karp, Z-function, missing pattern, LCS |
| M2 Suffix Structures | 4 | Suffix-array ordering, LCP, hit, miss |
| M3 Dynamic Programming | 4 | LCS, edit distance, alias, 0/1 knapsack |
| M4 Network Flow | 4 | Ford-Fulkerson, Edmonds-Karp, Dinic, bipartite matching |
| M5 NP Algorithms | 4 | Subset sum, vertex cover, Hamiltonian cycle, CNF |
| M6 Randomized/Parallel | 6 | Randomized quicksort, reservoir sampling, Miller-Rabin, scan, reduce |
| **Total** | **28** | **All passed** |

## Performance evaluation

`benchmarks/PerformanceBenchmark.java` measures the implemented algorithms using `System.nanoTime()`.
It performs two warm-up runs followed by seven timed repetitions and reports the median time.
This reduces the impact of one unusually slow or fast run while remaining dependency-free.

Run it with:

```bash
rm -rf out
mkdir out
javac -d out src/research/*.java benchmarks/PerformanceBenchmark.java
java -cp out research.PerformanceBenchmark
```

The generated measurements are stored in `results/performance_raw.txt` in this submission.

### Measured results from this submission environment

| Algorithm | Input size | Median time (ms) | Complexity |
|---|---:|---:|---|
| KMP | 5000 | 0.069 | O(n+m) |
| Rabin-Karp | 5000 | 0.028 | average O(n+m) |
| Z-Function | 5000 | 0.812 | O(n+m) |
| LCS | 400 | 1.709 | O(nm) |
| Edit Distance | 400 | 2.666 | O(nm), O(m) space |
| 0/1 Knapsack | 200 | 0.371 | O(nC) |
| Suffix Array | 100 | 0.205 | current implementation approx. O(n² log n) |
| Suffix Array | 250 | 0.312 | current implementation approx. O(n² log n) |
| Suffix Array | 500 | 0.920 | current implementation approx. O(n² log n) |
| LCP Array | 100 | 0.368 | current implementation O(n²) worst case |
| LCP Array | 250 | 0.584 | current implementation O(n²) worst case |
| LCP Array | 500 | 0.744 | current implementation O(n²) worst case |
| Edmonds-Karp | 20 | 0.051 | O(VE²) |
| Edmonds-Karp | 40 | 0.074 | O(VE²) |
| Edmonds-Karp | 80 | 0.143 | O(VE²) |
| Dinic | 20 | 0.077 | O(V²E) general |
| Dinic | 40 | 0.078 | O(V²E) general |
| Dinic | 80 | 0.175 | O(V²E) general |
| Randomized QuickSort | 10000 | 0.862 | expected O(n log n) |
| Randomized QuickSort | 50000 | 1.678 | expected O(n log n) |
| Randomized QuickSort | 100000 | 3.470 | expected O(n log n) |
| Blelloch Scan | 16384 | 0.100 | O(n) work, O(log n) span |
| Blelloch Scan | 65536 | 0.185 | O(n) work, O(log n) span |
| Blelloch Scan | 131072 | 0.486 | O(n) work, O(log n) span |
| Parallel Reduce | 10000 | 0.218 | O(n) work, O(log n) span |
| Parallel Reduce | 50000 | 0.244 | O(n) work, O(log n) span |
| Parallel Reduce | 100000 | 0.330 | O(n) work, O(log n) span |

**Interpretation:** the measurements show the expected growth trends for the tested implementations. Absolute timings depend on CPU, JVM, operating-system load and Java version, so the table is evidence for this run rather than a universal hardware benchmark.

## Functional demonstration

The main application supports:

- paper submission;
- similarity analysis;
- novelty analysis;
- similar-paper ranking;
- explainable screening report;
- individual M1–M6 demonstrations;
- complete screening demo;
- all-module demo.

The complete screening demo produces a similarity score, novelty score, ranked matches, new keywords/concepts, a decision and an insertion status.
