package game;

import java.util.Objects;

public class Coordinate {
	@Override
	public String toString() {
		return "Coordinate{" +
				"x=" + x +
				", y=" + y +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coordinate that = (Coordinate) o;
		return x == that.x &&
				y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	private int x;
	private int y;
	  
	public Coordinate(int x, int y) {
	    this.x = x; 
	    this.y = y; 
	}
	public void update(int x, int y){
		    this.x = x; 
		    this.y = y; 
	}
	public int getX(){
		  return x;
	}
	public int getY(){
		  return y;
	}

	public int getManhattanDistanceTo(Coordinate c1) {
		return (Math.abs(this.getX() - c1.getX())) + (Math.abs(this.getY() + c1.getY()));
	}
} 