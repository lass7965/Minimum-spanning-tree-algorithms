package Profiler;

import Boruvka.Boruvka;
import Kruskal.Kruskal;
import Kruskal.Sorting;
import Prims.Prims;

import java.io.FileNotFoundException;

public class CorrectnessCheck {
    public static void checkCorrectness(String graph,int n,int m,int[] BoruvkaResult, int[] PrimsResult,int[] KruskalResult) throws FileNotFoundException {
        String output = "n="+n+";m="+ m+" : ";
        try{
            /**                  Compare Boruvka and Prims                   **/
            if(BoruvkaResult==null) BoruvkaResult = Boruvka.MST(graph);
            if(PrimsResult==null) PrimsResult = Prims.MST(graph);
            if(KruskalResult==null) KruskalResult = Kruskal.MST(graph);
            Sorting.sort(BoruvkaResult);
            Sorting.sort(PrimsResult);
            output = compareResults(BoruvkaResult,PrimsResult,"Boruvka","Prims",output);
            output = compareResults(BoruvkaResult,KruskalResult,"Boruvka","Kruskal",output);
            if(output != "n="+n+";m="+ m+" : ") System.err.println(output);
        } catch (OutOfMemoryError e) {
            System.err.println(output + "Correctness check failed; Ran out of memory");
        }
    }

    public static String compareResults(int[] arr1, int[] arr2, String arr1Name, String arr2Name, String output){
        boolean flag = true;
        boolean weightFlag = true;
        if(arr1.length == arr2.length){
            for (int i = 0; i < arr1.length; i+=3) {
                if(arr1[i] != arr2[i] || arr1[i+1] != arr2[i+1]){
                    flag = false;
                }
                if(arr1[i+2] != arr2[i+2]) {
                    weightFlag = false;
                }
            }
            if(!flag && weightFlag) output += arr1Name+" found similar MST as "+arr2Name+"; ";
            else if(!flag) output += arr1Name+" found different MST than "+arr2Name+"! ";
        } else {
            output += arr1Name+".length = " + arr1.length + "!= "+arr2Name+".length = " + arr2.length + "; ";
            int i = 0;
            int j = 0;
            while(arr1.length > i && arr2.length > j) {
                if(arr1[i+2] < arr2[j+2]){
                    output += "\n    The following is in " + arr1Name + " but not in " + arr2Name + " " + arr1[i]+" "+arr1[i+1]+" "+arr1[i+2] +"\n";
                    i+=3;
                }
                if(arr1[i+2] > arr2[j+2]){
                    output += "\n    The following is in " + arr1Name + " but not in " + arr2Name + " " + arr2[j]+" "+arr2[j+1]+" "+arr2[j+2] +"\n";
                    j+=3;
                }
                else {
                    i +=3;
                    j+=3;
                }
            }
        }
        return output;
    }
}
