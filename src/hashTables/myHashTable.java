package hashTables;

import java.util.Random;

public abstract class myHashTable  {
    protected int size = 10;
    protected int added;
    protected double loadFactor;

    protected abstract int getHash(int key);
    protected abstract void ini();
    public abstract boolean add(int key, String value);
    public abstract boolean remove(int key);
    public abstract String find(int key);
    protected abstract void resize();
    public abstract void printTable();
    public<T extends myHashTable> void test(T t){
        randomTest(t);
    }


    static public <T extends myHashTable> void randomTest(T t){

        Random r = new Random();
        for(int i = 0; i < t.size ; i++){
            int x = r.nextInt(500);
            t.add(x," any "+x);
            if(t.loadFactor>=0.75){
                t.printTable();
                return;
            }
        }
        t.printTable();
    }


}


