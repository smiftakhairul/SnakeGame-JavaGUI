package SnakeGame;

import javax.swing.*;

import java.awt.*;

public class Frame extends JFrame {
	
	public Frame()
	{
		add(new Board());
		
		setTitle("Snake Game");
		pack();
		
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}

}
