package research;

public final class M1_StringAlgorithms {
    private M1_StringAlgorithms() {}

    public static int naiveSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int j = 0;
            while (j < pattern.length() && text.charAt(i + j) == pattern.charAt(j)) j++;
            if (j == pattern.length()) return i;
        }
        return -1;
    }

    public static int kmpSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] prefix = prefixTable(pattern);
        int textIndex = 0, patternIndex = 0;
        while (textIndex < text.length()) {
            if (text.charAt(textIndex) == pattern.charAt(patternIndex)) {
                textIndex++; patternIndex++;
                if (patternIndex == pattern.length()) return textIndex - patternIndex;
            } else if (patternIndex > 0) {
                patternIndex = prefix[patternIndex - 1];
            } else {
                textIndex++;
            }
        }
        return -1;
    }

    private static int[] prefixTable(String pattern) {
        int[] prefix = new int[pattern.length()];
        for (int i = 1, length = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(length)) prefix[i++] = ++length;
            else if (length > 0) length = prefix[length - 1];
            else prefix[i++] = 0;
        }
        return prefix;
    }

    public static int rabinKarpSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        if (pattern.length() > text.length()) return -1;
        final long prime = 1_000_000_007L;
        long patternHash = 0, windowHash = 0, power = 1;
        for (int i = 0; i < pattern.length(); i++) {
            patternHash = (patternHash * 257 + pattern.charAt(i)) % prime;
            windowHash = (windowHash * 257 + text.charAt(i)) % prime;
            if (i < pattern.length() - 1) power = power * 257 % prime;
        }
        for (int start = 0; start <= text.length() - pattern.length(); start++) {
            if (patternHash == windowHash && text.regionMatches(start, pattern, 0, pattern.length())) {
                return start;
            }
            if (start < text.length() - pattern.length()) {
                windowHash = (windowHash - text.charAt(start) * power % prime + prime) % prime;
                windowHash = (windowHash * 257 + text.charAt(start + pattern.length())) % prime;
            }
        }
        return -1;
    }

    /**
     * Z-function based pattern search. The Z value at an index is the length
     * of the longest substring starting there that matches the full prefix.
     * We search pattern + separator + text, so a Z value equal to the pattern
     * length identifies an exact occurrence in the text.
     */
    public static int zFunctionSearch(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        if (pattern.length() > text.length()) return -1;

        String combined = pattern + '\0' + text;
        int[] z = buildZArray(combined);
        for (int i = pattern.length() + 1; i < combined.length(); i++) {
            if (z[i] >= pattern.length()) {
                return i - pattern.length() - 1;
            }
        }
        return -1;
    }

    private static int[] buildZArray(String value) {
        int[] z = new int[value.length()];
        int left = 0, right = 0;
        for (int i = 1; i < value.length(); i++) {
            if (i <= right) z[i] = Math.min(right - i + 1, z[i - left]);
            while (i + z[i] < value.length()
                    && value.charAt(z[i]) == value.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] - 1 > right) {
                left = i;
                right = i + z[i] - 1;
            }
        }
        return z;
    }

    public static int lcsLength(String first, String second) {
        int[][] dp = new int[first.length() + 1][second.length() + 1];
        for (int i = 1; i <= first.length(); i++) {
            for (int j = 1; j <= second.length(); j++) {
                dp[i][j] = first.charAt(i - 1) == second.charAt(j - 1)
                        ? dp[i - 1][j - 1] + 1
                        : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[first.length()][second.length()];
    }

    public static void run() {
        String text = "research paper screening detects similar concepts";
        String pattern = "screening";
        System.out.println("\nM1 - STRING ALGORITHMS");
        System.out.println("Text: " + text);
        System.out.println("Pattern: " + pattern);
        System.out.println("Naive index: " + naiveSearch(text, pattern));
        System.out.println("KMP index: " + kmpSearch(text, pattern));
        System.out.println("Rabin-Karp index: " + rabinKarpSearch(text, pattern));
        System.out.println("Z-Function index: " + zFunctionSearch(text, pattern));
        System.out.println("LCS length (research, researcher): " + lcsLength("research", "researcher"));
    }
}
