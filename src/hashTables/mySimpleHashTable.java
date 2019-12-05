package hashTables;

public class mySimpleHashTable extends myHashTable {
    //простая таблица, без цепочек, если попали в тот же хэш перезаписываем

    int[] keys;
    String[] values;

    public mySimpleHashTable(){
        ini();
    }

    public mySimpleHashTable(int initialSize){
        size = initialSize;
        ini();
    }

    @Override
    protected int getHash(int key) {
        return key % size;
    }

    @Override
    protected void ini() {
        keys = new int[size];
        values = new String[size];
    }

    @Override
    public boolean add(int key, String value) {
        try{
            int hash =getHash(key);

            if(values[hash] == null) {
                added++;
                loadFactor =  1.0 /((double)size / (double) added);
            }

            keys[hash] = key;
            values[hash] = value;

            return true;
        }catch (Exception e) {
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
        return "";
    }

    @Override
    protected void resize() {

    }

    @Override
    public void printTable() {
        System.out.println("size = "+size +" loadFactor = "+loadFactor+" added "+added);
        for(int i = 0; i < size; i++)
            if(values[i] != null)
                System.out.println(keys[i]+" \""+values[i]+"\"");
    }


    @Override
    public void clearAll() {
        keys = new int[10];
        values = new String[10];
    }
}