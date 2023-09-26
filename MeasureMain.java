public class MeasureMain {

        public static void main(String[] args) {
                // SequentialSort sorterTest = new SequentialSort();
                // int[] arr= {3,2,5,6,2,1,7,5,2,7};
                // sorterTest.sort(arr);
                // for(int i = 0; i < arr.length; i++){
                // System.out.println(arr[i]);
                // }

                // Sorter name
                String name = args[0];
                // Number of threads.
                int threads = Integer.parseInt(args[1]);
                // Number of values.
                int arrSize = Integer.parseInt(args[2]);
                // Number of warm-up rounds.
                int warmUps = Integer.parseInt(args[3]);
                // Number of measurement rounds.
                int measurements = Integer.parseInt(args[4]);
                // Seed for RNG
                int seed = Integer.parseInt(args[5]);

                // Sorting algorithm.
                Sorter sorter = getSorter(name, threads);

                if (sorter == null) {
                        System.err.printf("ERROR: Unknown sorter %s.\n", args[0]);
                        System.exit(1);
                }

                // Print information to stderr
                System.err.printf("Sorting algorithm:  %s\n", name);
                System.err.printf("Thread count:       %d\n", sorter.getThreads());
                System.err.printf("Array size:         %d\n", arrSize);
                System.err.printf("Warm-up rounds:     %d\n", warmUps);
                System.err.printf("Measurement rounds: %d\n", measurements);
                System.err.printf("RNG seed:           %d\n", seed);

                // Warm-up but also check correctness.
                System.err.println("Validating sorter");
                if (!Auxiliary.validate(sorter, arrSize, seed, warmUps)) {
                        System.err.printf("ERROR: Sorting error.\n");
                        System.exit(2);
                }
                System.err.println("Validation passed");

                System.err.println("Starting Measurements");

                double[] resultMeasurements = Auxiliary.measure(sorter, arrSize, seed, measurements);

                // Prints average execution time and standard deviation to stdout.
                System.out.printf("%s %d %.2f %.2f\n", name, sorter.getThreads(), resultMeasurements[0],
                                resultMeasurements[1]);

                System.err.println("Measurements done");
        }

        private static Sorter getSorter(String name, int threads) {
                switch (name) {
                        case "Sequential":
                                return new SequentialSort();
                        case "ExecutorService":
                                return new ExecutorServiceSort(threads);
                        case "ForkJoinPool":
                                return new ForkJoinPoolSort(threads);
                        case "ParallelStream":
                                return new ParallelStreamSort(threads);
                        case "ThreadSort":
                                return new ThreadSort(threads);
                        default:
                                return null;
                }
        }
}
