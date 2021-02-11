package game;

import java.util.*;

public class Algorithms {
    private static Random r = new Random();
    public static void main(String[] args) {
        System.out.println(Arrays.toString(hamiltonianCycle(6, 5, true))); // should result in even number otherwise hamiltonian cycle is not possible
    }

    public static int[] hamiltonianCycle(int x, int y, boolean random){
        Random r = new Random();
        int[] resultMoves = new int[x*y];
        Coordinate current = new Coordinate(0,0);
        int currLen = 0;
        //resultMoves[0] = 1; // first move is right
        Stack<Coordinate> coordinates = new Stack<>();

        return findH(current, currLen, x, y, new int[x*y], coordinates, random);
    }

    private static int[] findH(Coordinate current, int currLen, int x, int y, int[] res, Stack<Coordinate> coordinates, boolean random) {
        if (currLen == (x*y)) {
            System.out.println("base");
            System.out.println(Arrays.toString(res));
            return res;
        } else {
            coordinates.push(current);
            ArrayList<Coordinate> neighbors = getNeighbors(current);
            if (currLen + 1 == (x*y) && neighbors.contains(new Coordinate(0,0))) {
                res[currLen] = move(current.getX(), current.getY(), 0, 0);
                return res;
            }
            ArrayList<Coordinate> valid = new ArrayList<>();
            for (Coordinate c : neighbors) {
                if (!outOfBounds(x,y,c.getX(),c.getY()) && !coordinates.contains(c)) {
                    valid.add(c);
                }
            }
            if (valid.size() != 0) {
                if (random){
                    Collections.shuffle(valid);
                }
                System.out.println("valid: " + valid.toString());
                for (Coordinate c : valid){
                    res[currLen] = move(current.getX(), current.getY(), c.getX(), c.getY());
                   // System.out.println("In loop");
                   // System.out.println(Arrays.toString(res));

                    res = findH(c, currLen + 1, x, y, res, coordinates, random);
                    boolean done = true;
                    for (int i = 0 ; i< res.length; i++){
                        if (res[i] == 0) {
                            done = false;
                        }
                    }
                    if (done) {
                      //  System.out.println("done");
                        return res;
                    }
                }
                res[currLen-1] = 0;
                coordinates.pop();


            } else  {

                // no move found
                res[--currLen] = 0;
                //System.out.println("else : " + Arrays.toString(res));
                coordinates.pop();
            }
        }
        return res;
        }
    // 1 right 2 left 3 top 4 bottom
    // return the move if it is, otherwise return -1
    private static int move(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            return 2;
        } else if (x2 > x1) {
            return 1;
        } else if (y1 < y2) {
            return 4;
        } else {
            return 3;
        }
    }

    private static boolean outOfBounds(int maxX, int maxY, int x, int y) {
        if (x < 0 || y < 0) {
            return true;
        }
        if (x >= maxX || y >= maxY) {
            return true;
        }

        return false;
    }

    private static ArrayList<Coordinate> getNeighbors(Coordinate curr) {
        ArrayList<Coordinate> res = new ArrayList<>();
        res.add(new Coordinate(curr.getX() + 1, curr.getY()));
        res.add(new Coordinate(curr.getX(), curr.getY() + 1));
        res.add(new Coordinate(curr.getX() - 1, curr.getY()));
        res.add(new Coordinate(curr.getX(), curr.getY() - 1));
        return res;
    }

}
