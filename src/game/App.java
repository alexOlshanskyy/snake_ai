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
	public static int width = 18;
	public static int height = 12;
	public static JPanel grid = new JPanel();
	public static MoveListener mv = new MoveListener();
	public static Game c;
	public App(int mode){
		recreateSnake(false, mode);
	}
	public void recreateSnake(boolean b, int mode) {
		if (mode == 2) {
			width = 6;
			height = 5;
			Game.speed = 100;
			colorChange = (float)0.02;
			colorRange = 34;
		} else if (mode == 1){
			width = 18;
			height = 12;
			Game.speed = 2;
			colorRange = 225;
			colorChange = (float)0.003;
		} else if (mode == 0) {
			width = 18;
			height = 12;
			Game.speed = 100;
			colorRange = 1380;
			colorChange = (float)0.0005;
		} else if (mode == 3) {
			width = 7;
			height = 6;
			Game.speed = 100;
			colorRange = 1380;
			colorChange = (float)0.01;
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
		grid.setLayout(new GridLayout(height,width,0,0));




		// Start & pauses all threads, then adds every square of each thread to the panel

		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				grid.add(Grid.get(i).get(j).square);

			}
		}


		JPanel grid2 = new JPanel();
		grid2.setLayout(new GridLayout(1,5,10,0));
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


		JButton pathfinding = new JButton("Pathfinding");
		pathfinding.setBackground(Color.white);
		pathfinding.addActionListener(e -> {
			// do something
			recreateSnake(true, 3);
		});

		JTextArea textArea = new JTextArea();
		textArea.setText("Score: 0");
		textArea.setSize(20, 100);
		Font font = new Font("Georgia", Font.BOLD, 20);
		textArea.setFont(font);
		textArea.setBackground(Color.getHSBColor(249, (float)0, (float)0.93));
		JTextArea textArea2 = new JTextArea();
		textArea2.setText("    ");
		textArea2.setSize(20, 100);
		textArea2.setEditable(false);
		Font font1 = new Font("Georgia", Font.BOLD, 20);
		textArea2.setFont(font1);
		textArea2.setBackground(Color.getHSBColor(249, (float)0, (float)0.93));
		grid2.add(textArea2);
		textArea2.setEditable(false);
		grid2.add(normalGameButton);
		grid2.add(hamiltonianButton);
		grid2.add(hamiltonianButtonS);
		grid2.add(pathfinding);
		grid2.add(textArea);

		m.add(grid, BorderLayout.CENTER);
		m.add(grid2, BorderLayout.PAGE_END);
		setContentPane(m);
		//setBackground(Color.white);

		// initial position of the snake
		Coordinate position = new Coordinate(0,0);
		// passing this value to the controller
		if (b) {
			//Game.stop = true;
			c.stop();
		}
		c = new Game(position, mode, textArea);
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
