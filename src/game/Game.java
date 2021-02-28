package game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;


public class Game extends Thread {
	 public static ArrayList<ArrayList<App.Box>> Squares= new ArrayList<ArrayList<App.Box>>();
	 public Coordinate headSnakePos;
	 int sizeSnake = 3;
	 static long speed = 500;
	 public static int directionSnake;
	 private static int mode;
	 public static boolean stop = false;
	 private static JTextArea scoreText;
	 private static JTextArea resultText;
	 public static boolean allowedToMove;

	 ArrayList<Coordinate> positions = new ArrayList<>();
	 public Coordinate foodPosition;

	 Game(Coordinate positionStart, int mode, JTextArea textArea, JTextArea textArea2){
	 	reconstruct(positionStart, mode, textArea, textArea2);
	 }

	/**
	 *
	 * @param positionStart start position of snake
	 * @param mode is the mode of the game
	 * @param scoreText is the text that shows score
	 * @param resultText is the text that shows result of the game
	 */
	 public void reconstruct(Coordinate positionStart, int mode, JTextArea scoreText, JTextArea resultText) {
		 Game.mode = mode;
		 Squares= App.Grid;

		 headSnakePos = new Coordinate(positionStart.getX(),positionStart.getY());
		 directionSnake = 1;

		 Coordinate headPos = new Coordinate(headSnakePos.getX(),headSnakePos.getY());
		 positions.add(headPos);

		 foodPosition= new Coordinate(App.width-1, App.height-1);
		 spawnFood(foodPosition);
		 Game.scoreText = scoreText;
		 Game.resultText = resultText;
	 }

	/**
	 * runs the game
	 */
	 public void run() {
		 while(!stop){
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
		moveInterne(directionSnake);
		checkForCollision();
		moveExterne();
		deleteTail();
		pauser();
		while(!stop){
			int[] moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, false, false);
			if (moves.length == 0) {
				//No shortest path found
				moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, true, false);
			}
			if (moves.length == 0) {
				//No longest path found
				moves = Algorithms.aStartSearch(positions, headSnakePos, foodPosition, App.width, App.height, true, true);
			}
			if (moves.length == 0) {
				//No path found, RIP
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
		 while(!stop){
			 directionSnake = moves[index];
			 moveInterne(directionSnake);

			 // loop index around back to 0
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
	 	Algorithms.buildCords();
		int index = 0;
		while(!stop){
			int[] m = Algorithms.getShortcutMove(index, foodPosition, positions);

			// if there is a shortcut move
			if (m[0] != -1) {
				directionSnake = m[0];
				index = m[1];
			} else {
				directionSnake = moves[index];
				index++;
			}

			if (index >= moves.length){
				index= index % moves.length;
			}
			moveInterne(directionSnake);
			checkForCollision();
			moveExterne();
			deleteTail();
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
			 scoreText.setText("Score: " + sizeSnake);
			 foodPosition = getRandNotInSnake();
			 spawnFood(foodPosition);
		 }
		 for(int i = 0;i<=positions.size()-2;i++){
			 boolean biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY()==positions.get(i).getY();
			 if(biteItself || stop){
				 stopGame();
			 }
		 }
	 }
	 
	 //Stops The Game
	 private void stopGame(){
		 if (sizeSnake == (App.width*App.height)) {
			 resultText.setText("WON!");
		 } else if (!(mode == -1)){
			 resultText.setText("LOST!");
		 }
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
	 //this method updates the snake positions
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
	 // moves snake in game based on positions
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

	 // deletes tail of a snake
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
