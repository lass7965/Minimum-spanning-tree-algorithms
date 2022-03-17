package Kruskal;
import Kruskal.Kruskal.Edge;

import java.util.Arrays;

public class Sorting {
    // quicksort the array a[] using dual-pivot quicksort
    public static void sort(int[] a) {
        sort(a, 0, a.length - 3);
        assert isSorted(a);
    }

    // quicksort the subarray a[lo .. hi] using dual-pivot quicksort
    private static void sort(int[] a, int lo, int hi) {
        if (hi <= lo) return;
        // make sure a[lo] <= a[hi]
        if (a[hi+2] < a[lo+2]) exch(a, lo, hi);

        int lt = lo + 3, gt = hi - 3;
        int i = lo + 3;
        while (i <= gt) {
            if (a[i+2] < a[lo+2]) {
                exch(a, lt, i);
                lt+=3;
                i+=3;
            }
            else if (a[hi+2] < a[i+2]) {
                exch(a, i, gt);
                gt -= 3;
            }
            else i+=3;
        }
        exch(a, lo, lt-=3);
        exch(a, hi, gt+=3);

        // recursively sort three subarrays
        sort(a, lo, lt-3);
        if (a[lt+2] < a[gt+2]) sort(a, lt+3, gt-3);
        sort(a, gt+3, hi);

        assert isSorted(a, lo, hi);
    }



    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/
    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int swap1 = a[i];
        int swap2 = a[i+1];
        int swap3 = a[i+2];
        a[i] = a[j];
        a[i+1] = a[j+1];
        a[i+2] = a[j+2];
        a[j] = swap1;
        a[j+1] = swap2;
        a[j+2] = swap3;
    }

    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(int[] a) {
        return isSorted(a, 0, a.length - 3);
    }

    private static boolean isSorted(int[] a, int lo, int hi) {
        for (int i = lo + 3; i <= hi; i+=3)
            if (a[i+2] < a[i-1]) return false;
        return true;
    }

    public static void main(String[] args) {
        int[] input = {10,1,100,5,2,90,15,3,80,7,4,70,1,4,70,3,4,70};
        sort(input);
        System.out.println(Arrays.toString(input));
    }
}
