package jpolanco.springbootapp.unit.utils.benchmark;

import jpolanco.springbootapp.user.infrastructure.components.implementation.Zxing;
import jpolanco.springbootapp.user.infrastructure.components.utils.QRToSVG;

public class QRGeneratorBenchmark {
    private static final int WARMUP_ROUNDS = 5_000;
    private static final int TEST_ROUNDS = 10_000;

    static class QRToSVGTest {
        public static void main(String[] args) {

            System.out.println("Warming up with " + WARMUP_ROUNDS + " rounds...");

            // 🔥 Warmup (ignored)
            for (int i = 0; i < WARMUP_ROUNDS; i++) {
                try {
                    // Simulate QR code generation without validation
                    String svg = QRToSVG.generateSVG("https://example.com", 300, 300);
                } catch (Exception e) {
                    // Ignore exceptions during warmup
                }
            }

            System.out.println("Starting timed benchmark...");

            // ⏱ Real measurement
            long totalTime = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (int i = 0; i < TEST_ROUNDS; i++) {
                long start = System.nanoTime();
                long duration = System.nanoTime() - start;

                totalTime += duration;
                if (duration < min) min = duration;
                if (duration > max) max = duration;
            }

            long average = totalTime / TEST_ROUNDS;

            System.out.println("Benchmark results (load without validation)");
            System.out.println("--------------------------------------------");
            System.out.println("Warmup rounds: " + WARMUP_ROUNDS);
            System.out.println("Test rounds:   " + TEST_ROUNDS);
            System.out.println("Average time:  " + average + " ns");
            System.out.println("Min time:      " + min + " ns");
            System.out.println("Max time:      " + max + " ns");
        }
    }

    static class QRGeneratorTest {
        public static void main(String[] args) {
            System.out.println("Warming up with " + WARMUP_ROUNDS + " rounds...");

//            var zxing = new Zxing("");

            // 🔥 Warmup (ignored)
            for (int i = 0; i < WARMUP_ROUNDS; i++) {
                try {
                    // Simulate QR code generation without validation
//                    zxing.generate("testQR" + i, "https://example.com");
                } catch (Exception e) {
                    // Ignore exceptions during warmup
                }
            }

            System.out.println("Starting timed benchmark...");

            // ⏱ Real measurement
            long totalTime = 0;
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for (int i = 0; i < TEST_ROUNDS; i++) {
                long start = System.nanoTime();
                try {
                    // Simulate QR code generation
//                    zxing.generate("testQR" + i, "https://example.com");
                } catch (Exception e) {
                    // Handle exception if needed
                }
                long duration = System.nanoTime() - start;

                totalTime += duration;
                if (duration < min) min = duration;
                if (duration > max) max = duration;
            }

            long average = totalTime / TEST_ROUNDS;

            System.out.println("Benchmark results (QR generation)");
            System.out.println("----------------------------------");
            System.out.println("Warmup rounds: " + WARMUP_ROUNDS);
            System.out.println("Test rounds:   " + TEST_ROUNDS);
            System.out.println("Average time:  " + average + " ns");
            System.out.println("Min time:      " + min + " ns");
            System.out.println("Max time:      " + max + " ns");
        }
    }
}
