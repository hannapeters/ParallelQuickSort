import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorServiceSort implements Sorter {
    private final int threads;
    private static ExecutorService pool;

    public ExecutorServiceSort(int threads) {
        this.threads = threads;
    }

    public void sort(int[] arr) {
        AtomicInteger count = new AtomicInteger(1);
        pool = Executors.newFixedThreadPool(threads);
        pool.execute(new QuicksortWorker(arr, 0, arr.length - 1, count, threads));
        // Wait until the sorting is done
        while (count.get() != 0) {
        }
        pool.shutdownNow();
    }

    public int getThreads() {
        return threads;
    }

    private static class QuicksortWorker implements Runnable {
        private final int[] arr;
        private final int start;
        private final int end;
        private final AtomicInteger count;
        private final int threads;
        SequentialSort sorter;

        public QuicksortWorker(int[] arr, int start, int end, AtomicInteger count, int threads) {
            this.arr = arr;
            this.start = start;
            this.end = end;
            // Counter is for knowing how many threads are being used
            this.count = count;
            this.threads = threads;
            sorter = new SequentialSort();
        }

        public void run() {
            quicksort(start, end);
            synchronized (count) {
                count.getAndDecrement();
            }
        }

        private void quicksort(int left, int right) {
            // No parallelism if the array is too small
            if (end - start <= 16) {
                sorter.sort(arr, start, end);
            }

            if (left < right) {
                int pivotIndex = partition(left, right);
                // do not create more workers than threads
                if (count.get() >= threads) {
                    quicksort(left, pivotIndex - 1);
                    quicksort(pivotIndex + 1, right);
                } else {
                    // 2 new threads are being used so 2 is added to the counter
                    count.getAndAdd(2);
                    QuicksortWorker leftWorker = new QuicksortWorker(arr, left, pivotIndex - 1, count, threads);
                    QuicksortWorker rightWorker = new QuicksortWorker(arr, pivotIndex + 1, right, count, threads);
                    pool.execute(leftWorker);
                    pool.execute(rightWorker);
                }
            }
        }

        // function to sort the partitioned array
        public int partition(int start, int end) {
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