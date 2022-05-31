package Profiler;

import Boruvka.Boruvka;
import Kruskal.Kruskal;
import Prims.Prims;
import ABTPrims.ABTPrims;
import FT.FT;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class profilerMain {
    public static ArrayList<String> labels = new ArrayList<String>();
    public static ArrayList<Long> profiling = new ArrayList<Long>();
    public static int n;
    public static int m;
    public static void main(String[] args) throws FileNotFoundException {
        Scanner fileReader = new Scanner(new File(args[0]));
        String[] line1 = fileReader.nextLine().split(",");
        n = Integer.parseInt(line1[0]);
        m = Integer.parseInt(line1[1]);
        fileReader.close();
        int[] BoruvkaResult = null;
        int[] PrimsResult = null;
        int[] KruskalResult = null;
        switch (args[1]) {
            case "Boruvka":
                BoruvkaResult = Boruvka.MST(args[0]);
                break;
            case "ABT":
                PrimsResult = ABTPrims.MST(args[0]);
                break;
            case "FT":
                PrimsResult = FT.MST(args[0]);
                break;
            case "Prims":
                PrimsResult = Prims.MST(args[0]);
                break;
            case "Kruskal":
                KruskalResult = Kruskal.MST(args[0]);
                break;
            case "All":
                BoruvkaResult = Boruvka.MST(args[0]);
                PrimsResult = Prims.MST(args[0]);
                KruskalResult = Kruskal.MST(args[0]);
        }
        TreeMap<String,Long> measurePoints = new TreeMap<String,Long>();
        for (int j = 1; j < labels.size(); j++) {
            if(labels.get(j) == "Start time") continue;
            long time = (profiling.get(j) - profiling.get(j-1)) / 1000000;
            Long measured = measurePoints.getOrDefault(labels.get(j),0L);
            measurePoints.put(labels.get(j), measured+time);
        }
        String Output = n + " : " + m + " = ";
        int total = 0;
        for(String point: measurePoints.keySet()){
            total += measurePoints.get(point);
            Output +=  point +" : " +total+"ms ; ";
        }
        System.out.println(Output);
        CorrectnessCheck.checkCorrectness(args[0],n,m,BoruvkaResult,PrimsResult,KruskalResult);
    }
}
