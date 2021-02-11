package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

 public class MoveListener extends KeyAdapter{
 	
 		public void keyPressed(KeyEvent e){
 		    switch(e.getKeyCode()){

				case 37:
					if(Game.directionSnake!=1)
						Game.directionSnake=2;
					break;
				case 38:
					if(Game.directionSnake!=4)
						Game.directionSnake=3;
					break;
				case 39:
					//if it's not the opposite direction
					if(Game.directionSnake!=2)
						Game.directionSnake=1;
					break;
				case 40:
					if(Game.directionSnake!=3)
						Game.directionSnake=4;
					break;
				default:
					break;
 		    }
 		}
 	
 }
