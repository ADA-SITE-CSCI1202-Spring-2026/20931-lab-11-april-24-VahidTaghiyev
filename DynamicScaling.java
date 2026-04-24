public class DynamicScaling {

    static class MathTask implements Runnable {
        private final int taskId;

        public MathTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            long result = 0;
            for (int i = 0; i < 10_000_000; i++) {
                result += (long)(Math.pow(i, 3) + i * taskId);
            }
            
            if (result == Long.MIN_VALUE) System.out.println("unlikely");
        }
    }

    static long runWithThreads(int threadCount) throws InterruptedException {
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(new MathTask(i));
        }

        long start = System.currentTimeMillis();

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join(); 

        return System.currentTimeMillis() - start;
    }

    public static void main(String[] args) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Logical processors available: " + cores);

        long singleThreadTime = runWithThreads(1);
        System.out.println("Time with 1 thread:    " + singleThreadTime + " ms");

        long multiThreadTime = runWithThreads(cores);
        System.out.println("Time with " + cores + " threads: " + multiThreadTime + " ms");

        double speedup = (double) singleThreadTime / multiThreadTime;
        System.out.printf("Speedup: %.2fx%n", speedup);
    }
}