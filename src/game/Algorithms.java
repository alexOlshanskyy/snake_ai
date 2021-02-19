package game;

import java.util.*;
import java.util.zip.CheckedOutputStream;

public class Algorithms {
    private static Random r = new Random();

    private static class PQPair implements Comparable<PQPair> {
        Coordinate c;
        int value;
        ArrayList<Coordinate> positions;
        PQPair(Coordinate c, int value, ArrayList<Coordinate> positions) {
            this.c = c;
            this.value = value;
            this.positions = positions;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PQPair pqPair = (PQPair) o;
            return value == pqPair.value &&
                    c.equals(pqPair.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c, value);
        }

        @Override
        public int compareTo(PQPair other) {
            return this.value - other.value;
        }
    }

    public static void main(String[] args) {
        ArrayList<Coordinate> positions = new ArrayList<>();
        positions.add(new Coordinate(3,1));
        positions.add(new Coordinate(2,1));
        //positions.add(new Coordinate(0,1));
        positions.add(new Coordinate(1,1));
        positions.add(new Coordinate(0,1));
        positions.add(new Coordinate(0,0));
        Coordinate head = new Coordinate(0,0);
        Coordinate food = new Coordinate(3,3);
        aStartSearch(positions, head, food, 4, 4);
        //System.out.println(Arrays.toString(hamiltonianCycle(6, 5, true))); // should result in even number otherwise hamiltonian cycle is not possible
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

    public static int[] aStartSearch(ArrayList<Coordinate> positions, Coordinate headSnakePos, Coordinate foodPosition, int x, int y) {
        if (positions.isEmpty()) {
            return new int[0];
        }
        PriorityQueue<PQPair> pq = new PriorityQueue<>();
        HashSet<Coordinate> visited = new HashSet<>();
        HashMap<Coordinate, Coordinate> map = new HashMap<>();
        PQPair p = new PQPair(headSnakePos, 0, positions);
        pq.add(p);
        map.put(headSnakePos, new Coordinate(-1,-1));
        while (!pq.isEmpty()) {
            PQPair current = pq.poll();
            if (visited.contains(current.c)) {
                continue;
            }
            if (current.c.equals(foodPosition)) {
                // check if can get to many cells
                break;
            }
            visited.add(current.c);
            ArrayList<Coordinate> neighbors = getNeighbors(current.c);
            for (int i = 0; i < neighbors.size(); i++) {
                if (current.positions.contains(neighbors.get(i)) || outOfBounds(x, y, neighbors.get(i).getX(), neighbors.get(i).getY())) {
                    neighbors.remove(neighbors.get(i));
                    i--;
                }
            }
            for (Coordinate cor : neighbors) {
                if (!visited.contains(cor)) {
                    int move = move(current.c.getX(), current.c.getY(), cor.getX(), cor.getY());
                    ArrayList<Coordinate> newPositions = new ArrayList<>(current.positions);
                    deleteTail(newPositions);
                    addNewHead(move, newPositions);
                    pq.add(new PQPair(cor, current.value+1 + distance(cor, foodPosition), newPositions));
                    map.put(cor, current.c);
                }

            }

        }
        Coordinate temp;
        ArrayList<Coordinate> result = new ArrayList<>();
        result.add(foodPosition);
        temp = map.get(foodPosition);
        while (temp != null && !temp.equals(new Coordinate(-1,-1))) {
            result.add(temp);
            temp = map.get(temp);
        }
        int[] ret = new int[result.size()-1];
        for (int i = 0; i < result.size()-1; i++) {
            Coordinate c1 = result.get(i);
            Coordinate c2 = result.get(i+1);
            ret[result.size() - 2 - i] = move(c2.getX(), c2.getY(), c1.getX(), c1.getY());
        }
        System.out.println(Arrays.toString(ret));
        return ret;
    }

    private static int distance(Coordinate c1, Coordinate c2) {
        return c1.getManhattanDistanceTo(c2);
    }

    private static int[] findH(Coordinate current, int currLen, int x, int y, int[] res, Stack<Coordinate> coordinates, boolean random) {
        if (currLen == (x*y)) {
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

    private static void addNewHead(int dir, ArrayList<Coordinate> positions){
        Coordinate head = positions.get(positions.size()-1);
        switch(dir){
            case 4:
                positions.add(new Coordinate(head.getX(), head.getY()+1));
                break;
            case 3:
                positions.add(new Coordinate(head.getX(), head.getY()-1));
                break;
            case 2:
                positions.add(new Coordinate(head.getX()-1, head.getY()));
                break;
            case 1:
                positions.add(new Coordinate(head.getX()+1, head.getY()));
        }
    }


    private static void deleteTail(ArrayList<Coordinate> positions){
        int size = positions.size()-1;
        for(int i = positions.size()-1;i>=0;i--){
            if(size==0){
                positions.remove(i);
            }
            else{
                size--;
            }
        }
    }

}
