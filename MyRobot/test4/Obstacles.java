
// sim's width is 20(-5,15)->42 nodes, heigt is 10(-5,5)->22 nodes
/**
 robot is (-4.5, 0) in the array is (0.5, 5)*2 => [1, 10]

 left-down corner is (-5.0, -5.0), in the array is [0, 0]
 right-up corner is (15.0, 5.0), in the array is [20, 10]*2 =>[40, 20]
 so (x,y) in the array is [(x+5)*2, (y+5)*2]
 =========
 for example
 obstacle is in (0,0), radius<0.5, then
 in the array its index is [10, 10]
 it occupy x:(-0.5, 0.5), y:(-0.5, 0.5)
 which means it occupy the array's [9, 9] [9, 10] [10, 9] [10, 10] etc
 thus obstacle in (x, y) occupy 9 units in the array
 [x,y] [x,y+1] [x,y-1]
 [x-1,y] [x-1,y-1] [x-1, y+1]
 [x+1,y] [x+1, y+1] [x+1, y-1]

 occupied is 0, available is 1 in the array
 then it can go to the 0s,
 etc.

 tips:

 0.8 -1.8 0.6
 0.8 / 0.5 = 1.6 floor=> 2   * 0.5 = 1
 0.3 / 0.5 = 0.6 floor => 1 *0.5 = 0.5
 0.1 / 0.5 = 0.2 floor => 1 * 0.5 = 0.5
 6     */

import  java.lang.*;
import  java.util.ArrayList;
import java.util.List;
import  java.lang.Math;
import java.util.Stack;

public class Obstacles {
    Stack<Node> path;
    public static void main(String[] argc)
    {
        Obstacles ob = new Obstacles();
        
    }

    public Obstacles() {
        path = new Stack<>();
        int[][] isObstacle = new int[42][22];
        List<Node> array = new ArrayList<Node>();
        Node tmp = new Node();
        int x = 0;
        int y = 0;

        System.out.println("init Obstacle:");
        // initialize store
        for(int i = 0; i < 42; i++) {
            for(int j = 0; j < 22; j++) {
                // 0 means the [x,y] is available, quite safe, 1 means near obstacle 2 means is obstacle
                isObstacle[i][j] = 0;
                // System.out.print(isObstacle[i][j] + " ");
            }
            // System.out.println();
        }

        array.add(new Node(-0.5, 3.5));
        array.add(new Node(-1.5, 0));
        array.add(new Node(-2.5, 2));
        array.add(new Node(-4, 4));
        array.add(new Node(1, 1.2));

        array.add(new Node(0.8, -1));
        array.add(new Node(-0.5, -2.8));
        array.add(new Node(-1.5, -4));
        array.add(new Node(-3, -2.3));

        array.add(new Node(3.5, 2.8));
        array.add(new Node(5, 0.8));
        array.add(new Node(6.5, 3.5));
        array.add(new Node(8, 1));


        array.add(new Node(3, 0.0));
        array.add(new Node(3, -1.5));
        array.add(new Node(3, -2.0));
        array.add(new Node(3, -2.5));

        array.add(new Node(6, -3));
        array.add(new Node(7, -1.2));
        array.add(new Node(6.5, -0.9));
        array.add(new Node(7.5, -1.5));

        // System.out.println("=========add nodes==========");
        for(int i = 0; i < array.size(); i++) {
            // System.out.println("x: " + array.get(i).x + " y: " + array.get(i).y);
            tmp = exchangeTo(format(array.get(i)));
            x = (int) tmp.x;
            y = (int) tmp.y;
            if(x == 0 && y == 0 ){
                // [y,x]
                isObstacle[x][y] = 2;
            }else if(y == 0 && x >= 1){
                isObstacle[x-1][y]= 1;
                isObstacle[x][y]= 2;
                isObstacle[x+1][y] = 1;

                isObstacle[x-1][y+1] = 1;
                isObstacle[x][y+1] = 1;
                isObstacle[x+1][y+1] = 1;
            } else if (x == 0 && y >= 1){
                isObstacle[x][y-1] = 1;
                isObstacle[x][y] = 2;
                isObstacle[x][y+1] = 1;

                isObstacle[x + 1][y - 1] = 1;
                isObstacle[x + 1][y] = 1;
                isObstacle[x + 1][y + 1] = 1;
            }else {
                isObstacle[x][y - 1] = 1;
                isObstacle[x][y] = 2;
                isObstacle[x] [y + 1]= 1;

                isObstacle[x + 1][y - 1] = 1;
                isObstacle[x + 1] [y]= 1;
                isObstacle[x + 1][y + 1] = 1;

                isObstacle[x - 1][y - 1] = 1;
                isObstacle[x - 1][y] = 1;
                isObstacle[x - 1][y + 1] = 1;
            }
        }
        path = findPath(isObstacle, 2, 10, 30, 10);
    }
    // return Stack<Node> path for Obstacles.java
    // public Stack<Node> getPath(){
    //   // Stack<Node> tmp = new Stack<>();
    //   // tmp = path;
    //   return ob.path;
    // }

    // ceil to 0.5
    public static Node format(Node node) {
        Node result = new Node();
        double x;
        double y;
        x = Math.ceil( node.x / 0.5) * 0.5;
        y = Math.ceil( node.y / 0.5) * 0.5;
        result.x = x;
        result.y = y;
        // System.out.println("node x,y" + node.x + node.y);
        // System.out.println("x,y" + x + y);
        return result;
    }
    // (x,y) + 5 ) * 2
    public static Node exchangeTo(Node node) {
        Node result = new Node();
        result.x = (node.x + 5) * 2;
        result.y = (node.y + 5) * 2;

        // result.x = isObstacle's indexX
        return result;
    }

    // isObstacle's indexX,indexY back to (double, double) in Sim
    public static Node exchangeBack(int indexX, int indexY) {
        Node result = new Node();
        // old node

        result.x = (double)indexX / 2.0 - 5.0;
        result.y = (double)indexY / 2.0 - 5.0;

        System.out.println("result: " + result.x + result.y);
        return result;
    }

    private static final int[][] DIREC = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1},
            {1, 0}, {1, -1}, {0, -1}, {-1, -1}};

    public static Stack<Node> findPath(int[][] map, int x1, int y1, int x2, int y2) {
        List<Position> openList = new ArrayList<Position>();
        List<Position> closeList = new ArrayList<Position>();
        boolean findFlag = false;
        // strict mode, 1 means strict, 2 means so so
        double isObstacle = 2.0;
        Position termPos = null;
        // start
        Position startPos = new Position(x1, y1, calcH(x1, y1, x2, y2));
        openList.add(startPos);
        do {
            // find smallest F in the open list as currPos
            Position currentPos = openList.get(0);
            for (int i = 0; i < openList.size(); i++) {
                if (currentPos.F > openList.get(i).F) {
                    currentPos = openList.get(i);
                }
            }
            // add currPos in close list,and delete it from openlist
            closeList.add(currentPos);
            openList.remove(currentPos);

            //遍历当前点对应的8个相邻点
            for (int i = 0; i < DIREC.length; i++) {
                int tmpX = currentPos.row + DIREC[i][0];
                int tmpY = currentPos.col + DIREC[i][1];
                if (tmpX < 0 || tmpX >= map.length || tmpY < 0 || tmpY >= map[0].length) {
                    continue;
                }
                //create
                Position tmpPos = new Position(tmpX, tmpY, calcH(tmpX, tmpY, x2, y2), currentPos);
                //map 1 is obstacle or already in the close list
                if (map[tmpX][tmpY] == 1 || closeList.contains(tmpPos)) {
                    continue;
                }
                //if not in open list, add
                if (!openList.contains(tmpPos)) {
                    openList.add(tmpPos);
                } else {
                    // if in the open list, cal G to contrast,if better, change father Position as currPos,and re-cal the F G H value
                    Position prePos = null;
                    for (Position pos : openList) {
                        if (pos.row == tmpX && pos.col == tmpY) {
                            prePos = pos;
                            break;
                        }
                    }
                    if (tmpPos.G < prePos.G) {
                        prePos.setFaPos(currentPos);
                    }
                }
            }
            // if target in the open list, found
            for (Position tpos : openList) {
                if (tpos.row == x2 && tpos.col == y2) {
                    termPos = tpos;
                    findFlag = true;
                    break;
                }
            }

        } while(openList.size() != 0);

        if(!findFlag) {
            System.out.println("no valid path!");
            return null;
        }

        Stack<Node> resStack = new Stack<Node>();
        // Node format = new Node();
        if (termPos != null) {
            // format = exchangeBack(termPos.row, termPos.col);
            resStack.push(new Node(termPos.row, termPos.col));
            while(termPos.fa != null) {
                termPos = termPos.fa;
                // format = exchangeBack(termPos.row, termPos.col);
                resStack.push(new Node(termPos.row, termPos.col));
            }
        }
//        StringBuilder sb = new StringBuilder();
//        while (!resStack.empty()) {
//            sb.append("(" + resStack.pop().intX + "," + resStack.pop().intY + ")");
//            if (!resStack.empty()) {
//                sb.append(" -> ");
//            }
//        }
//        System.out.println(sb.toString());
        return resStack;
    }

    /**
     * cal Position's H value
     * @param x
     * @param y
     * @param tx target x
     * @param ty target y
     * @return
     */
    private static int calcH(int x, int y, int tx, int ty) {
        int diff = Math.abs(x - tx) + Math.abs(y - ty);
        return diff * 10;
    }

}


class Node{
    public int intX;
    public int intY;

    public double x;
    public double y;

    public Node() {
//        this.x = 0;
//        this.y = 0;
    }

    public Node(double _x, double _y){
        this.x = _x;
        this.y = _y;
    }

    public Node(int _x, int _y){
        this.intX = _x;
        this.intY = _y;
    }

}



 class Position {
    public int F;
    public int G;
    public int H;
    public Position fa;
    public int row;
    public int col;

    public Position() {
    }

    public Position(int row, int col, int H) {
        this(row, col, H, null);
    }

    public Position(int row, int col, int H, Position pos) {
        this.H = H;
        this.row = row;
        this.col = col;
        this.fa = pos;
        this.G = calcG();
        this.F = G + H;
    }

    /**
     * cal the G from Position to startPos
     * @return
     */
    private int calcG() {
        if (fa == null) return 0;
        if (fa.row != this.row && fa.col !=  this.col) {
            return 14 + fa.G;
        }
        return 10 + fa.G;
    }

    public void setFaPos(Position pos) {
        this.fa = pos;
        this.G = calcG();
        this.F = G + H;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position pos = (Position) obj;
        return this.row == pos.row && this.col == pos.col;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

}