package game;

import javax.swing.*;
import java.util.ArrayList;


public class Game extends Thread {
	 public static ArrayList<ArrayList<App.Box>> Squares= new ArrayList<ArrayList<App.Box>>();
	 public Coordinate headSnakePos;
	 int sizeSnake = 3;
	 static long speed = 500;
	 public static int directionSnake;
	 private static int mode;
	 public static boolean stop = false;
	 private static JTextArea ta;
	 public static boolean allowedToMove;

	 ArrayList<Coordinate> positions = new ArrayList<Coordinate>();
	 public Coordinate foodPosition;
	 
	 //Constructor of ControlleurThread 
	 Game(Coordinate positionStart, int mode, JTextArea textArea){
	 	reconstruct(positionStart, mode, textArea);
	 }
	 public void reconstruct(Coordinate positionStart, int mode, JTextArea textArea) {
		 Game.mode = mode;
		 Squares= App.Grid;

		 headSnakePos = new Coordinate(positionStart.getX(),positionStart.getY());
		 directionSnake = 1;

		 Coordinate headPos = new Coordinate(headSnakePos.getX(),headSnakePos.getY());
		 positions.add(headPos);

		 foodPosition= new Coordinate(App.width-1, App.height-1);
		 spawnFood(foodPosition);
		 ta = textArea;
		 //ta.setText("Score: " + sizeSnake);
	 }

	 public void run() {
		 while(!stop){
		 	System.out.println("This is mode: " + mode);
		 	if (mode == -1){
		 		stopGame();
			} else if (mode == 0) {
		 		runNormalMode();
			} else if (mode == 1) {
				runHamiltonianShortcutMode(Algorithms.randomH);
			} else if (mode == 2) {
				runHamiltonianMode(Algorithms.hamiltonianCycle(App.width, App.height, true));
			} else if (mode == 3) {
		 		runPathFinding();
			}
		 }
	 }

	private void runPathFinding() {
	 	//boolean max = false;
		moveInterne(directionSnake);
		checkForCollision();
		moveExterne();
		deleteTail();
		pauser();
		while(!stop){
			int[] moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, false, false);
			if (moves.length == 0) {
				System.out.println("No shortest path found");
				moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, true, false);
			}
			if (moves.length == 0) {
				System.out.println("No longest path found");
				moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, true, true);
			}
			if (moves.length == 0) {
				System.out.println("No path found, RIP");
				runNormalMode();
			}
			for (int i = 0; i < moves.length; i++) {
				directionSnake = moves[i];
				moveInterne(directionSnake);
				checkForCollision();
				moveExterne();
				deleteTail();
				pauser();
			}
		}
	}

	 private void runNormalMode() {
		 while(!stop){
		 	allowedToMove = true;
		 	 int temp = directionSnake;
			 moveInterne(temp);
			 checkForCollision();
			 moveExterne();
			 deleteTail();
			 pauser();
		 }
	 }

	 private void runHamiltonianMode(int[] moves) {
	 	int index = 0;
	 	System.out.println("in here");
		 while(!stop){
			 directionSnake = moves[index];
			 moveInterne(directionSnake);
			 if (index == moves.length -1){
			 	index = 0;
			 } else {
			 	index++;
			 }

			 checkForCollision();
			 moveExterne();
			 deleteTail();
			 pauser();
		 }
	 }

	private void runHamiltonianShortcutMode(int[] moves) {
		int index = 0;
		while(!stop){
			directionSnake = moves[index];
			System.out.println(directionSnake);
			moveInterne(directionSnake);


			checkForCollision();
			moveExterne();
			deleteTail();
			if (index == moves.length -1){
				index=0;
			} else {
				index++;
			}
			pauser();
		}
	}

	 //delay between each move of the snake
	 private void pauser(){
		 try {
				sleep(speed);
		 } catch (InterruptedException e) {
				e.printStackTrace();
		 }
	 }
	 
	 //Checking if the snake bites itself or is eating
	 private void checkForCollision() {
		 Coordinate posCritique = positions.get(positions.size()-1);
		 boolean eatingFood = posCritique.getX()==foodPosition.getX() && posCritique.getY()==foodPosition.getY();
		 if(eatingFood){

			 sizeSnake=sizeSnake+1;
			 ta.setText("Score: " + sizeSnake);
			 System.out.println("got food");
			 foodPosition = getRandNotInSnake();
			 spawnFood(foodPosition);
		 }
		 for(int i = 0;i<=positions.size()-2;i++){
			 boolean biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY()==positions.get(i).getY();
			 if(biteItself || stop){
			 	System.out.println("bit");
				 stopGame();
			 }
		 }
	 }
	 
	 //Stops The Game
	 private void stopGame(){
		 System.out.println("end \n");
		 System.out.println(sizeSnake);
		 while(true){
			 pauser();
		 }
	 }
	 
	 //Put food in a position and displays it
	 private void spawnFood(Coordinate foodPositionIn){
	 	if (foodPositionIn.getX() == -1 && foodPositionIn.getY() == -1) {
	 		return;
		}
	 	Squares.get(foodPositionIn.getY()).get(foodPositionIn.getX()).setColor(1);
	 }

	 private Coordinate getRandNotInSnake(){
	 	if (sizeSnake == (App.width *App.height)) {
	 		return new Coordinate(-1, -1);
		}
		 Coordinate p ;
		 int ranX= 0 + (int)(Math.random()*App.width);
		 int ranY= 0 + (int)(Math.random()*App.height);
		 p=new Coordinate(ranX,ranY);
		 // make sure it is not on the snake
		 boolean found = false;
		 while (!found) {
		 	found = true;
			 for(int i = 0;i<=positions.size()-1;i++){
				 if(p.getX()==positions.get(i).getX() && p.getY()==positions.get(i).getY()){
					 ranX= 0 + (int)(Math.random()*App.width);
					 ranY= 0 + (int)(Math.random()*App.height);
					 p=new Coordinate(ranX,ranY);
					 i=0;
					 found = false;
					 break;
				 }
			 }
		 }
		 return p;
	 }

	 //1:right 2:left 3:top 4:bottom 0:nothing
	 private void moveInterne(int dir){
		 switch(dir){
		 	case 4:
		 		if (headSnakePos.getY()+1 == App.height){
					stopGame();
				} else {
					headSnakePos.update(headSnakePos.getX(), (headSnakePos.getY() + 1));
					positions.add(new Coordinate(headSnakePos.getX(), headSnakePos.getY()));
				}
		 		break;
		 	case 3:
		 		if(headSnakePos.getY()-1<0){
					stopGame();
		 		 }
		 		else{
				 	headSnakePos.update(headSnakePos.getX(),Math.abs(headSnakePos.getY()-1));
					positions.add(new Coordinate(headSnakePos.getX(),headSnakePos.getY()));
		 		}

		 		break;
		 	case 2:
		 		 if(headSnakePos.getX()-1<0){
					 stopGame();
		 		 }
		 		 else{
		 			 headSnakePos.update(Math.abs(headSnakePos.getX()-1),headSnakePos.getY());
					 positions.add(new Coordinate(headSnakePos.getX(),headSnakePos.getY()));
		 		 }
		 		 break;
		 	case 1:
				if (headSnakePos.getX()+1 == App.width){
					stopGame();
				} else {
				 	headSnakePos.update(Math.abs(headSnakePos.getX()+1),headSnakePos.getY());
				 	positions.add(new Coordinate(headSnakePos.getX(),headSnakePos.getY()));
				}
		 		 break;
		 }
	 }

	 private void moveExterne(){

		 for(int i = positions.size() - 1; i >= 0; i--){
			 int x = positions.get(i).getX();
			 int y = positions.get(i).getY();
			 if (i == positions.size() - 1){
				 Squares.get(y).get(x).setColor(3);
			 } else {
				 Squares.get(y).get(x).setColor(3 + positions.size() - i - 1);
			 }

			 
		 }
	 }

	 private void deleteTail(){
		 int size = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(size==0){
				 Coordinate t = positions.get(i);
				 Squares.get(t.getY()).get(t.getX()).setColor(2);
				 positions.remove(i);
			 }
			 else{
				 size--;
			 }
		 }
	 }
}
