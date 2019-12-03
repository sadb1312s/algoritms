package hashTables;

import java.util.LinkedList;

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
                loadFactor =  1.0 /((double)size / (double) added);
                table[hash] = new LinkedList<>();

            }

            //проверить есть ли такой ключ
            boolean is = false;
            for(int i = 0; i < table[hash].size(); i++)
                if(table[hash].get(i).key == key) {
                    is = true;
                    table[hash].get(i).value = value;
                }

            if(!is)
                table[hash].add(new Node(key,value));

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean remove(int key) {
        return false;
    }

    @Override
    public String find(int key) {
        int hash = getHash(key);
        if(table[hash] != null)
            for(int i = 0 ; i < table[hash].size(); i++){
                if(table[hash].get(i).key == key)
                    return table[hash].get(i).value;
            }

        return null;
    }

    @Override
    protected void resize() {

    }

    @Override
    public void printTable() {
        System.out.println("size = "+size +" loadFactor = "+loadFactor+" added "+added);

        for(int i = 0; i < size; i++)
            if (table[i] != null) {
                System.out.print("hash = "+i+" ");
                for (int j = 0; j < table[i].size(); j++)
                    System.out.print("( "+table[i].get(j).key + " " + table[i].get(j).value+" )");
                System.out.println();
            }



    }
}
