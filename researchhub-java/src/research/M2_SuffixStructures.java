package research;

import java.util.Arrays;

public final class M2_SuffixStructures {
    private M2_SuffixStructures() {}

    public static int[] suffixArray(String text) {
        Integer[] suffixes = new Integer[text.length()];
        for (int i = 0; i < text.length(); i++) suffixes[i] = i;
        Arrays.sort(suffixes, (a, b) -> text.substring(a).compareTo(text.substring(b)));
        return Arrays.stream(suffixes).mapToInt(Integer::intValue).toArray();
    }

    public static int[] lcpArray(String text, int[] suffixArray) {
        int[] lcp = new int[suffixArray.length];
        for (int i = 1; i < suffixArray.length; i++) {
            int first = suffixArray[i - 1], second = suffixArray[i];
            while (first + lcp[i] < text.length() && second + lcp[i] < text.length()
                    && text.charAt(first + lcp[i]) == text.charAt(second + lcp[i])) lcp[i]++;
        }
        return lcp;
    }

    public static boolean contains(String text, String pattern) {
        int[] suffixes = suffixArray(text);
        int low = 0, high = suffixes.length - 1;
        while (low <= high) {
            int middle = (low + high) >>> 1;
            String suffix = text.substring(suffixes[middle]);
            int comparison = suffix.compareTo(pattern);
            if (suffix.startsWith(pattern)) return true;
            if (comparison < 0) low = middle + 1;
            else high = middle - 1;
        }
        return false;
    }

    public static void run() {
        String text = "analysis analytics algorithm";
        int[] suffixes = suffixArray(text);
        System.out.println("\nM2 - SUFFIX STRUCTURES");
        System.out.println("Text: " + text);
        System.out.println("Suffix array: " + Arrays.toString(suffixes));
        System.out.println("LCP array: " + Arrays.toString(lcpArray(text, suffixes)));
        System.out.println("Search 'analytic': " + contains(text, "analytic"));
    }
}