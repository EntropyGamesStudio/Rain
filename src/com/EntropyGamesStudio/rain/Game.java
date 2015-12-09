package com.EntropyGamesStudio.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{ 
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = width / 16 * 9;
	public static int scale = 3;
	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	public Game(){
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		frame = new JFrame(); 
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		while (running == true){
			update();
			render();
		}
	}
	
	/*
	 * Updates Game Logic.
	 */
	public void update(){
	}
	
	/*
	 * Displays images on the screen.
	 */
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if (bs == null){
			createBufferStrategy(3);
			return;
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false); //Sets widow not to be re-sizable.
		game.frame.setTitle("Rain"); //Sets window title.
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null); //puts window in the centre of the screen.
		game.frame.setVisible(true);
		
		game.start();
	}
}
