
/**
 * Sort using Java's ForkJoinPool.
 */

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPoolSort implements Sorter {
        public final int threads;

        public ForkJoinPoolSort(int threads) {
                this.threads = threads;
        }

        public void sort(int[] arr) {
                ForkJoinPool pool = new ForkJoinPool(threads);
                pool.invoke(new Worker(arr, 0, arr.length - 1));
                pool.shutdown();
        }

        public int getThreads() {
                return threads;
        }

        private static class Worker extends RecursiveAction {
                private int[] arr;
                private int start;
                private int end;
                SequentialSort sorter;

                Worker(int[] arr, int start, int end) {
                        this.arr = arr;
                        this.start = start;
                        this.end = end;
                        sorter = new SequentialSort();
                }

                protected void compute() {
                        // No parallelism if the array is too small
                        if (end - start <= 16) {
                                sorter.sort(arr, start, end);
                        }

                        else {
                                // If this is not the case we are done sorting
                                if (start < end) {
                                        // Get the correct index value of the partition element
                                        int partitionIndex = partition(arr, start, end);

                                        Worker leftWorker = new Worker(arr, start, partitionIndex - 1);
                                        Worker rightWorker = new Worker(arr, partitionIndex + 1, end);

                                        // Start working on the left and right side of the partition
                                        leftWorker.fork();
                                        rightWorker.fork();

                                        // Wait for the left and right side to finish and return the result
                                        leftWorker.join();
                                        rightWorker.join();
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
}
