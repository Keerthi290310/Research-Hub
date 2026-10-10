# ResearchHub — DSA-3 Mapping

| DSA module | Implemented techniques | ResearchHub role |
|---|---|---|
| M1 String Algorithms | Naive, KMP, Rabin-Karp, Z-function, LCS | Exact phrase matching and shared-text comparison |
| M2 Advanced String Structures | Suffix Array, LCP, suffix-array search | Indexing and fast lookup over research text |
| M3 Dynamic Programming | LCS, Wagner-Fischer Edit Distance, 0/1 Knapsack | Similarity, approximate term matching and bounded resource selection |
| M4 Network Flow | Ford-Fulkerson, Edmonds-Karp, Dinic, bipartite matching, König analysis | Flow/matching examples connected to research assignment/relationship analysis |
| M5 NP/Approximation | Subset Sum, minimum Vertex Cover, Hamiltonian Cycle, CNF/SAT representation | Demonstrates hard-combinatorial decision problems relevant to research optimization |
| M6 Randomized/Parallel | Randomized QuickSort, Reservoir Sampling, Miller-Rabin, Blelloch Scan, parallel reduction, Brent theorem | Randomized processing and parallel aggregation concepts |

## System pipeline

```text
New Paper
   |
   v
Normalize + Tokenize
   |
   +--> String matching / DP comparison
   |
   +--> Suffix indexing and lookup
   |
   v
Similarity Ranking
   |
   +--> New keywords / concepts
   |
   v
Novelty + Contribution Check
   |
   v
Explainable Screening Decision
   |
   +--> Accepted -> add to ResearchHub
   +--> Rejected -> keep out of accepted corpus
```

The project is therefore an integrated research-paper screening application, not a collection of unrelated DSA programs.
