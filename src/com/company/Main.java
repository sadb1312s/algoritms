package com.company;



import hashTables.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;

//test2
public class Main {

    public static void main(String[] args) {

        new HashTableWithChain().randomTest(10);
        new mySimpleHashTable().randomTest(10);

    }

    static int error = 0;
    static CountDownLatch l = new CountDownLatch(Runtime.getRuntime().availableProcessors());
    public static void BTreeTest(){

        class testThread extends Thread{
            @Override
            public void run() {
                for(int i = 0 ; i < 1_000_000 ; i++)
                    try {
                        //System.out.println("=======================================");
                        randomTest(32,1000,5000);
                        System.out.println(getName()+" "+i);
                    }catch (Exception e){
                        System.out.println("ERRROR "+i);
                        e.printStackTrace();
                        break;
                    }

                l.countDown();
            }

        }


        for(int i = 0 ; i < Runtime.getRuntime().availableProcessors(); i++){
            testThread t = new testThread();
            t.start();

        }

        try {
            l.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Количество ошибок "+error);
    }
    public static int[] getRandomArray(int size,int d) {
            //random array without repeats
            if(d<size)
                try {
                    throw new Exception("do you want infinity cycle? really?");
                } catch (Exception e) {
                    e.printStackTrace();
                }


        int[] arr = new int[size];
            Random r = new Random();

            for(int i=0;i<size;i++){
                int x;
                boolean check;

                do {
                    check = true;
                    x = r.nextInt(1+d);
                    for (int j = 0; j < size&&check; j++) {
                        if (arr[j] == x)
                            check = false;

                    }
                } while (!check);

                arr[i] = x;
            }



            return arr;

        }
    public static void randomTest(int treeT,int arraySize,int range){

        BTree2 tree = new BTree2(treeT);

        int[] x = getRandomArray(arraySize, range);
        //for(int i : x)
        //    System.out.print(i+",");
        //System.out.println();

        ArrayList<Integer> shuffleX = new ArrayList<>();
        for(int i : x)
            shuffleX.add(i);

        Collections.shuffle(shuffleX);

        //for(int i : shuffleX)
        //    System.out.print(i+",");
        //System.out.println();

        tree.add(x);
        //tree.print2();
        //System.out.println(tree.propertiesCheck());

        tree.remove(shuffleX);

    }
}

class myBinaryTree{
    Element element;

    public myBinaryTree(){
        element = new Element();
    }

    public void add(int x){
        if(element.nullFlag) {
            element = new Element(x);
        }else {
            Element e = new Element(x);
            element.insert(e);
        }
    }

    public void add(int[] x){
        for(int o:x)
            add(o);

    }

    int find(int x){
        return element.find(x);
    }

    void print(){
        if(!element.nullFlag){
            element.print();
            System.out.print("\n");
        }
    }

    void remove(int x){
        element.remove(x);
    }

    boolean checkThree(){
        //is binary tree?

        boolean checkResult = element.checkElement(false);

        if(checkResult)
            System.out.println("Tree is binary tree");
        else
            System.out.println("Tree is not binary tree");

        return checkResult;

    }

    void printSort(){
        System.out.println();
        element.printSort();
    }

    void printSortBack(){
        System.out.println();
        element.printSortBack();
    }
}

class Element{

    boolean nullFlag = true;
    int key;

    Element leftChild = null;
    Element rightChild = null;

    public Element(){
    }

    public Element(int x){
        key = x;
        nullFlag = false;
    }

    int find(int x){
        if(key== x){
            return key;
        }

        if(x<key)
            return leftChild.find(x);

        if(x>key)
            return rightChild.find(x);

        return 0;
    }

    void remove(int x){

        //System.out.println(x);
        Element tempLeftChild;
        Element tempRightChild;

        boolean find = false;

        if( key == x){
            tempLeftChild = leftChild.leftChild;
            tempRightChild = leftChild.rightChild;

            //System.out.println(tempLeftChild.key+" "+tempRightChild.key);

            key = leftChild.key;


            leftChild = tempLeftChild;
            //System.out.println(leftChild.key);
            //leftChild.rightChild = tempRightChild;
            insert(tempRightChild);
        }

        if (leftChild!=null&&leftChild.key == x) {
                find = true;
                tempLeftChild = leftChild.leftChild;
                tempRightChild = leftChild.rightChild;

                leftChild = null;
                insert(tempLeftChild);
                insert(tempRightChild);

        }

        if (rightChild!=null&&rightChild.key == x) {

            find = true;
            tempLeftChild = rightChild.leftChild;
            tempRightChild = rightChild.rightChild;
            rightChild = null;

            insert(tempLeftChild);
            insert(tempRightChild);
        }


        if(!find) {
            if (leftChild != null)
                if (leftChild.key < x)
                    leftChild.remove(x);
            if (rightChild != null)
                if (rightChild.key > x)
                    rightChild.remove(x);
        }








    }

    void print(){
        System.out.print("["+key+":");
        if(leftChild!=null)
            System.out.print(leftChild.key+" ");
        else
            System.out.print("- ");

        if(rightChild!=null)
            System.out.print(rightChild.key);
        else
            System.out.print("-");

        System.out.print("]");

        if(leftChild!=null)
            leftChild.print();

        if(rightChild!=null)
            rightChild.print();

    }

    void printThis(){
        try {
            System.out.print("[" + key + ":" + leftChild.key + " " + rightChild.key + "]");
        }catch (NullPointerException e){
            //end list
        }
    }

    boolean checkElement(boolean checkResult){

        //printThis();

        if(leftChild!=null) {
            checkResult = leftChild.key < key;
        }

        if(rightChild!=null) {
            checkResult = rightChild.key > key;
        }

        //System.out.print(" this tree check:"+checkResult+"\n");

        if(leftChild!=null)
            checkResult = leftChild.checkElement(checkResult);

        if(rightChild!=null) {
            checkResult = rightChild.checkElement(checkResult);
        }


        return checkResult;


    }

    void insert(Element insertElement){

        if(insertElement!=null) {
            if (insertElement.key < key) {
                if (leftChild == null)
                    leftChild = insertElement;
                else
                    leftChild.insert(insertElement);
            }

            if (insertElement.key > key) {
                if (rightChild == null)
                    rightChild = insertElement;
                else
                    rightChild.insert(insertElement);
            }
        }
    }

    void printSort(){

        if(leftChild!=null){
            leftChild.printSort();

        }
        System.out.print(" "+key);
        if(rightChild!=null){
            rightChild.printSort();
        }
    }

    void printSortBack(){
        if(rightChild!=null){
            rightChild.printSortBack();
        }
        System.out.print(" "+key);
        if(leftChild!=null){
            leftChild.printSortBack();

        }
    }
}

class AVLTree{
    boolean isBalance;
    boolean nullFlag = true;
    static boolean found =false;
    int key;
    AVLTree parent;
    AVLTree leftChild;
    AVLTree rightChild;
    int height;

    AVLTree(){}

    AVLTree(int x){
        key = x;
        nullFlag = false;
        height = 1;
    }

    void add(int x){
        insert(new AVLTree(x));
    }

    void add(int[] x){
        for(int o:x) {
            insert(new AVLTree(o));

        }
    }

    public boolean isBalance() {
        isBalance = checkTree();

        if(isBalance)
            System.out.println("the tree is AVL tree");
        else
            System.out.println("the tree is not AVL tree");

        return isBalance;
    }

    public AVLTree find(int x) {
        AVLTree t = null;
        if(key == x) {
            found = true;
            return this;
        }
        else {
            if(!found)
                if(leftChild!=null)
                    t = leftChild.find(x);

            if(!found)
                if(rightChild!=null) {
                    t = rightChild.find(x);

                }

            if(parent == null)
                found = false;

            return t;
        }
    }

    public void delete(int x){

        AVLTree del = find(x);
        AVLTree min;
        if(del!=null){

            if(del.leftChild==null&&del.rightChild==null) {
                System.out.println("!!!");
                del.removeNode();
                del.getBalance();
                del.recountParent();
                del = null;
            }else {

                if(del.rightChild!=null) {
                    min = del.rightChild.findMin();
                    System.out.println("!! -> "+min.key);

                    if(min == min.parent.leftChild){
                        min.parent.leftChild = min.parent.leftChild.leftChild;
                        if(min.parent.leftChild!=null)
                            min.parent.leftChild.parent = this;


                    }

                    if(min == min.parent.rightChild) {
                        min.parent.rightChild = min.parent.rightChild.rightChild;
                        if(min.parent.rightChild!=null)
                            min.parent.rightChild.parent = this;

                    }


                    del.key = min.key;

                    if(min.parent.leftChild!=null)
                        min.parent.leftChild.getBalance();
                    if(min.parent.rightChild!=null)
                        min.parent.rightChild.getBalance();
                    if(min.parent!=null) {
                        min.parent.getBalance();
                        min.parent.recountParent();
                    }


                    min = del = null;



                }
                else {
                    System.out.println("!");
                    if(del.leftChild!=null){

                        del.key = del.leftChild.key;
                        //System.out.println("!! "+del.key);
                        /*del.parent.rightChild = del.leftChild;
                        del.leftChild.parent = del.parent;*/
                        del.leftChild = del.leftChild.leftChild;



                        del.getBalance();
                        del.recountParent();
                        del = null;
                    }
                }
            }
        }
    }

    private void removeNode(){

            if (parent.leftChild == this) {
                parent.leftChild = null;
            }
            if (parent.rightChild == this)
                parent.rightChild = null;

    }

    public AVLTree findMax(){
        if(rightChild!=null)
            return rightChild.findMax();
        else
            return this;

    }

    public AVLTree findMin(){
        if(leftChild!=null)
            return leftChild.findMin();
        else
            return this;
    }

    private AVLTree cloneThis(){
        AVLTree tr = new AVLTree(key);
        tr.leftChild = leftChild;
        tr.rightChild = rightChild;

        if(tr.leftChild!=null)
            leftChild.parent = tr;
        if(tr.rightChild!=null)
            rightChild.parent = tr;

        return tr;
    }

    private boolean checkTree(){
        int b = getBalance();
        boolean balance = true;
        //System.out.println("> "+b+" "+balance);

        if(b > 1) {
            balance = false;
            return balance;
        }
        else  {
            if(leftChild!=null){
                balance = leftChild.checkTree();
                if(!balance)
                    return false;
            }
            if(rightChild!=null){
                balance = rightChild.checkTree();
                if(!balance)
                    return false;
            }

            return balance;
        }
    }

    private void insert(AVLTree tree){
        if(nullFlag) {
            key = tree.key;
            nullFlag = false;
        }
        if(tree.key < key){
            if(leftChild == null) {
                tree.parent = this;
                leftChild = tree;

                tree.recountParent();
            }
            else
                leftChild.insert(tree);
        }
        if(tree.key > key){
            if(rightChild == null) {
                tree.parent = this;
                rightChild = tree;

                tree.recountParent();
            }
            else
                rightChild.insert(tree);
        }
    }

    private void recountParent(){
        //System.out.println("recount parent "+parent);
        if(parent!=null) {
            parent.recountHeight();
            parent.recountParent();
        }
    }

    private void recountHeight(){
        //check balance
        int balance = getBalance();

        if(balance>1){
            rebalance();
            recountHeight();
        }
    }

    private int  getBalance(){
        //получить высоту и баланс
        int leftHeight = 0;
        int rightHeight = 0;

        if(leftChild!=null)
            leftHeight = leftChild.height;
        if(rightChild!=null)
            rightHeight = rightChild.height;

        height = Math.max(leftHeight,rightHeight)+1;

        return Math.abs(leftHeight - rightHeight);
    }

    private int  getBalance(boolean f){
        //получить высоту и баланс
        int leftHeight = 0;
        int rightHeight = 0;

        if(leftChild!=null)
            leftHeight = leftChild.height;
        if(rightChild!=null)
            rightHeight = rightChild.height;

        height = Math.max(leftHeight,rightHeight)+1;

        return leftHeight - rightHeight;
    }

    private void rebalance(){

        leftCheck();
        rightCheck();

    }

    private void rightCheck(){
        int b = 0;
        int r = 0;
        int c = 0;
        int l = 0;

        b = getHeight(leftChild);
        r = getHeight(rightChild);

        if(b!=0) {
            c = getHeight(leftChild.rightChild);
            l = getHeight(leftChild.leftChild);
        }

        /*System.out.println("---");
        System.out.println(key);
        System.out.println(leftChild.height+" "+rightChild.height+" "+leftChild.leftChild.height+" "+leftChild.rightChild.height);
        System.out.println(b+" "+r+" "+c+" "+l);
        System.out.println("---");*/

        if((b-r)==2&&c<=l) {
            rightRotate();
        }

        if((b-r)==2&&c>l) {
            bigRightRotate();
        }
    }

    private void leftCheck(){
        int b = 0;
        int r = 0;
        int c = 0;
        int l = 0;






        if(leftChild!=null)
            b = leftChild.height;

        if(rightChild!=null){
            r = rightChild.height;
            if(rightChild.rightChild!=null)
                c = rightChild.rightChild.height;
            if(rightChild.leftChild!=null)
                l = rightChild.leftChild.height;
        }


        //System.out.println(b+" "+r+" "+l+" "+c);

        if((b-r==-2)&&l<=c)
           leftRotate();
        if((b-r==-2)&&l>c)
            bigLeftRotate();
    }

    private void rightRotate(){
        //System.out.println("need right rotate "+key);

        AVLTree a = null;
        AVLTree b = null;
        AVLTree l = null;
        AVLTree c = null;

        a = cloneThis();
        b = leftChild;

        if(b!=null){
            l = b.leftChild;
            c = b.rightChild;
        }

        leftChild = null;
        rightChild = null;

        key = b.key;
        leftChild = l;
        leftChild.parent = this;

        if(c!=null)
            c.parent = a;
        a.parent = this;
        a.leftChild = c;
        if(a.rightChild!=null)
            a.rightChild.parent = a;

        rightChild = a;

        leftChild.getBalance();
        rightChild.getBalance();
        getBalance();
        recountParent();
    }

    private void leftRotate(){
        //System.out.println("need left rotate "+key);

        AVLTree A = cloneThis();
        AVLTree R = rightChild;
        AVLTree RR = null;
        AVLTree RL = null;
        if(R!=null) {
            RL = R.leftChild;
            RR = R.rightChild;
        }

        key = R.key;
        if(RR!=null) {
            RR.parent = this;
            rightChild = RR;
        }

        A.parent = this;
        A.rightChild = null;
        if(RL!=null){
            RL.parent = A;
            A.rightChild = RL;
        }
        if(A.leftChild!=null) {
            A.leftChild.parent = A;
        }

        leftChild = A;

        leftChild.getBalance();
        rightChild.getBalance();


        getBalance();
        recountParent();
    }

    private void bigRightRotate(){
        //System.out.println("need big right rotate "+key);

        AVLTree a = cloneThis();
        AVLTree b = leftChild;
        AVLTree c = b.rightChild;

        //System.out.println(a.key+" "+b.key+" "+c.key);

        AVLTree m = c.leftChild;
        AVLTree n = c.rightChild;

        //System.out.println(m+" "+n);

        if(n!=null)
            n.parent = a;
        a.rightChild = rightChild;
        if(a.rightChild!=null)
            a.rightChild.parent = a;
        a.leftChild = n;
        a.parent = this;

        if(m!=null)
            m.parent = b;
        b.rightChild = m;
        b.parent = this;

        key = c.key;
        leftChild = b;
        rightChild = a;

        a.getBalance();
        b.getBalance();
        getBalance();
        recountParent();
    }

    private void bigLeftRotate(){
       // System.out.println("need big left rotate "+key);
        AVLTree a = cloneThis();
        AVLTree b = rightChild;
        AVLTree c = b.leftChild;

        AVLTree l = leftChild;
        AVLTree m = c.leftChild;
        AVLTree n = c.rightChild;


        key = c.key;

        a.rightChild = m;
        a.leftChild = l;
        b.leftChild = n;

        leftChild = a;
        rightChild = b;

        leftChild.parent = this;
        rightChild.parent = this;

        if(leftChild!=null){
            if(leftChild.leftChild!=null)
                leftChild.leftChild.parent = leftChild;
            if(leftChild.rightChild!=null)
                leftChild.rightChild.parent = leftChild;

        }

        if(rightChild!=null){
            if(rightChild.leftChild!=null)
                rightChild.leftChild.parent = rightChild;
            if(rightChild.rightChild!=null)
                rightChild.rightChild.parent = rightChild;

        }






        a.getBalance();
        b.getBalance();
        getBalance();
        recountParent();


    }

    private AVLTree getRoot(){
        AVLTree root;
        if(parent!=null)
            root = parent.getRoot();
        else
            root = this;

        return root;
    }

    private int getHeight(AVLTree t){
        try{
            return t.height;
        }catch (NullPointerException e){
            return 0;
        }
    }

    void print(){

        printThis();
        if (leftChild != null)
            leftChild.print();
        if (rightChild != null)
            rightChild.print();

        if(parent == null)
            System.out.print("\n");
    }

    private void printThis(){

        String l="-";
        String r="-";
        String p = "-";

        if(leftChild!=null)
            l = String.valueOf(leftChild.key);
        if(rightChild!=null)
            r = String.valueOf(rightChild.key);
        if(parent!=null)
            p = String.valueOf(parent.key);

        System.out.print("["+p+"<"+key+" h= "+height+":"+l+" "+r+"]");
    }
}

class RBTRee{
    static boolean isEmpty = true;
    static boolean canSetUncheck;
    static boolean unCheck; //for debug
    boolean isLeaf;
    RBTRee parent;
    boolean isBlack; //true == black, false == red;
    //boolean red = false;
    //boolean black = false;
    int key;
    RBTRee leftChild;
    RBTRee rightChild;

    public void setUnCheck(){

        if(canSetUncheck)
            unCheck = !unCheck;
        else {
            System.out.println("дерево не для отладки!!");
        }
    }

    protected RBTRee clone(){
        RBTRee n = new RBTRee(key);
        n.leftChild = leftChild;
        n.rightChild = rightChild;
        n.parent = parent;
        if(leftChild!=null)
            n.leftChild.parent = n;
        if(rightChild!=null)
            n.rightChild.parent = n;
        //n.red = red;
        //n.black = black;
        n.isBlack = isBlack;

        return n;
    }

    //constructors
    public RBTRee(){
    }

    //for debug
    public RBTRee(boolean canSetUncheck){
        if(canSetUncheck)
            RBTRee.canSetUncheck = unCheck = true;
    }

    public RBTRee(int x){
        this.key = x;
        setRed();
    }

    private RBTRee getRoot(){
        if(parent == null)
            return this;
        else {
            return parent.getRoot();
        }
    }

    public void add(int x,boolean isBlack){
        if(canSetUncheck) {
            RBTRee t = new RBTRee(x);

            if (isBlack)
                t.setBlack();
            else
                t.setRed();

            insert(t);
        }else {
            System.out.println("Дерево не для отладки!!");
        }
    }

    //add methods
    public void add(int x){

        RBTRee n = new RBTRee(x);
        n.addLeafs();

        insert(n);
    }

    public void add(int[] x){
        for(int o:x) {
            add(o);
        }
    }

    private void addLeafs(){
        addLeftLeaf();
        addRightLeaf();

    }

    private void addLeftLeaf(){
        leftChild = new RBTRee();
        leftChild.isLeaf = true;
        leftChild.setBlack();
        leftChild.parent = this;
    }

    private void addRightLeaf(){
        rightChild = new RBTRee();
        rightChild.isLeaf = true;
        rightChild.setBlack();
        rightChild.parent = this;
    }

    private void insert(RBTRee tree){
        if(isEmpty) {
            isEmpty = false;
            key = tree.key;
            leftChild = tree.leftChild;
            rightChild = tree.rightChild;
            checkBalance();
            return;
        }
        if(tree.key < key){
            if(leftChild == null || leftChild.isLeaf) {
                tree.parent = this;
                leftChild = tree;
                tree.checkBalance();

            }
            else
                leftChild.insert(tree);
        }
        if(tree.key > key){
            if(rightChild == null || rightChild.isLeaf) {
                tree.parent = this;
                rightChild = tree;
                tree.checkBalance();

            }
            else
                rightChild.insert(tree);
        }

    }

    //find methods
    public RBTRee find(int x){
        if(key == x)
            return this;
        else {
            if(x < key) {
                if(!leftChild.isLeaf)
                    return leftChild.find(x);
                else
                    return null;
            }
            else {
                if(!rightChild.isLeaf)
                    return rightChild.find(x);
                else
                    return null;
            }
        }
    }

    public RBTRee findMin(){
        if(leftChild.isLeaf)
            return this;
        else
            return leftChild.findMin();
    }

    public RBTRee findMax(){
        if(rightChild.isLeaf)
            return this;
        else
            return rightChild.findMax();
    }

    //remove methods
    public void remove(int x){
        RBTRee del = find(x);
        if(del!=null) {
            System.out.println("нашли "+del.colorKey());
            del.remove();
        }else {
            System.out.println("not found key "+x);
        }
    }

    private void remove(){
        if(!leftChild.isLeaf&&!rightChild.isLeaf){
            RBTRee min = rightChild.findMin();
            System.out.println("узел с 2 детьми, нашли на замену "+min.key);
            key = min.key;
            min.removeWithOneChild();

        }else
            removeWithOneChild();
    }

    private void removeWithOneChild(){
        RBTRee child = leftChild.isLeaf ? rightChild:leftChild;
        System.out.println("удаляем узел с одним ребёнком "+colorKey()+" "+child);

        boolean oldColor = isBlack;

        replaceBy(child);

        //balance
        System.out.println(colorKey());
        if(oldColor) {
            System.out.println("НУЖНО ОТБАЛАНСИРОВАТЬ");
            if (!child.isBlack) {
                System.out.println("ребёнок красный, перекрашиваем себя в черный");
                setBlack();
            }
            else
                deleteCase1();
        }

    }

    private void replaceBy(RBTRee node){
        System.out.println("заменяем на "+node.colorKey()+" "+isLeaf);
        key = node.key;
        isLeaf = node.isLeaf;
        isBlack = node.isBlack;

        if(node == node.parent.leftChild){
            leftChild = node.leftChild;
            if(leftChild!=null)
                leftChild.parent = this;
            }
        else {
            rightChild = node.rightChild;
            if(rightChild!=null)
                rightChild.parent = this;
        }



    }

    //get relatives methods
    private RBTRee getUncle(){
        if(parent!=null&&parent.parent!=null){
            if(parent == parent.parent.leftChild)
                return parent.parent.rightChild;
            else
                return parent.parent.leftChild;

        }

        return null;
    }

    private RBTRee getGrandFather(){
        if(parent!=null)
            return parent.parent;

        return null;
    }

    private RBTRee getBrother(){
        if(this == parent.leftChild)
            return parent.rightChild;
        else
            return parent.leftChild;
    }

    //set isBlack methods
    private void setBlack(){
        isBlack = true;
    }

    private void setRed(){
        isBlack = false;
    }

    //balance methods
    private void checkBalance(){
        //System.out.print(" -> 1");

        if (parent == null)
            setBlack();
        else
            if(!unCheck)
                checkBalance2();

        //System.out.print("\n");

    }

    private void checkBalance2(){
        //System.out.print(" -> 2");
        if(parent.isBlack)
            return;
        else
            checkBalance3();
    }

    private void checkBalance3(){
        //System.out.print(" -> 3");
        RBTRee uncle = getUncle();
        RBTRee grand = getGrandFather();

        if(uncle!=null&&!uncle.isBlack){

            parent.setBlack();
            uncle.setBlack();
            grand.setRed();
            grand.checkBalance();
        }else {
            checkBalance4();
        }
    }

    private void checkBalance4(){
        //System.out.print(" -> 4");
        RBTRee grand = getGrandFather();
        RBTRee newN = this;


        if(this == parent.rightChild&&parent == grand.leftChild){
            newN = parent.leftRotate();
        }
        else {
            if(this == parent.leftChild&&parent == grand.rightChild) {
                newN = parent.rightRotate();
            }
        }

        //System.out.println("!"+newN.key);
        newN.parent.setBlack();
        newN.getGrandFather().setRed();

        //getRoot().printTree();

        newN.checkBalance5();


    }

    private void checkBalance5(){
        //System.out.print(" -> 5");
        RBTRee grand = getGrandFather();

        if(this == parent.leftChild&&parent==grand.leftChild)
            grand.rightRotate();
        else
            grand.leftRotate();

    }

    private void deleteCase1(){
        System.out.println("delete case 1 "+key);
        if(parent!=null)
            deleteCase2();

            
    }

    private void deleteCase2() {
        RBTRee brother = getBrother();
        System.out.println("delete case 2 "+key+" "+brother.colorKey());

        RBTRee n = this;

        if(!brother.isBlack){
            System.out.println("1");
            parent.setRed();
            brother.setBlack();

            if(this == parent.leftChild)
                n = parent.leftRotate();
            else
                n = parent.rightRotate();
        }

        if(n!=null)
            n.deleteCase3();

    }

    private void deleteCase3() {
        System.out.println("delete case 3 "+key);
        RBTRee b = getBrother();//brother

        if(parent.isBlack&&b.isBlack&&b.leftChild.isBlack&&b.rightChild.isBlack) {
            System.out.println("!");
            b.setRed();
            parent.deleteCase1();
        } else
            deleteCase4();

    }

    private void deleteCase4() {
        System.out.println("delete case 4 "+key);
        RBTRee b = getBrother();//brother

        if(!parent.isBlack&&b.isBlack&&b.leftChild.isBlack&&b.rightChild.isBlack){
            System.out.println("!");
            b.setRed();
            parent.setBlack();
        }else
            deleteCase5();
    }

    private void deleteCase5() {
        System.out.println("delete case 5 "+key);
        RBTRee b = getBrother();//brother

        if(b.isBlack){
            if(this == parent.leftChild&&
               b.rightChild.isBlack&&
               !b.leftChild.isBlack
            ){
                System.out.println("!");
                b.setRed();
                b.leftChild.setBlack();
                b.rightRotate();
            }
        }else if(this==parent.rightChild&&
                    b.leftChild.isBlack&&
                    !b.rightChild.isBlack
        ){
            System.out.println("!");
            b.setRed();
            b.rightChild.setBlack();
            b.leftRotate();
        }
        deleteCase6();

    }

    private void deleteCase6() {
        System.out.println("delete case 6 "+key);
        RBTRee b = getBrother();//brother

        b.isBlack = parent.isBlack;
        parent.setBlack();

        if(this == parent.leftChild){
            System.out.println("!");
            b.rightChild.setBlack();
            parent.leftRotate();
        }else {
            System.out.println("!");
            b.leftChild.setBlack();
            parent.rightRotate();
        }
    }

    //rotate methods, вызывать дя выще стоящего узла
    private RBTRee rightRotate() {
        //System.out.print(" rotate to the right " + key);

        RBTRee newR = clone();
        RBTRee newL = null;
        RBTRee newRL = null;

        newR.leftChild = null;

        if(newR!=null) {
            newR.parent = this;
        }

        if(leftChild!=null) {
            newL = leftChild.leftChild;
            newRL = leftChild.rightChild;

            if(newL!=null){
                newL.parent = this;
            }

            if(newRL!=null) {
                newRL.parent = newR;
                newR.leftChild = newRL;
            }
        }

        isBlack = leftChild.isBlack;
        key = leftChild.key;

        leftChild = newL;
        rightChild = newR;


        return newR;
    }

    private RBTRee leftRotate() {
        //System.out.print(" rotate to the left " + key);

        RBTRee newL = clone();
        RBTRee newR = null;
        RBTRee newLR = null;

        newL.rightChild = null;
        if(newL!=null)
            newL.parent = this;

        if(rightChild!=null){
            newR = rightChild.rightChild;
            newLR = rightChild.leftChild;

            if(newR!=null) {
                newR.parent = this;
            }

            if (newLR != null) {
                newLR.parent = newL;
                newL.rightChild = newLR;
            }
        }

        isBlack = rightChild.isBlack;
        key = rightChild.key;

        leftChild = newL;
        rightChild = newR;

        return newL;

    }

    //print tree methods
    public void printTree(){
        printThis();
        if(!leftChild.isLeaf)
            leftChild.printTree();
        if(!rightChild.isLeaf)
            rightChild.printTree();

        if(parent==null)
            System.out.print("\n");
    }

    private void printThis(){


        String l = "-";
        String r = "-";
        String p = "-";

        if (!leftChild.isLeaf)
            l = leftChild.colorKey();
        else
            l = "#";

        if (!rightChild.isLeaf)
            r = rightChild.colorKey();
        else
            r = "#";

        if (parent != null)
            p = parent.colorKey();

        //System.out.print("[" + p + "<" + getColorString() + " " + key + ":" + l + "|" + r + "] ");
        System.out.print("[" + p + "<" + colorKey() + ":" + l + "|" + r + "] ");

    }

    private String getColorString(){

        String color="null";

        if(!isBlack)
            color = "\u001B[31m r";
        else
            color =  "\u001B[30m b";

        return color+"\u001B[0m";
    }

    private String colorKey(){

        String color="null";

        if(!isBlack)
            color = "\u001B[31m"+key;
        else
            color =  "\u001B[30m"+key;

        return color+"\u001B[0m";
    }
}

class BTree2 {
    BTree2 parent;
    int T;
    int minSize;
    int size;
    boolean isEmpty = true;
    boolean debug;
    int level;

    ArrayList<Integer> keys;
    ArrayList<BTree2> children;


    public BTree2(int t) {
        if(t < 2){
            try {
                throw new Exception("in B-Tree T cannot be less than 2, use binary tree");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
            this.T = t;

        level = 1;

        if(isEmpty) {
            minSize = 1;
            isEmpty = false;
        }else
            minSize = T-1;

        size = 2 * T - 1;

        keys = new ArrayList<>();
        children = new ArrayList<>();
    }

    //add methods
    public void add(int x){
        insert(x);

    }

    public void add(int[] x){
        for(int o : x){
            //System.out.println("Add "+o);
            add(o);
            //print();
            //System.out.println();
            propertiesCheck();

        }



}

    //insert methods
    private void insert(int x) {
        //вставка
        //случай 1: узел не заполнен полностью
        //System.out.println("> "+keys.size()+" "+(children ==null ? children: children.size()));
        if(keys.size()<size/*||(children!=null&&children.size() == keys.size()+1)*/) {
            //случай 1.1: узел не заполнен полностью, и все ссылки пустые те к примеру 2 ключа и все дети листы , 3 ключи и все дети листы
            if(!isFullChild()) {
                insertCase1(x);
            }
            //случай 1.2 узел не заполнен полностью, но есть не пустые ссылки те к примеру 1 ключи 2 ребёнка(не листы), 2 ключа и 3 ребёнка(не листы)
            else {
                //нужно найти в какого ребёнка засунуть этот ключ
                //System.out.println("case 2");
                insertCase2(x);
            }
        }
        //случай 2: узел заполнен -> нужно разделить
        else
            insertCase3(x);

    }

    private void insertCase1(int x){
        //массив дожлен быть отсортирован
        keys.add(getNewIndex(x),x);
    }

    private void insertCase2(int x){
        //перебираем детей и ищем куда засунуть ключ

        int index = 0;
        for(int i = 0; i < keys.size(); i++){
            if(x<keys.get(i)){
                index = i;
                break;
            }
            if(i==keys.size()-1)
                if(x>keys.get(i)){
                    index = i+1;
                    break;
                }
        }

        children.get(index).insert(x);

    }

    private void insertCase3(int x) {
        //сначала нужно разделить
        splitСase1(x);
    }

    private int getIndexInParentArray(BTree2 t){
        return children.indexOf(t);
    }

    private void splitСase1(int x){
        //System.out.println("-split-");
        //System.out.println("add  "+x);
        //System.out.println("узел выглядит так ");

        //print();
        //новые поддеревья
        BTree2 leftTree = new BTree2(T);
        BTree2 rightTree = new BTree2(T);

        //делим ключи
        ArrayList<Integer> left = getLeftKey();
        ArrayList<Integer> right = getRightKey();
        ArrayList<Integer> middle = getMiddle();

        //делим детей если есть
        ArrayList<BTree2> leftChildren =  getLeftChildren(leftTree);
        ArrayList<BTree2> rightChildren = getRightChildren(rightTree);

        //создаём новые поддеревья, у детей переделываем ссылки на родителя(соответствующее поддерево)
        leftTree.keys = left;
        leftTree.children = leftChildren;

        rightTree.keys = right;
        rightTree.children = rightChildren;
        rightTree.level = level;
        leftTree.level = level;
        //нужно определить в какое поддерево вставлять новый елемент
        //System.out.println("middle "+middle.get(0));
        if(x<middle.get(0))
            leftTree.insert(x);
        else
            rightTree.insert(x);

        //для отладки, выводим получившиеся поддеревья
        //leftTree.print();
        //rightTree.print();



        merge(leftTree,middle,rightTree);

        //System.out.println("--------");
    }

    private void merge(BTree2 leftTree, ArrayList<Integer> middle, BTree2 rightTree) {
        int index = 0;


        //System.out.println("> "+parent);
        if(parent == null) {

            //System.out.println("merge case 1");
            keys = middle;
            leftTree.parent = this;
            rightTree.parent = this;
            rightTree.level = level;
            leftTree.level = level;
            level++;
            children = new ArrayList<>(size);
            children.add(leftTree);
            children.add(rightTree);



        }
        else {
            index = parent.getIndexInParentArray(this);
            leftTree.parent = parent;
            rightTree.parent = parent;

            if(!parent.isFull()){
                //System.out.println("merge case 2");

                parent.children.set(index, leftTree);
                parent.children.add(index + 1, rightTree);
                rightTree.level = parent.level - 1;
                leftTree.level = parent.level - 1;

                parent.insert(middle.get(0));
            }else {
                //System.out.println("merge case 3");
                //leftTree.print();
                //rightTree.print();
                parent.children.set(index, leftTree);
                parent.children.add(index + 1, rightTree);
                rightTree.level = parent.level - 1;
                leftTree.level = parent.level - 1;
                parent.splitСase2(middle.get(0),index);
            }
        }

    }

    private void splitСase2(int downMiddle,int index) {
        //System.out.println("split case 2");

        ArrayList<Integer> middle = getMiddle();

        keys.remove(middle.get(0));

        ArrayList<Integer> left = getKey(0,keys.size()/2);
        ArrayList<Integer> right = getKey(keys.size()/2,keys.size());

        BTree2 leftTree = new BTree2(T);
        BTree2 rightTree = new BTree2(T);



        leftTree.keys = left;
        rightTree.keys = right;

        if(downMiddle<middle.get(0)) {
            leftTree.insert(downMiddle);
        }
        else {
            rightTree.insert(downMiddle);
        }

        ArrayList<BTree2> leftChildren = getChildren(0,leftTree.keys.size()+1,leftTree);
        ArrayList<BTree2> rightChildren = getChildren(leftTree.keys.size()+1,children.size(),rightTree);

        leftTree.children = leftChildren;
        rightTree.children = rightChildren;

        //System.out.println(downMiddle);
        //print();
        //merge
        if(parent == null) {
            //System.out.println("parent is null");
            keys = null;
            keys = new ArrayList<>(size);
            keys.add(middle.get(0));


            level++;
            leftTree.level = rightTree.level = level -1;
            children = new ArrayList<>();

            leftTree.parent = this;
            rightTree.parent = this;


            children.add(leftTree);
            children.add(rightTree);


        }else {
            //leftTree.level++;
            //rightTree.level++;
            merge(leftTree,middle,rightTree);
        }



    }

    //find methods
    public int find(int x){

        for(int i = 0 ; i < keys.size() ; i++){
            if(keys.get(i) == x)
                return keys.get(i);
            else {
                if(x < keys.get(i))
                    return children.get(i).find(x);

                //если в последнем
                if(i == keys.size() - 1)
                    if (x > keys.get(i))
                        //ошибка, неохота выяснять почему
                        try {
                            return children.get(i + 1).find(x);
                        }catch (Exception e){}

            }
        }

        return -1;
    }

    //remove methods
    public void remove(Integer x){

        BTree2 removeIn = findForRemove(x);


        //System.out.println(removeIn);
        if(removeIn != null) {
            //System.out.println(">> "+removeIn.children.size());
            if(removeIn.children.size()==0) {
                //System.out.println("is leaf");
                BTree2 pR = removeIn.parent;
                //if node is root with null child
                if(removeIn == getRoot() && removeIn.keys.contains(x)){
                    //System.out.println("remove case 0");
                    keys.remove(x);
                    return;
                }


                //if parent contains 2 children and children is leaf
                if (pR.children.size() == 2  && pR.children.get(0).isLeafOneLength() && pR.children.get(1).isLeafOneLength())
                    pR.removeCase1(x);
                else
                    //if node is leaf
                    if (removeIn.children.size() == 0)
                        removeIn.removeCase2(x);
            }else {
                removeIn.removeCase3(x);
            }


        }
    }

    public void remove(ArrayList<Integer> x){
        for(Integer o : x){
            //System.out.println("удалить "+o);
            remove(o);
            //sprint2();
            //System.out.println(propertiesCheck());
            if(!propertiesCheck()) {
                getRoot().print2();
                System.out.println("1111111111");
                System.out.println(propertiesCheck());
                try {
                    Thread.sleep(12312312);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void removeCase3(Integer x) {
        //System.out.println("удалить не в листе");
        //System.out.println("remove case 3");
        //System.out.println();
        int index = keys.indexOf(x);
        //System.out.println(index);

        BTree2 left = children.get(index);

        //System.out.println("left first "+left.keys.get(0));
        //System.out.println("size "+left.keys.size());
        //System.out.println("T = "+T);

        if(left.keys.size()>=T){
            //System.out.println("> cases 1");
            //int prev = left.keys.get(left.keys.size()-1);
            int prev = left.findMax();
            //System.out.println(prev);
            keys.set(index,prev);
            //System.out.println(">> "+prev);
            left.remove(prev);
        }else {
            BTree2 right = children.get(index + 1);
            if(right.keys.size()>=T){
                //System.out.println("> cases 2");
                int prev = right.findMin();
                //System.out.println(" min = "+prev);
                keys.set(index,prev);
                //System.out.println(">> "+prev);
                right.remove(prev);
            }
            else {
                if(left.keys.size() == T -1 && right.keys.size() == T -1){
                    //System.out.println(left.keys.get(0)+" "+right.keys.get(0));
                    //System.out.println("> case 3");
                    keys.remove(x);
                    left.keys.add(left.getNewIndex(x),x);
                    for(int o : right.keys)
                        left.keys.add(o);

                    for(BTree2 o : right.children){
                        o.parent = left;
                        left.children.add(o);
                    }
                    children.remove(right);

                    if(parent == null && keys.size() == 0){
                        //System.out.println("!");
                        childrenLevelMinus();
                        if(children.size()== 1) {
                            BTree2 c = children.get(0);
                            keys = c.keys;
                            children = new ArrayList<>();
                            for(BTree2 t : c.children){
                                t.parent = this;
                                children.add(t);
                            }

                            //System.out.println("!dasdsadasdasdsad");
                            //getRoot().print2();
                            //System.out.println("dsadsadsa213123");

                            remove(x);
                            return;

                        }

                    }



                    left.remove(x);
                }
            }
        }
    }

    private int findMax(){
        if(children.size() == 0){
            return keys.get(keys.size() - 1);
        }else {
            return children.get(children.size() - 1).findMax();
        }
    }

    private int findMin(){
        if(children.size() == 0){
            return keys.get(0);
        }else {
            return children.get(0).findMin();
        }
    }

    private void removeCase1(Integer x){
        //System.out.println("remove case 1");
        BTree2 c1 = children.get(0);
        BTree2 c2 = children.get(1);
        children = new ArrayList<>();

        insert(c1.keys.get(0));
        insert(c2.keys.get(0));

        level = 1;

        keys.remove(x);
    }

    private void removeCase2(Integer x){
        //System.out.println("remove case 2");
        //System.out.println(keys.size() + 1+" "+T);

        if(keys.size()> T - 1)
            keys.remove(x);
        else {
           BTree2 leftBrother = getLeftBrother();
           BTree2 rightBrother = getRightBrother();
           BTree2 suitable = null;

           //System.out.println(leftBrother);
           //System.out.println(rightBrother)

           //ищем соседа содержащего больше T - 1 ключей
           if(leftBrother!=null && leftBrother.keys.size() > T - 1)
               suitable = leftBrother;
           else
               if(rightBrother!=null && rightBrother.keys.size() > T -1)
                   suitable = rightBrother;

           //если такой сосед есть
           if(suitable!=null){
               //System.out.println("есть подходящий сосед");
               int k1 = 0;
               int k2 = 0;

               if(suitable == leftBrother) {
                   //System.out.println("левый");
                   k1 = suitable.keys.get(suitable.keys.size() - 1);
                   k2 = parent.keys.get(parent.children.indexOf(this)-1);

               }
               if(suitable == rightBrother) {
                   //System.out.println("правый");
                   k1 = suitable.keys.get(0);
                   k2 = parent.keys.get(parent.children.indexOf(this));
               }


               //System.out.println("k1 = "+k1+" k2 = "+k2);
               //for(int o : parent.keys)
                //   System.out.print(o+" ");
               //System.out.println();

               parent.keys.set(parent.keys.indexOf(k2),k1);
               //parent.keys.set(parent.getNewIndex(k1),k1);

               insert(k2);
               suitable.keys.remove((Integer)k1);

           }
           //если нет
           else {
               //System.out.println("нет");
               //System.out.println("нет такого");
               //System.out.println("!!!!");
               int k1 = 0;

               if(leftBrother!=null) {
                   //System.out.println("мержим с левый");
                   k1 = parent.keys.get(parent.children.indexOf(this) - 1);
                   merge(leftBrother);
               }
               else
                   if(rightBrother!=null) {
                       //System.out.println("мержим с правым");
                       k1 = parent.keys.get(parent.children.indexOf(this));
                       merge(rightBrother);
                   }

               //System.out.println("k1 = "+k1);
               parent.keys.remove((Integer) k1);
                   if(parent.keys.size() == 0){
                       if(parent.children.size() == 1){
                           parent.keys = keys;
                           parent.children = new ArrayList<>();
                       }
                   }
               insert(k1);
           }


           keys.remove(x);


        }
    }

    private void replace(int whatReplace, int changingFor){
        keys.set(keys.indexOf(whatReplace),changingFor);
    }

    private void removeCase4() {
        //System.out.println("remove case 4");
        //for(Integer k : keys)
        //    System.out.print(k+" ");
        //System.out.println();

        BTree2 lB = getLeftBrother();
        BTree2 rB = getRightBrother();

        int mid = parent.keys.get(parent.keys.size()/2);
        //System.out.println("> "+mid);
        int brotherX;



        //если хотя бы у одного брата из двух keys.size >= T
        //System.out.println(lB+ " "+rB);
        if(lB != null) {
            //System.out.println("!!");
            //System.out.println(" l s "+lB.keys.size());
            //System.out.println(lB.keys.get(0));
            if (lB.keys.size() >= T) {

                int i = parent.getIndexInParentArray(this);
                if(i != 0)
                    i--;

                mid = parent.keys.get(i);

                //System.out.println(i);

                //System.out.println("!!! 1");
                brotherX = lB.keys.get(lB.keys.size() - 1);
                //System.out.println(">> "+mid+" "+brotherX);

                parent.replace(mid,brotherX);
                lB.keys.remove((Integer) brotherX);
                keys.add(getNewIndex(mid),mid);

                BTree2 newChild = lB.children.get(lB.children.size() - 1);
                newChild.parent = this;
                lB.children.remove(newChild);
                children.add(0,newChild);



                return;
            }
        }

            if( rB != null){
                //System.out.println(" r s "+rB.keys.size());
                //System.out.println("!");
                if(rB.keys.size() >= T){
                    int i = parent.getIndexInParentArray(this);
                    //if(i != parent.keys.size() + 1)
                        //i--;

                    //System.out.println(i);

                    mid = parent.keys.get(i);
                    //System.out.println("!!! 2");

                    brotherX = rB.keys.get(0);
                    //System.out.println(">> "+mid+" "+brotherX);
                    //System.out.println(">> "+mid+" "+brotherX);

                    parent.replace(mid,brotherX);
                    rB.keys.remove((Integer) brotherX);
                    keys.add(getNewIndex(mid),mid);

                    BTree2 newChild = rB.children.get(0);
                    newChild.parent = this;
                    rB.children.remove(newChild);
                    children.add(newChild);

                    //System.out.println("dasdasda!");
                    //getRoot().print2();
                    //System.out.println("das-das-ds-a");


                    return;
                }
            }


        //если у убоих keys.size <= T - 1
        if(lB != null && lB.keys.size() <= T - 1){
            if(parent.keys.size() == 1)
                mid = parent.keys.get(0);
            else {
                //mid = parent.keys.get(parent.getNewIndex(keys.get(0)));
                int i = parent.getIndexInParentArray(this);
                if(i != 0)
                    i--;

                mid = parent.keys.get(i);
            }
            //System.out.println("!!!!! 1 "+mid);
            keys.add(getNewIndex(mid),mid);

            for(Integer o : lB.keys)
                keys.add(getNewIndex(o),o);

            parent.children.remove(lB);
            parent.keys.remove((Integer) mid);

            for(int i = 0 ; i < lB.children.size() ; i++) {
                BTree2 newChild = lB.children.get(i);
                newChild.parent = this;
                children.add(i, newChild);
            }

            //System.out.println("dasdasda!");
            //getRoot().print2();
            //System.out.println("das-das-ds-a");

        }else {
            if(rB != null && rB.keys.size() <= T -1){
                if(parent.keys.size() == 1)
                    mid = parent.keys.get(0);
                else {
                    int i = parent.getIndexInParentArray(this);
                    //if(i != parent.keys.size() + 1)
                    //i--;

                    //System.out.println(i);

                    mid = parent.keys.get(i);
                }
                //System.out.println("!!!!! 2 "+mid);
                keys.add(getNewIndex(mid),mid);

                for(Integer o : rB.keys)
                    keys.add(getNewIndex(o),o);

                parent.children.remove(rB);
                parent.keys.remove((Integer) mid);

                for(int i = 0 ; i < rB.children.size() ; i++) {
                    BTree2 newChild = rB.children.get(i);
                    newChild.parent = this;
                    children.add(rB.children.get(i));
                }

            }
        }

        if(parent.keys.size() == 0){
            parent.keys = keys;
            parent.children = children;

            for(BTree2 o : parent.children)
                o.parent = parent;

            getRoot().childrenLevelMinus();
        }


    }

    private BTree2 getRoot(){
        if(parent==null)
            return this;
        else
            return parent.getRoot();
    }

    private void childrenLevelMinus(){
        if(level != 1)
            level--;

        for(BTree2 o : children)
            o.childrenLevelMinus();

    }

    private void merge(BTree2 brother){
        for(Integer o : brother.keys)
            insert(o);

        parent.children.remove(brother);


    }

    private BTree2 getLeftBrother(){
        int i = parent.children.indexOf(this);

        if(i != 0)
            return parent.children.get(i-1);

        return null;
    }

    private BTree2 getRightBrother(){
        int i = parent.children.indexOf(this);

        try {
            if (i != parent.children.size())
                return parent.children.get(i + 1);
        }catch (Exception e){}

        return null;
    }

    private BTree2 findForRemove(int x){

        if(parent != null && children.size() != 0){
            if(keys.size() == T - 1) {
                removeCase4();
                return getRoot().findForRemove(x);
            }

        }

        if(keys.contains(x))
            return this;
        else {
            for(int i = 0 ; i < keys.size() ; i++){
                if(x < keys.get(i))
                    return children.get(i).findForRemove(x);
                if(i == keys.size() - 1)
                    if(x > keys.get(i))
                        return children.get(i + 1).findForRemove(x);


            }
        }

        return null;
    }

    private boolean isLeafOneLength(){
        return children.size() == 0 && keys.size() == 1;
    }
    //проверка свойств B дерева
    public boolean propertiesCheck() {
        boolean check = propertiesCheck1();
        if(!check)
            Main.error++;
        return check;
    }

    private boolean propertiesCheck2() {
        return true;

    }

    private boolean propertiesCheck1(){
        boolean check = false;

        if(parent == null && keys.size() == 0 && children.size() ==0)
            return true;

        if(minSize<=keys.size()&&keys.size()<=size) {
            check = true;
        }else {
            check = false;
            return check;
        }

        if(children.size()!=0) {
            if (keys.size() + 1 == children.size())
                check = true;
            else {
                check = false;
                return check;
            }

            for (BTree2 o : children) {
                check = o.propertiesCheck();
                if (!check)
                    break;
            }
        }

        return check;
    }

    //key/children
    private ArrayList<Integer> getLeftKey(){
        return new ArrayList<Integer>(keys.subList(0, keys.size()/2));
    }

    private ArrayList<Integer> getRightKey(){
        return new ArrayList<Integer>(keys.subList(keys.size()/2+1,keys.size()));
    }

    private ArrayList<Integer> getKey(int lD,int rD){
        //получить ключи в диапозоне
        return new ArrayList<Integer>(keys.subList(lD,rD));
    }

    private ArrayList<Integer> getMiddle(){
        ArrayList<Integer> a = new ArrayList<>();
        a.add(keys.get(T-1));
        return a;
    }

    private ArrayList<BTree2> getLeftChildren(BTree2 parent){
        if(children!=null) {

            ArrayList<BTree2> subL = new ArrayList<BTree2>(children.subList(0, children.size() / 2));
            for(BTree2 o : subL)
                o.parent = parent;

            return subL;
        }
        else
            return null;
    }

    private ArrayList<BTree2> getRightChildren(BTree2 parent){
        if(children!=null) {
            ArrayList<BTree2> subR = new ArrayList<BTree2>(children.subList(children.size() / 2, children.size()));

            for(BTree2 o : subR)
                o.parent = parent;

            return subR;
        }
        else
            return null;
    }

    private ArrayList<BTree2> getChildren(int lD,int rD,BTree2 newParent){
        if(children!=null){
            ArrayList<BTree2> subChildren = new ArrayList<BTree2>(children.subList(lD,rD));

            for(BTree2 o : subChildren)
                o.parent = newParent;

            return subChildren;

        }else
            return null;
    }

    //for insert
    private int getNewIndex(int x){

        int newIndex = keys.size();

        if(!keys.contains(x))
        for (int i = keys.size() - 1; (i >= 0 && keys.get(i) > x); i--) {
            newIndex = i;
        }else{
            newIndex = keys.indexOf(x);
        }

        return newIndex;
    }

    private boolean isFullChild(){
        //если все возможные ссылки заполнены
        if(children==null)
            return false;
        else {
            return keys.size() + 1 == children.size();
        }

    }

    private boolean isFull(){
        //заполнено все что можно
        /*System.out.println(keys.size()+" "+size);
        for(Integer o : keys)
            System.out.println(o);

        print();*/
        return keys.size() == size;
    }

    //print methods
    static HashMap<Integer,ArrayList<BTree2>> nodes = new HashMap<>();
    public void print2() {
        int size = level;


        if(parent == null)
            for(int i = 1 ; i <= size ; i++)
                nodes.put(i,new ArrayList<>());


        add(level,this);
        if(children.size() != 0)
            for(BTree2 o : children)
                o.print2();

            BTree2 prevP =null;
        if(parent == null)
            for(int i = size ; i >= 1 ; i--){
                ArrayList<BTree2> x = nodes.get(i);
                System.out.print("\u001B[31m[\u001B[0m");
                for(BTree2 o : x) {


                    //t = "\u001B[31m" + t.substring(t.length() - 3) + "\u001B[0m";
                    if(prevP !=null&& o != x.get(0))
                        if(prevP != o.parent) {
                            System.out.print("\u001B[31m] \u001B[0m");
                            System.out.print("\u001B[31m[\u001B[0m");
                        }



                    System.out.print("(");
                    for (Integer k : o.keys) {
                        System.out.print(k);
                        if(k != o.keys.get(o.keys.size()-1))
                            System.out.print(" ");

                    }

                    prevP = o.parent;
                    System.out.print(")");


                }
                System.out.print("\u001B[31m]\u001B[0m");
                System.out.println();
            }




    }

    private static void add(int level,BTree2 x){
        nodes.get(level).add(x);
    }

    public void print(){
        System.out.print("( "+level+" :");
        for(int o : keys)
            System.out.print(o+"  ");
        System.out.print(")");

        for(BTree2 o : children)
            o.print();


    }


}

