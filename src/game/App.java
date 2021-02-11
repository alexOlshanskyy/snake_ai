package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;


class App extends JFrame{
	public static int colorRange = 1380;
	public static float colorChange = (float)0.0005;
	public static ArrayList<ArrayList<Box>> Grid;
	public static int width = 45;
	public static int height = 30;
	public static JPanel grid = new JPanel();
	public static MoveListener mv = new MoveListener();
	public Game c;
	public App(int mode){
		recreateSnake(false, mode);
	}
	public void recreateSnake(boolean b, int mode) {
		if (mode == 2) {
			width = 6;
			height = 5;
			Game.speed = 100;
			colorChange = (float)0.03;
			colorRange = 32;
		} else if (mode == 1){
			width = 45;
			height = 30;
			Game.speed = 3;
		} else if (mode == 0) {
			width = 45;
			height = 30;
			Game.speed = 250;
		}
		Grid = new ArrayList<>();
		ArrayList<Box> data;

		for(int i=0;i<height;i++){
			data= new ArrayList<>();
			for(int j=0;j<width;j++){
				Box c = new Box(2);
				data.add(c);
			}
			Grid.add(data);
		}

		// Setting up the layout of the panel
		getContentPane().removeAll();
		getContentPane().setLayout(new BorderLayout());
		JPanel m = new JPanel();
		m.setLayout(new BorderLayout());
		m.setBorder(null);
		grid.removeAll();
		grid.setFocusable(true);
		grid.setLayout(new GridLayout(height,width,2,2));




		// Start & pauses all threads, then adds every square of each thread to the panel

		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				grid.add(Grid.get(i).get(j).square);

			}
		}


		JPanel grid2 = new JPanel();
		grid2.setLayout(new FlowLayout());
		JButton normalGameButton = new JButton("Normal Game");
		normalGameButton.setBackground(Color.white);
		normalGameButton.addActionListener(e -> {
			// do something
			recreateSnake(true, 0);
		});

		JButton hamiltonianButton = new JButton("Hamiltonian Solution 45X30");
		hamiltonianButton.setBackground(Color.white);
		hamiltonianButton.addActionListener(e -> {
			// do something
			recreateSnake(true, 1);
		});
		JButton hamiltonianButtonS = new JButton("Hamiltonian Solution 6X5");
		hamiltonianButtonS.setBackground(Color.white);
		hamiltonianButtonS.addActionListener(e -> {
			// do something
			recreateSnake(true, 2);
		});


		JButton button2 = new JButton("Click Me 2");
		grid2.add(normalGameButton);
		grid2.add(hamiltonianButton);
		grid2.add(hamiltonianButtonS);
		grid2.add(button2);
		m.add(grid, BorderLayout.CENTER);
		m.add(grid2, BorderLayout.PAGE_END);
		setContentPane(m);

		// initial position of the snake
		Coordinate position = new Coordinate(0,0);
		// passing this value to the controller
		if (b) {
			//Game.stop = true;
			c.stop();
		}
		c = new Game(position, mode);
		Game.stop = false;

		c.start();
		if (mode == 0){
			grid.addKeyListener(mv);
		}
		this.setTitle("Snake AI");
		this.setSize(1200,800);
		this.setVisible(true);
		//this.addKeyListener(mv);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public static class Box {
		ArrayList<Color> C =new ArrayList<Color>();
		int color;
		public SquarePanel square;
		public Box(int col){

			//Lets add the color to the arrayList
			C.add(Color.darkGray);
			C.add(Color.RED);
			C.add(Color.white);
			C.add(Color.BLACK);
			float h = 0;
			float s = 0;
			float a = 0;
			for (int i = 0; i < colorRange; i++){
				C.add(Color.getHSBColor(h, s, a));
				h += colorChange;
				s += colorChange;
				a += colorChange;


			}
			color=col;
			square = new SquarePanel(C.get(color));
			square.setSize(30,30);

		}
		public void setColor(int c){
			square.ChangeColor(C.get(c%colorRange));
		}
	}
}
