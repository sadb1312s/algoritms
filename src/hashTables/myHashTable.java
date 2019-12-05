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
    public abstract void clearAll();

    private boolean randomSingleTest(int testArraySize,int bound){
        int[] testArray = getRandomArrayWithRepeat(testArraySize,bound);

        //if(testArraySize < 50)
            //printArray(testArray);

        for(int o : testArray){
            add(o,"anything "+o);
        }

        //test 1
        int errorCount = 0;

        if(testArray.length != elementCount + reWriteCount)
            errorCount++;

        //test 2

        for(int o : testArray)
            if(!find(o).equals("anything "+o))
                errorCount++;

        if(errorCount == 0) {
            return true;
        }
        else {
            //System.out.println("error");
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

    public void randomTest(){
        randomTest(1);
    }

    public void randomTest(int testArraySize, int bound){
        randomSingleTest(testArraySize,bound);
    }

    public void randomTest(int testCount){
        Random r = new Random();
        int errorCount =0;

        myHashTable table = null;

        long midS = 0;
        long midB = 0;

        Long start = System.currentTimeMillis();
        for(int i = 0; i<testCount; i++){
            if(i%10 == 0)
                System.out.println("test "+i+" start");



            int s = r.nextInt(1_000_000);
            int d = r.nextInt(1_000_000);

            midS += s;
            midB += d;

            if(!randomSingleTest(s,d))
                errorCount++;

            clearAll();
        }
        Long finish = System.currentTimeMillis();
        System.out.println("test time = "+(finish - start)+" ms");
        System.out.println("testCount = "+testCount+" errrorCount = "+errorCount);
        System.out.println("midSize = "+midS/testCount+" midBound = "+midB/testCount);
    }



}


