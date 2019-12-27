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

    boolean[] were;
    int[] mark;
    boolean noWay = false;
    public void Dijkstra(int startPoint, int finishPoint){
        boolean thisIsStart = false;
        //System.out.println("start = "+startPoint);

        if(were == null && mark == null) {
            were = new boolean[matrix.size()];
            mark = new int[matrix.size()];
            thisIsStart = true;
        }

        for(int i = 0; i < matrix.size(); i++){
            int currentWay = matrix.get(startPoint,i);
            if(currentWay != 0 && (mark[i] > mark[startPoint]+currentWay || mark[i] == 0) && !were[i]) { //& i != startPoint
                mark[i] = mark[startPoint] + currentWay;
            }

            if(i == matrix.size() - 1)
                were[startPoint] = true;
        }


        /*for(int o : mark)
            System.out.println("> "+o);
        for(boolean o: were)
            System.out.println("> "+o);*/


        Integer nextIndex = DijkstraGetMin(startPoint,finishPoint);
        if(nextIndex == null){
            noWay = true;
        }else {
            Dijkstra(nextIndex,finishPoint);
            if(noWay) {
                //System.out.println("no way next " + nextIndex + " this " + startPoint);
                Dijkstra(startPoint,finishPoint);

            }
        }


        if(thisIsStart){
            //System.out.println("end "+startPoint);
            //get Way
            System.out.println("> "+finishPoint);
            DijkstraAnalise(startPoint,finishPoint);
        }

    }

    private Integer DijkstraGetMin(int startPoint,int finishPoint){
        int nextIndex = 0; // min


        for(int i = 0; i < mark.length; i++) {

            if(matrix.get(startPoint,i) != 0) {
                int o = mark[i];

                if (o != 0 && !were[i]) {
                    if (nextIndex == 0)
                        nextIndex = i;
                    else if (o < mark[nextIndex])
                        nextIndex = i;


                }
            }

        }

        //System.out.println("next min = "+mark[nextIndex]+" "+nextIndex+" "+startPoint);

        if(nextIndex == 0) {
            return null;
        }
        else
            return nextIndex;
    }

    private void DijkstraAnalise(int startPoint, int finishPoint){

        for(int i = 0; i < matrix.size(); i++){
            if(matrix.get(finishPoint,i) != 0 || matrix.get(i,finishPoint) != 0){
                if(mark[finishPoint] - matrix.get(i,finishPoint) == mark[i]) {
                    System.out.println("> "+i);
                    DijkstraAnalise(startPoint,i);
                    break;
                }


            }
        }
    }

    //минимальное останове дерево
    public void Kraskal() {
        Matrix m = new Matrix(new int[matrix.size()][matrix.size()], false, matrix.withWeight);
        //перебираем ребрка ищем минимальное
        ArrayList<Edge> edges = getEdge();

        /*System.out.println("edges");
        for(Edge o : edges)
            System.out.println(o.i+" "+o.j+" : "+o.weight);*/

        Edge min = edges.get(0);
        int count = edges.size() - 1;
        ArrayList<ArrayList<Integer>> col = new ArrayList<>();

        while (count >=0) {
            min = new Edge(0,0,0);
            for (Edge o : edges) {
                if (!o.were) {
                    if (o.weight < min.weight || min.weight == 0) {
                        min = o;
                    }
                }
            }
            min.were = true;
            count--;

            System.out.println(min.i+" "+min.j+" : "+min.weight);
            if(col.size() == 0){
                col.add(new ArrayList<>());
                col.get(0).add(min.i);
                col.get(0).add(min.j);
                m.set(min.i,min.j,min.weight);
            }else {

                for(ArrayList<Integer> o : col) {
                    for (Integer x : o)
                        System.out.print(x + " ");
                    System.out.println();
                }
                System.out.println("------------");

                for(ArrayList<Integer> o : col)
                    if(!o.contains(min.i) && !o.contains(min.j)){

                    }else {
                        System.out.println("!");
                        if(o.contains(min.i) && !o.contains(min.j))
                            o.add(min.j);
                        if(!o.contains(min.i) && o.contains(min.j))
                            o.add(min.i);


                    }

            }

            /*System.out.println("----");
            for(ArrayList<Integer> o : col) {
                for (Integer x : o)
                    System.out.print(x + " ");
                System.out.println();
            }
            System.out.println("----");*/

        }





    }

    private ArrayList<Edge> getEdge(){
        ArrayList<Edge> edges = new ArrayList<>();
        for(int i = 0; i < matrix.size(); i++)
            for (int j = i; j < matrix.size(); j++)
                if(matrix.get(i,j) != 0)
                    edges.add(new Edge(i,j,matrix.get(i,j)));

        System.out.println(edges.size());

        return edges;
    }

    /*private ArrayList<Edge> sortEdges(){
        //sorting.... quick sort?....
        return null;
    }*/

    class Edge{
        int i;
        int j;
        int weight;
        boolean were = false;

        public Edge(int i, int j, int weight) {
            this.i = i;
            this.j = j;
            this.weight = weight;
        }
    }

}
