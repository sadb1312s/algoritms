package Graphs;

public abstract class myGraph {
    protected boolean isOriented = false;
    protected boolean withWeight = false;
    protected int initialSize = 5;
    protected int size = 0;

    public myGraph(){
        size = initialSize;
    }

    public myGraph(int iniciialSize, boolean isOriented, boolean withWeight){
        this.size = iniciialSize;
        this.isOriented = isOriented;
        this.withWeight = withWeight;
    }

    public abstract void addPoint();
    public abstract void addWay(int point1,int point2);
    public abstract void getWays(int point1,int point2);
    public abstract void getShortestWay(int point1,int point2);
}
