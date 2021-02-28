# Snake AI

## Methods
This project uses two techniques to play snake. 
One is path finding with shortest/longest paths using A* algorithm. 
The other is Hamiltonian cycles, which finds a cycle around the grid and follows it. 
### Pathfinding
Pathfinding follows this strategy:
1. Tries to find the shortes path to the food, such that after it eats the food it can access 80% of the available grid.
2. If no shortes path to fod is found, it tries to find the longes path such that after it eats the food it can access 80% of the available grid.
3. If no path is found in 1 and 2 it tries to find any longest path to the food.

### Hamiltonian cycle
In Hamiltonian mode snake follows the generated Hamiltonian cycle, which is slow but guaranties that snake will win. 
In the shortcut version of Hamiltonian cycle, the cycle is followed normally, but if adjacent cell on the grid is the food the cycle is cut. 
Not every cut is safe because after the cut snake can hit its tail, so algorithm checks if it is safe to cut to the food.  

### How To Run
Clone the repo and run Main.java from IDE.

#### Demo: https://www.youtube.com/watch?v=TEEFZ9yv-WQ
