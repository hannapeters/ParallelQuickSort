import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Sort using Java's ParallelStreams and Lambda functions.
 *
 * Hints:
 * - Do not take advice from StackOverflow.
 * - Think outside the box.
 * - Stream of threads?
 * - Stream of function invocations?
 *
 * By default, the number of threads in parallel stream is limited by the
 * number of cores in the system. You can limit the number of threads used by
 * parallel streams by wrapping it in a ForkJoinPool.
 * ForkJoinPool myPool = new ForkJoinPool(threads);
 * myPool.submit(() -> "my parallel stream method / function");
 */

public class ParallelStreamSort implements Sorter {
        public final int threads;

        public ParallelStreamSort(int threads) {
                this.threads = threads;
        }
        //ForkjoinPool is used to create a pool of threads
        //then submit is called to start a thread and execute parallelsort
        public void sort(int[] arr) {
                ForkJoinPool pool = new ForkJoinPool(threads);
                pool.submit(() -> parallelSort((arr), 0, arr.length - 1)).join();
                pool.shutdown();
        }

        public int getThreads() {
                return threads;
        }

        
        public void parallelSort(int[] arr, int start, int end) {
                SequentialSort sorter = new SequentialSort();
                // No parallelism if the array is too small
                if (end - start <= 16) {
                        sorter.sort(arr, start, end);
                } else {

                        // If this is not the case we are done sorting
                        if (start < end) {
                                // Get the correct index value of the partition element
                                int partitionIndex = partition(arr, start, end);

                                // create parallel streams to sort left and right subarray
                                IntStream.range(0, 2).parallel().forEach(i -> {
                                        if (i == 0) {
                                                parallelSort(arr, start, partitionIndex - 1);
                                        } else {
                                                parallelSort(arr, partitionIndex + 1, end);
                                        }
                                });
                        }
                }

        }

        // function to sort the partitioned array
        public int partition(int[] arr, int start, int end) {
                // make last element pivot
                int pivot = arr[end];
                int i = start - 1;
                // iterate over elements and swap if necessary
                for (int j = start; j <= end; j++) {
                        if (arr[j] < pivot) {
                                i++;
                                int temp = arr[i];
                                arr[i] = arr[j];
                                arr[j] = temp;
                        }
                }
                // place pivot in right place
                int temp = arr[i + 1];
                arr[i + 1] = arr[end];
                arr[end] = temp;
                // return index of pivot element
                return i + 1;
        }

}
