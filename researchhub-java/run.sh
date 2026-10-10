#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"
rm -rf out
mkdir -p out
javac -Xlint:all -d out src/research/*.java
exec java -cp out research.ResearchHub