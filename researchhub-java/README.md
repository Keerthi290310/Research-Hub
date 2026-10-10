# ResearchHub – Intelligent Research Paper Screening and Analytics Platform

ResearchHub is a standard-Java console application designed specifically for
**scientific research-paper screening**. It does more than store or search
papers: a new submission is compared with an accepted-paper corpus, similar
papers are ranked, new keywords/concepts are identified, a novelty score is
calculated, and an explainable screening report is produced.

## Problem statement

Researchers and review teams may have to compare a new paper with a growing
collection of existing papers. Simple keyword search does not distinguish
between exact phrase matches, approximate wording, shared concepts, and a
paper's claimed contribution. ResearchHub combines several DSA techniques in
one research-specific screening workflow.

## Existing system vs ResearchHub

| Typical repository/search system | ResearchHub project focus |
| --- | --- |
| Stores and retrieves papers | Screens a new submission before adding it |
| Keyword/title search | Multi-stage text and keyword comparison |
| Returns matching documents | Ranks similar accepted papers |
| Usually gives little explanation for a screening decision | Reports similarity, novelty, new keywords and new concepts |
| Search is the main task | Search algorithms are connected to a research-screening decision workflow |

The project therefore has a domain-specific contribution rather than being a
collection of unrelated algorithm demonstrations.

## Core workflow

1. Submit title, abstract, keywords, author, and contribution description.
2. Normalize and tokenize the research text.
3. Compare the submission with every accepted paper.
4. Use similarity measures to rank the closest papers.
5. Identify keywords and concept-like terms not present in the existing corpus.
6. Calculate a novelty score.
7. Produce an explainable accept/reject report.
8. Add the paper only when it passes the configured screening logic.

## Project structure

```text
src/research/
  ResearchHub.java
  Author.java
  ResearchPaper.java
  Dataset.java
  Researcher.java
  Project.java
  SimilarityResult.java
  ScreeningResult.java
  ResearchScreeningSystem.java
  M1_StringAlgorithms.java
  M2_SuffixStructures.java
  M3_DPAlgorithms.java
  M4_FlowAlgorithms.java
  M5_NPAlgorithms.java
  M6_RandomizedParallelAlgorithms.java

tests/
  ResearchHubTest.java
benchmarks/
  PerformanceBenchmark.java
docs/
  DSA_MAPPING.md
  TESTING_AND_PERFORMANCE.md
  DEMO_RESULTS.md
results/
  performance_raw.txt
```

## Complete project scope

The project contains an integrated ResearchHub workflow plus all six DSA modules used for the rubric. M1–M3 cover string structures and dynamic programming; M4 covers network flow and matching; M5 covers NP/combinatorial decision problems; M6 covers randomized and parallel algorithms.

| Module | Implemented algorithm | ResearchHub-specific use |
| --- | --- | --- |
| M1 | Naive pattern search | Baseline exact phrase matching |
| M1 | KMP | Efficient exact phrase matching in abstracts/titles |
| M1 | Rabin-Karp | Hash-based phrase matching |
| M1 | Z-Function | Linear-time pattern matching in research text |
| M1 | LCS | Shared text subsequence comparison |
| M2 | Suffix Array | Indexing suffixes of research text |
| M2 | LCP Array | Measuring common prefixes among indexed suffixes |
| M2 | Suffix search | Fast lookup over the suffix-array index |
| M3 | LCS | Title/text similarity support |
| M3 | Wagner-Fischer Edit Distance | Approximate matching of research terms/phrases |
| M3 | 0/1 Knapsack | Demonstration of bounded resource/project selection |

### Algorithm selection

ResearchHub selects DSA techniques that directly support paper screening, text comparison, indexing, matching, combinatorial analysis, randomized processing and parallel aggregation. The project is organized around an integrated application workflow rather than unrelated textbook programs.

## Testing and performance evidence

The project now includes executable regression tests and a reproducible benchmark.

Run all automated tests with:

```bash
./run_tests.sh
```

The current verified run reports **28 passed and 0 failed** across M1–M6. Detailed test coverage is documented in `docs/TESTING_AND_PERFORMANCE.md`.

Run the performance benchmark with:

```bash
rm -rf out
mkdir out
javac -d out src/research/*.java benchmarks/PerformanceBenchmark.java
java -cp out research.PerformanceBenchmark
```

Measured results from the current environment are stored in `results/performance_raw.txt` and summarized in `docs/TESTING_AND_PERFORMANCE.md`. The benchmark uses two warm-up runs and seven timed repetitions, reporting the median. Absolute timings are environment-dependent.

### Representative functional test cases

```text
M1: screening -> index 15 using Naive, KMP, Rabin-Karp and Z-Function
M2: suffix array + LCP generated; search for analytic -> true
M3: edit distance(research, reseach) -> 1; knapsack -> 14
M4: Ford-Fulkerson / Edmonds-Karp / Dinic -> max flow 18
M5: subset sum target 10 -> true; minimum vertex cover -> 2
M6: scan [1,2,3,4] -> [0,1,3,6]; parallel reduce -> 15
```

See `docs/DEMO_RESULTS.md` for the verified end-to-end execution summary.

## Individual viva preparation

Each team member should be able to explain:

- why the selected algorithm fits the ResearchHub problem;
- the main logic of the implementation;
- the input and output of each algorithm;
- best/average/worst-case complexity where applicable;
- why KMP differs from Naive Search;
- how Rabin-Karp uses rolling hashes;
- how the Z-function identifies pattern occurrences;
- what a suffix array and LCP array represent;
- how Wagner-Fischer computes edit distance;
- how the DP table/rolling rows reduce repeated work;
- how the algorithm output is connected to the ResearchHub screening workflow.

## Requirements

- JDK 11 or newer
- No external dependencies

## How to run

From this directory:

```bash
javac -d out src/research/*.java
java -cp out research.ResearchHub
```

On Windows, use `;` instead of `:` in the classpath if needed.

The application starts with sample accepted papers. Choose **13** for the
complete screening demo, **7–9** for the individual DSA modules, or **2** to submit
your own paper interactively.

## Verification commands

Build and launch the application:

```bash
./run.sh
```

Run automated regression tests:

```bash
./run_tests.sh
```

The source and tests use only the standard Java library; no external dependencies are required.

## Screening thresholds

The default thresholds are:

- Similarity threshold: 70%
- Novelty threshold: 40%

The decision uses both similarity and novelty together with the contribution
check. Thresholds are configurable through `ResearchScreeningSystem`.

## Unique project contribution

ResearchHub's distinguishing project-level idea is the **research-screening
gate**: DSA algorithms are not presented as isolated textbook programs. Their
outputs feed a domain-specific pipeline that compares a new research paper,
ranks related papers, identifies new keywords/concepts, evaluates novelty and
produces an explainable screening report before repository insertion.
