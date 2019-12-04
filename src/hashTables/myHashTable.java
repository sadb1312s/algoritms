package hashTables;

import java.util.Random;

public abstract class myHashTable  {
    protected int size = 10;
    protected int added;
    protected double loadFactor;
    protected int elementCount;
    protected int reWriteCount;

    protected abstract int getHash(int key);
    protected abstract void ini();
    public abstract boolean add(int key, String value);
    public abstract boolean remove(int key);
    public abstract String find(int key);
    protected abstract void resize();
    public abstract void printTable();






    public static <T extends myHashTable> boolean randomSingleTest(T t,int testArraySize,int bound){

        int[] testArray = getRandomArrayWithRepeat(testArraySize,bound);

        //if(testArraySize < 50)
            //printArray(testArray);

        for(int o : testArray){
            t.add(o,"anything "+o);
        }


        //test 1
        int errorCount = 0;

        if(testArray.length != t.elementCount + t.reWriteCount)
            errorCount++;

        //test 2

        for(int o : testArray)
            if(!t.find(o).equals("anything "+o))
                errorCount++;

        if(errorCount == 0)
            return true;
        else {
            System.out.println("error");
            return false;
        }
    }

    private static int[] getRandomArrayWithRepeat(int size,int bound){
        int[] array = new int[size];
        Random r = new Random();

        for(int i = 0; i<size; i++)
            array[i] = r.nextInt(bound);



        return array;
    }

    private static void printArray(int[] a){
        System.out.println("array size = "+a.length);
        for(int o : a)
            System.out.print(o+" ");
        System.out.println();
    }

}


