public class SequentialSort implements Sorter {

        //Quicksort 
        public SequentialSort() {
                
        }

        
        public void sort(int[] arr) {
                sort(arr, 0, arr.length - 1);
        }

        //main function for quicksort method
        public void sort(int[] arr, int start, int end) {
                //If this is not the case we are done sorting
                if (start < end)
                {
                        //Get the correct index value of the partition element
                        int partitionIndex = partition(arr, start, end);
                        sort(arr, start, partitionIndex - 1);
                        sort(arr, partitionIndex + 1, end);
                }
        }

        //function to sort the partitioned array
        public int partition(int[] arr, int start, int end){
                //make last element pivot
                int pivot=arr[end];      
                int i=start-1;
                //iterate over elements and swap if necessary
                for(int j=start; j<=end;j++){
                        if(arr[j]<pivot){
                                i++;
                                int temp=arr[i];
                                arr[i]=arr[j];
                                arr[j]=temp;
                        }
                }
                //place pivot in right place
                int temp=arr[i+1];
                arr[i+1]=arr[end];
                arr[end]=temp;
                //return index of pivot element
                return i+1;
        }

        public int getThreads() {
                return 1;
        }
}
