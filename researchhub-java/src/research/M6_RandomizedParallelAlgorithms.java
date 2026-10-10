package research;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public final class M6_RandomizedParallelAlgorithms {
    private M6_RandomizedParallelAlgorithms() {}

    public static void randomizedQuickSort(int[] values) {
        randomizedQuickSort(values, 0, values.length - 1, new Random(42));
    }

    private static void randomizedQuickSort(int[] values, int low, int high, Random random) {
        if (low >= high) return;
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(values, pivotIndex, high);
        int split = low;
        for (int i = low; i < high; i++) if (values[i] <= values[high]) swap(values, i, split++);
        swap(values, split, high);
        randomizedQuickSort(values, low, split - 1, random);
        randomizedQuickSort(values, split + 1, high, random);
    }

    public static int[] reservoirSample(int[] stream, int sampleSize, long seed) {
        if (sampleSize < 0 || sampleSize > stream.length) throw new IllegalArgumentException("invalid sample size");
        int[] sample = Arrays.copyOf(stream, sampleSize);
        Random random = new Random(seed);
        for (int i = sampleSize; i < stream.length; i++) {
            int index = random.nextInt(i + 1);
            if (index < sampleSize) sample[index] = stream[i];
        }
        return sample;
    }

    public static boolean millerRabin(long number, int rounds) {
        if (number < 2) return false;
        for (int prime : new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37}) {
            if (number == prime) return true;
            if (number % prime == 0) return false;
        }
        long d = number - 1;
        int powers = 0;
        while ((d & 1) == 0) { d >>= 1; powers++; }
        Random random = new Random(99);
        for (int round = 0; round < rounds; round++) {
            long base = 2 + Math.floorMod(random.nextLong(), number - 3);
            long x = modPow(base, d, number);
            if (x == 1 || x == number - 1) continue;
            boolean witnessPassed = false;
            for (int i = 1; i < powers; i++) {
                x = multiplyMod(x, x, number);
                if (x == number - 1) { witnessPassed = true; break; }
            }
            if (!witnessPassed) return false;
        }
        return true;
    }

    public static int[] blellochScan(int[] input) {
        int size = 1;
        while (size < input.length) size <<= 1;
        int[] tree = new int[size];
        System.arraycopy(input, 0, tree, 0, input.length);
        for (int stride = 1; stride < size; stride <<= 1)
            for (int index = 0; index < size; index += 2 * stride)
                tree[index + 2 * stride - 1] += tree[index + stride - 1];
        tree[size - 1] = 0;
        for (int stride = size >> 1; stride >= 1; stride >>= 1)
            for (int index = 0; index < size; index += 2 * stride) {
                int left = tree[index + stride - 1];
                tree[index + stride - 1] = tree[index + 2 * stride - 1];
                tree[index + 2 * stride - 1] += left;
            }
        return Arrays.copyOf(tree, input.length);
    }

    public static long parallelReduce(int[] values) {
        return ForkJoinPool.commonPool().invoke(new ReduceTask(values, 0, values.length));
    }

    public static String brentTheorem(int work, int processors) {
        if (processors <= 0) throw new IllegalArgumentException("processors must be positive");
        return "T_p <= " + (work / processors + 1) + " + span; work=" + work
                + ", processors=" + processors;
    }

    private static class ReduceTask extends RecursiveTask<Long> {
        private static final long serialVersionUID = 1L;
        private final int[] values; private final int start; private final int end;
        ReduceTask(int[] values, int start, int end) { this.values = values; this.start = start; this.end = end; }
        @Override protected Long compute() {
            if (end - start <= 256) {
                long sum = 0; for (int i = start; i < end; i++) sum += values[i]; return sum;
            }
            int middle = (start + end) >>> 1;
            ReduceTask left = new ReduceTask(values, start, middle);
            ReduceTask right = new ReduceTask(values, middle, end);
            left.fork(); long rightValue = right.compute(); return left.join() + rightValue;
        }
    }

    private static long modPow(long base, long exponent, long modulus) {
        long result = 1 % modulus;
        while (exponent > 0) {
            if ((exponent & 1) == 1) result = multiplyMod(result, base, modulus);
            base = multiplyMod(base, base, modulus); exponent >>= 1;
        }
        return result;
    }

    private static long multiplyMod(long first, long second, long modulus) {
        return BigInteger.valueOf(first).multiply(BigInteger.valueOf(second))
                .mod(BigInteger.valueOf(modulus)).longValue();
    }

    private static void swap(int[] values, int first, int second) {
        int temporary = values[first]; values[first] = values[second]; values[second] = temporary;
    }

    public static void run() {
        int[] values = {9, 2, 7, 1, 5};
        randomizedQuickSort(values);
        System.out.println("\nM6 - RANDOMIZED & PARALLEL ALGORITHMS");
        System.out.println("Randomized quicksort: " + Arrays.toString(values));
        System.out.println("Reservoir sample: " + Arrays.toString(reservoirSample(
                new int[]{1, 2, 3, 4, 5, 6}, 3, 42)));
        System.out.println("Miller-Rabin says 104729 is prime: " + millerRabin(104729, 6));
        System.out.println("Blelloch exclusive scan: " + Arrays.toString(blellochScan(new int[]{1, 2, 3, 4})));
        System.out.println("Parallel reduce: " + parallelReduce(new int[]{1, 2, 3, 4, 5}));
        System.out.println(brentTheorem(1000, 4));
    }
}