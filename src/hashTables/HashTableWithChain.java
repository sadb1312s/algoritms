package hashTables;

import com.company.Main;

import java.util.LinkedList;
import java.util.Random;

public class HashTableWithChain extends myHashTable {

    LinkedList<Node>[] table;

    class Node{
        int key;
        String value;

        public Node(int key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public HashTableWithChain(){
        ini();
    }
    public HashTableWithChain(int initialSize){
        size = initialSize;
        ini();
    }

    @Override
    protected int getHash(int key) {
        return key % size;
    }

    @Override
    protected void ini() {
        table = new LinkedList[size];
    }

    @Override
    public boolean add(int key, String value) {
        try {
            int hash = getHash(key);
            if(table[hash] == null) {
                added++;
                loadFactor = getLoadFactor();
                table[hash] = new LinkedList<>();

                if(loadFactor >= 0.75) {
                        resize();
                        return add(key,value);

                }
            }

            //проверить есть ли такой ключ
            boolean is = false;

            for(int i = 0; i < table[hash].size(); i++)
                if(table[hash].get(i).key == key) {
                    is = true;
                    reWriteCount++;
                    table[hash].get(i).value = value;
                    //System.out.println("reWRITE!!! "+key);
                    break;

                }

            if(!is) {
                table[hash].add(new Node(key, value));
                elementCount++;
                //System.out.println("add "+key);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    private double getLoadFactor(){
        return  1.0 /((double)size / (double) added);
    }

    @Override
    public void clearAll() {
        size  = 10;
        added = elementCount = reWriteCount = 0;
        loadFactor = 0;
        table = new LinkedList[size];
    }

    @Override
    public boolean remove(int key) {
        Node o = findObject(key);


        int hash = getHash(key);
        if(o != null){
            table[hash].remove(o);
            if(table[hash].size() == 0)
                table[hash] = null;

            return true;
        }

        return false;

    }

    public Node findObject(int key){
        int hash = getHash(key);
        if(table[hash] != null)
            for(int i = 0 ; i < table[hash].size(); i++){
                if(table[hash].get(i).key == key) {
                    //System.out.println(table[hash].get(i));
                    return table[hash].get(i);
                }
            }

        return null;
    }

    @Override
    public String find(int key) {
        Node o = findObject(key);

        if(o != null)
            return o.value;
        else
            return null;
    }

    @Override
    protected void resize() {

        size = (int)((float)size * 1.5);

        LinkedList<Node>[] temp = table.clone();
        table = new LinkedList[size];
        loadFactor = added = elementCount  = 0;


        for(int i = 0; i<temp.length; i++){
            if(temp[i] != null){
                for(int j=0; j<temp[i].size(); j++){
                    add(temp[i].get(j).key,temp[i].get(j).value);
                }
            }
        }

        loadFactor = getLoadFactor();
    }

    @Override
    public void printTable() {
        System.out.println("size = "+size +" loadFactor = "+loadFactor+" elementCount = "+elementCount+" rewriteCount = "+reWriteCount);

        for(int i = 0; i < size; i++)
            if (table[i] != null) {
                System.out.print("hash = "+i+" ");
                for (int j = 0; j < table[i].size(); j++)
                    System.out.print("( "+table[i].get(j).key + " \"" + table[i].get(j).value+"\" )");
                System.out.println();
            }



    }
}
