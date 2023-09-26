
/**
 * Sort using Java's Thread, Runnable, start(), and join().
 */

public class ThreadSort implements Sorter {
        public final int threads;

        public ThreadSort(int threads) {
                this.threads = threads;
        }

        //initialize threads and starts the first thread
        public void sort(int[] arr) {
                Thread thread = new Thread(new Worker(arr, 0, arr.length - 1));
                thread.start();
                try {
                        thread.join();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        public int getThreads() {
                return threads;
        }

        //the worker that the each thread runs, itialialized a sequentialsorter that each
        //thread uses to sort their subarray
        private class Worker implements Runnable {
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

                public void run() {
                        // No parallelism if the array is too small
                        if (end - start <= 16) {
                                sorter.sort(arr, start, end);
                        }

                        else {
                                // If this is not the case we are done sorting
                                if (start < end) {
                                        // Get the correct index value of the partition element
                                        int partitionIndex = partition(arr, start, end);
                                        //makes sure the number of threads are not exceeded
                                        if (Thread.activeCount() < threads) {

                                                Thread leftWorker = new Thread(
                                                                new Worker(arr, start, partitionIndex - 1));
                                                Thread rightWorker = new Thread(
                                                                new Worker(arr, partitionIndex + 1, end));
                                                // Start thread on the left subarray and another on right subarray
                                                leftWorker.start();
                                                rightWorker.start();
                                                // Wait for the left and right side to finish and join
                                                try {
                                                        leftWorker.join();
                                                        rightWorker.join();

                                                } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                }
                                        } else {

                                                sorter.sort(arr, start, partitionIndex - 1);
                                                sorter.sort(arr, partitionIndex + 1, end);

                                        }

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
