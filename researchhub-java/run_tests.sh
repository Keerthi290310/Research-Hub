#!/usr/bin/env bash
set -euo pipefail
rm -rf out
mkdir -p out
javac -d out src/research/*.java tests/ResearchHubTest.java benchmarks/PerformanceBenchmark.java
java -cp out research.ResearchHubTest
