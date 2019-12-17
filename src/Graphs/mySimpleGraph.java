package Graphs;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.spi.AbstractResourceBundleProvider;

public class mySimpleGraph extends myGraph {

    Matrix matrix;

    public mySimpleGraph(){
        super();
    }

    public mySimpleGraph( boolean isOriented, boolean withWeight) {
        super(isOriented, withWeight);
    }
    public mySimpleGraph( boolean isOriented, boolean withWeight,int iniSize) {
        super(isOriented, withWeight);
        if(iniSize != 0)
            //matrix with zero value
            setMatrix(new int[iniSize][iniSize]);
        else
            try {
                throw new Exception("iniSize can not be 0");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void setMatrix(int[][] inputMatrix){
        if(inputMatrix.length > size)
            size = inputMatrix.length;

        matrix = new Matrix(inputMatrix,isOriented,withWeight);

        if(!matrix.GCheck)
            matrix = null;
    }

    public void printInfo(){
        if(matrix != null){
            System.out.println("size = "+size);
            matrix.printMatrix();
        }

    }
    @Override
    public void addPoint() {
        if(matrix != null) {
            matrix.addNode();
            size = matrix.size();
        }
    }

    @Override
    public void addWay(int point1, int point2) {
        if(matrix != null)
            addWay(point1,point2,1);
    }

    public void addWay(int point1, int point2,int weight){
        if(matrix != null) {
            if (weight == 1 || withWeight) {
                matrix.set(point1, point2, weight);
                if (!isOriented)
                    matrix.set(point2, point1, weight);
            }
            if (weight > 1 && !withWeight)
                try {
                    throw new Exception("weight = " + weight + " : Graph not have weight");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public void removeWay(int point1, int point2){
        matrix.set(point1,point2,0);
        if (!isOriented)
            matrix.set(point2,point1,0);
    }

    @Override
    public ArrayList<ArrayList<Integer>> getWays(int point1, int point2,boolean printWay) {
        //point where we were
        boolean[][] were = new boolean[matrix.size()][matrix.size()];
        ArrayList<ArrayList<Integer>> ways = new ArrayList<>();
        lookForWays(point1,point2,were,ways,new ArrayList<>());

        if(printWay)
            printWays(ways);

        return ways;

    }

    private ArrayList<ArrayList<Integer>> lookForWays(int startPoint,int finishPoint,boolean[][] were,ArrayList<ArrayList<Integer>> ways,ArrayList<Integer> currentWay){
        ArrayList<Integer> prevWay = currentWay;
        boolean[][] prevWere = were;
        //System.out.println("start with "+startPoint);
        for(int i = startPoint; i == startPoint; i++) {
            for (int j = 0; j < matrix.size(); j++) {
                //System.out.println("check "+i+" "+j+" : "+matrix.get(i,j)+" "+were[i][j]);
                if(matrix.get(i,j) != 0 && !were[i][j]){
                    were[i][j] = true;
                    currentWay.add(i);
                    //System.out.println("we in point "+i+" : "+j);
                    if(j == finishPoint){
                        //System.out.println("way found "+j);
                        currentWay.add(j);
                        ways.add(new ArrayList<>(currentWay));

                        currentWay.remove(currentWay.size() - 1);
                        continue;
                    }


                    lookForWays(j,finishPoint,were,ways,currentWay);


                }

                currentWay = new ArrayList<>();
                for(int o : prevWay)
                    currentWay.add(o);

                were = new boolean[prevWere.length][prevWere.length];
                for(int k = 0; k < prevWere.length; k++)
                    for(int l = 0; l < prevWere.length; l++)
                        were[k][l] = prevWere[k][l];

            }
        }

        return ways;

    }

    @Override
    public ArrayList<Integer> getShortestWay(int point1, int point2 ,boolean printWay) {
        //return 1 found way
        ArrayList<ArrayList<Integer>> ways = getWays(point1,point2,false);
        if(ways.size() != 0) {
            if(!withWeight) {
                int s = ways.get(0).size();
                int i = 0;

                for (int j = 1; j < ways.size(); j++) {
                    if (ways.get(j).size() < s)
                        i = j;
                }

                if(printWay)
                    printWays(ways);

                return new ArrayList<>(ways.get(i));
            }else {
                if(ways.size() == 1)
                    return ways.get(0);
                else {
                    //way price
                    int[] sum = new int[ways.size()];

                    for (int i = 0; i < ways.size(); i++) {
                        for (int j = 1; j < ways.get(i).size(); j++)
                            sum[i] += (matrix.get(ways.get(i).get(j - 1), ways.get(i).get(j)));
                    }

                    //look for min price
                    int mIndex = 0;
                    int min = sum[0];
                    for (int i = 1; i < sum.length; i++) {
                        if (sum[i] < min) {
                            min = sum[i - 1];
                            mIndex = i;
                        }
                    }

                    if(printWay)
                        printWays(ways,sum);
                    System.out.println("shortest way " + mIndex + " " + sum[mIndex]);
                    ArrayList<Integer> shortest = ways.get(mIndex);

                    for (int o : shortest)
                        System.out.print(o + " -> ");
                    System.out.println(" : " + sum[mIndex]);

                    return shortest;
                }
            }
        }
        return null;
    }

   private ArrayList<ArrayList<Integer>> clearRepeatWay(ArrayList<ArrayList<Integer>> ways){
        //remove cycle ways, part of cycle way
       //...

       return ways;
   }

   private void printWays(ArrayList<ArrayList<Integer>> ways){
       for(ArrayList<Integer> o : ways) {
           for (int k : o)
               System.out.print(k + " -> ");
           System.out.println();
       }
   }

   private void printWays(ArrayList<ArrayList<Integer>> ways,int sum[]){
        for(int i = 0; i < ways.size(); i++) {
            for (int k : ways.get(i))
                System.out.print(k + " -> ");
            System.out.println(" : "+sum[i]);
        }
    }
}
