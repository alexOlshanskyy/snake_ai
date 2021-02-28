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
	public static Game g;

	public App(int mode){
		recreateSnake(false, mode);
	}
	public void recreateSnake(boolean b, int mode) {

		// set style depending on mode
		if (mode == 2) {
			width = 6;
			height = 5;
			Game.speed = 100;
			colorChange = (float)0.02;
			colorRange = 34;
		} else if (mode == 1){
			width = 18;
			height = 12;
			Game.speed = 30;
			colorRange = 225;
			colorChange = (float)0.003;
		} else if (mode == 0) {
			width = 18;
			height = 12;
			Game.speed = 200;
			colorRange = 225;
			colorChange = (float)0.003;
		} else if (mode == 3) {
			width = 7;
			height = 5;
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
		getContentPane().removeAll(); // clear previous game
		getContentPane().setLayout(new BorderLayout());
		JPanel m = new JPanel();  // main pane
		m.setLayout(new BorderLayout());
		m.setBorder(null);

		grid.removeAll(); // clear previous game
		grid.setFocusable(true);
		grid.setLayout(new GridLayout(height,width,0,0));




		// Add squares to the grid
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				grid.add(Grid.get(i).get(j).square);

			}
		}

		JPanel bottomGrid = new JPanel();
		bottomGrid.setLayout(new GridLayout(1,5,10,0));

		//Create buttons

		JButton normalGameButton = new JButton("Normal Game");
		normalGameButton.setBackground(Color.white);
		normalGameButton.addActionListener(e -> {
			recreateSnake(true, 0);
		});

		JButton hamiltonianButton = new JButton("Hamiltonian Shortcut");
		hamiltonianButton.setBackground(Color.white);
		hamiltonianButton.addActionListener(e -> {
			recreateSnake(true, 1);
		});

		JButton hamiltonianButtonS = new JButton("Hamiltonian Random");
		hamiltonianButtonS.setBackground(Color.white);
		hamiltonianButtonS.addActionListener(e -> {
			recreateSnake(true, 2);
		});

		JButton pathfinding = new JButton("Pathfinding");
		pathfinding.setBackground(Color.white);
		pathfinding.addActionListener(e -> {
			recreateSnake(true, 3);
		});

		// create text areas for score and result
		JTextArea scoreTextArea = new JTextArea();
		scoreTextArea.setText("Score: 0");
		scoreTextArea.setSize(20, 100);
		scoreTextArea.setEditable(false);
		Font font = new Font("Georgia", Font.BOLD, 20);
		scoreTextArea.setFont(font);
		scoreTextArea.setBackground(Color.getHSBColor(249, (float)0, (float)0.93));  // had to hardcode the color

		JTextArea resultTextArea = new JTextArea();
		resultTextArea.setSize(20, 100);
		resultTextArea.setEditable(false);
		Font font1 = new Font("Georgia", Font.BOLD, 20);
		resultTextArea.setFont(font1);
		resultTextArea.setBackground(Color.getHSBColor(249, (float)0, (float)0.93));

		//add elements in order
		bottomGrid.add(resultTextArea);
		bottomGrid.add(normalGameButton);
		bottomGrid.add(hamiltonianButton);
		bottomGrid.add(hamiltonianButtonS);
		bottomGrid.add(pathfinding);
		bottomGrid.add(scoreTextArea);

		// add two grids to main pane
		m.add(grid, BorderLayout.CENTER);
		m.add(bottomGrid, BorderLayout.PAGE_END);
		setContentPane(m);

		// initial position of the snake
		Coordinate position = new Coordinate(0,0);


		if (b) {
			g.stop(); // stop the thread
		}
		g = new Game(position, mode, scoreTextArea, resultTextArea);
		Game.stop = false;
		g.start();  // start new game
		if (mode == 0){
			grid.addKeyListener(mv);  // only mode that allows keyboard input to move the snake
		}

		this.setTitle("Snake AI");
		this.setSize(1200,800);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// helper class that is used to color the boxes on the grid
	public static class Box {
		ArrayList<Color> C =new ArrayList<Color>();
		int color;
		public SquarePanel square;
		public Box(int col){
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
