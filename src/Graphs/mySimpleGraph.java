package Graphs;

public class mySimpleGraph extends myGraph {

    Matrix matrix;

    public mySimpleGraph(){
        super();
    }

    public mySimpleGraph(int inicitialSize, boolean isOriented, boolean withWeight) {
        super(inicitialSize, isOriented, withWeight);
    }

    public void setMatrix(int[][] inputMatrix){
        if(inputMatrix.length > size)
            size = inputMatrix.length;

        matrix = new Matrix(inputMatrix);
    }

   /* public void setMatrix(Matrix matrix){
        if(matrix.size() > size)
            size = matrix.size();

    }*/

    @Override
    public void addPoint() {

    }

    @Override
    public void addWay(int point1, int point2) {

    }

    @Override
    public void getWays(int point1, int point2) {

    }

    @Override
    public void getShortestWay(int point1, int point2) {

    }
}
