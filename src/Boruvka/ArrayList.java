package Boruvka;

import java.util.Arrays;

public class ArrayList {
    int[] data;
    int size = 0;
    public ArrayList(){
        data = new int[10];
    }
    public ArrayList(int size){
        data = new int[size];
    }

    public void add(int elem){
        if(size < data.length){ // Check that add is within index bounds
            data[size++] = elem;
        } else {
            data = Arrays.copyOf(data,size*2);
            data[size++] = elem;
        }
    }

    public void add(int[] list){
        for (int i:list) {
            this.add(i);
        }
    }

    public int get(int index){
        return data[index];
    }

    public void clear(){
        this.size = 0;
    }

    public int size(){
        return size;
    }

    public int[] getArray() {
        return Arrays.copyOf(data,size);
    }
}

