package Graphs;

import java.util.ArrayList;

class Matrix {
    ArrayList<ArrayList> matrix;
    public Matrix(int[][] inputMatrix){
        //only square matrix and not null
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

            printMatrix();
        }
    }

    public void printMatrix(){
        for(ArrayList<Integer> o : matrix) {
            for (int x : o)
                System.out.print(x+" ");
            System.out.println();
        }
    }

    public int size(){
        return matrix.size();
    }

    private boolean checkInputMatrix(int[][] matrix){
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

        int size = matrix.length;
        for(int[] o : matrix)
            if(o.length != size)
                try {
                    check = false;
                    throw new Exception("Input matrix is not square");
                } catch (Exception e) {
                    e.printStackTrace();
                }

        return check;
    }
}
