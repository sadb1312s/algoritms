package Graphs;

import java.util.ArrayList;

public abstract class myGraph {
    protected boolean isOriented = false;
    protected boolean withWeight = false;
    protected int size = 0;

    public myGraph(boolean isOriented, boolean withWeight){
        this.isOriented = isOriented;
        this.withWeight = withWeight;
    }

    public myGraph() {

    }

    public abstract void addPoint();
    public abstract void addWay(int point1,int point2);
    public abstract ArrayList<ArrayList<Integer>> getWays(int point1, int point2,boolean printWay);
    public abstract ArrayList<Integer> getShortestWay(int point1,int point2,boolean printWay);
}
