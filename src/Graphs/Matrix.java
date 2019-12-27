package Graphs;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;

class Matrix{
    protected boolean withWeight = false;
    protected boolean isOriented = false;
    ArrayList<ArrayList<Integer>> matrix;
    protected Matrix(int[][] inputMatrix, boolean isOriented, boolean withWeight){
        //only square matrix and not null
        this.isOriented = isOriented;
        this.withWeight = withWeight;
        boolean check = checkInputMatrix(inputMatrix);

        if(check) {

            matrix = new ArrayList<>(inputMatrix.length);

            for (int i = 0; i < inputMatrix.length; i++)
                matrix.add(new ArrayList(inputMatrix.length));

            for (int i = 0; i < inputMatrix.length; i++) {
                for (int j = 0; j < inputMatrix.length; j++) {
                    matrix.get(i).add(j, inputMatrix[i][j]);
                }
            }

        }
    }
    boolean GCheck = true;

    protected void printMatrix(){
        if(matrix != null)
            for(ArrayList<Integer> o : matrix) {
                for (int x : o)
                    System.out.print(x+" ");
                System.out.println();
            }
    }

    protected int size(){
        return matrix.size();
    }

    //check input matrix
    protected boolean checkInputMatrix(int[][] matrix){
        GCheck = true;
        boolean check = true;

        if(matrix == null)
            try {
                check = false;
                throw new Exception("Input matrix is null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        if(matrix.length == 0)
            try {
                check = false;
                throw new Exception("Input matrix have 0 length");
            } catch (Exception e) {
                e.printStackTrace();
            }

        if(isOriented)
            if(!checkRepeatWay(matrix))
                check = false;

        int size = matrix.length;
        for(int[] o : matrix) {
            for(int x : o){
                //weight value check
                if(!withWeight && x > 1)
                    try {
                        check = false;
                        throw new Exception("Input matrix have weight, Graph not have weight");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            if (o.length != size)
                try {
                    check = false;
                    throw new Exception("Input matrix is not square");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        GCheck = check;
        return check;
    }

    protected boolean checkRepeatWay(int[][] matrix){
        boolean check = true;

        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix.length; j++)
                if(matrix[i][j] != 0 && matrix[j][i] != 0)
                    try {
                        check = false;
                        throw new Exception("Graph is oriented, was find repeat way "+i+" "+j+" "+matrix[i][j]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


        return check;
    }
    //add new Node
    protected void addNode(){
        for(ArrayList<Integer> o : matrix)
            o.add(0);

        ArrayList<Integer> n = new ArrayList<>();
        for(int i = 0; i < matrix.size() + 1; i++)
            n.add(0);

        matrix.add(n);

    }

    //add way
    protected void set(int point1, int point2){
        set(point1,point2,1);
    }
    protected void set(int point1, int point2,int weight){
        if(matrix.get(point2).get(point1) != 0 && isOriented)
            try {
                throw new Exception("Graph is oriented, way from point: "+point2+" to point: "+point1+" already added");
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            matrix.get(point1).set(point2,weight);
    }

    protected int get(int point1, int point2){
        return matrix.get(point1).get(point2);
    }

    protected int findMax(){
        int max = 0;

        for(int i = 0; i < matrix.size(); i++)
            for(int j = 0; j < matrix.size(); j++)
                if(matrix.get(i).get(j) > max)
                    max = matrix.get(i).get(j);


        return max;
    }

}
