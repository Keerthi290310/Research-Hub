# ResearchHub — Verified Demo Results

The project was compiled and executed from a clean build directory.

Command:

```bash
javac -Xlint:all -d out src/research/*.java
java -cp out research.ResearchHub
```

The `Run All DSA Modules` option produced successful outputs for M1–M6, including:

- M1: all four exact-search algorithms returned index 15 for `screening`.
- M2: suffix array and LCP array were generated and suffix search returned `true` for `analytic`.
- M3: edit distance for `research` / `reseach` returned 1 and knapsack returned 14.
- M4: Ford-Fulkerson, Edmonds-Karp and Dinic all returned max flow 18.
- M5: subset sum returned `true`, minimum vertex cover returned 2, and Hamiltonian cycle returned `true`.
- M6: randomized quicksort sorted the sample, Miller-Rabin identified 104729 as prime, Blelloch scan returned `[0, 1, 3, 6]`, and parallel reduction returned 15.

The complete screening demo also produces the ResearchHub-specific similarity/novelty report and explains the accept/reject decision.
