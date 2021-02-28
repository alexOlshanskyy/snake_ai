package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

 public class MoveListener extends KeyAdapter{
 	
 		public void keyPressed(KeyEvent e){
			// prevents moving twice in one tick which can lead to snake hitting itself. If snake was moving in 1
			// direction and then user quickly clicks 3 and then 2. At the end of the tick snake will move in 2
			// direction and hit itself
 			if (!Game.allowedToMove) {
 				return;
			}
 		    switch(e.getKeyCode()){

				case 37:
					if(Game.directionSnake!=1) {
						Game.directionSnake=2;
					}
					break;
				case 38:
					if(Game.directionSnake!=4) {
						Game.directionSnake=3;
					}
					break;
				case 39:
					if(Game.directionSnake!=2) {
						Game.directionSnake=1;
					}
					break;
				case 40:
					if(Game.directionSnake!=3) {
						Game.directionSnake=4;
					}
					break;
				default:
					break;
 		    }
 			// lock the move
 			Game.allowedToMove = false;
 		}
 }
