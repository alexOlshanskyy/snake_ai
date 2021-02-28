package game;

import javax.swing.*;
import java.awt.*;

public class Main {

	public static void main(String[] args) {
		App app = new App(-1);
		app.setTitle("Snake AI");
		app.setSize(1200,800);
		app.setVisible(true);
		app.setResizable(false);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setBackground(Color.white);
	}
}
