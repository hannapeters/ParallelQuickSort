/**
 * Helper methods.
 */

import java.util.Arrays;
import java.util.Random;

public class Auxiliary {
        /**
         * Generate a pseudo-random array of length `n`.
         */
        public static int[] arrayGenerate(int seed, int n) {
                Random prng = new Random(seed);
                int[] arr = new int[n];
                for (int i = 0; i < n; ++i)
                        arr[i] = prng.nextInt();
                return arr;
        }

        /**
         * Measures the execution time of the 'sorter'.
         * @param sorter Sorting algorithm
         * @param n Size of list to sort
         * @param initSeed Initial seed used for array generation
         * @param m Measurment rounds.
         * @return result[0]: average execution time
         *         result[1]: standard deviation of execution time
         */
        public static double[] measure(Sorter sorter, int n, int initSeed, int m) {
                
                double[] result = new double[2];
                //make a new random array using a seed
                long time[] = new long[m];
                int[] generatedArray=new int[m];
                for(int i = 0; i < m; i++)
                {
                        generatedArray = arrayGenerate(initSeed, n);
                        long start = System.nanoTime();
                        sorter.sort(generatedArray);
                        long end = System.nanoTime();
                        time[i] = (end - start);
                }
                double total = 0;
                for(int i = 0; i < m; i++)
                {
                        total += time[i];
                }
                result[0] = (total / m);
                result[1] = standardDeviation(generatedArray);
                return result;
        }

        /**
         * Checks that the 'sorter' sorts.
         * @param sorter Sorting algorithm
         * @param n Size of list to sort
         * @param initSeed Initial seed used for array generation
         * @param m Number of attempts.
         * @return True if the sorter successfully sorted all generated arrays.
         */
        public static boolean validate(Sorter sorter, int n, int initSeed, int m) {
                for(int i = 0; i < m; i++)
                {
                        int[] generatedArray = arrayGenerate(initSeed, n);
                        sorter.sort(generatedArray);
                        for(int j = 0; j < generatedArray.length - 1; j++)
                        {
                                if(generatedArray[j] > generatedArray[j + 1])
                                {
                                        return false;
                                }
                        }
                }
                return true;
        }

        public static double standardDeviation(int[] arr)
        {
                double total = 0;
                for(int i = 0; i < arr.length; i++) 
                {
                        total += arr[i];
                }

                double average = total / arr.length;

                double result = 0;
                for(int i = 0; i < arr.length; i++) 
                {
                        result += Math.pow(arr[i] - average, 2);
                }

                return Math.sqrt(result / arr.length);
        }        

}
