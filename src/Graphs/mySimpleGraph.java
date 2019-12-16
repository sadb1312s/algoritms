package Graphs;

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
    public void getWays(int point1, int point2) {

    }

    @Override
    public void getShortestWay(int point1, int point2) {

    }
}
