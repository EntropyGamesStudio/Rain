package com.EntropyGamesStudio.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.EntropyGamesStudio.rain.graphics.Screen;

public class Game extends Canvas implements Runnable{ 
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = 168; // width / 16 * 9;
	public static int scale = 3;
	public static String title = "Rain";
	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	private Screen screen;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); //Creates an image.
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); //Allows access to the image.
	
	public Game(){
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		
		frame = new JFrame(); 
		screen = new Screen(width, height);
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
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; 
		double delta = 0;
		int frames = 0;
		int updates = 0;
		
		while (running == true){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println(updates + " ups, " + frames + "fps");
				frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps" );
				updates = 0;
				frames = 0;
			}
				
		}
		stop();
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
		
		screen.clear();
		screen.render();
		
		for (int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false); //Sets widow not to be re-sizable.
		game.frame.setTitle(Game.title); //Sets window title.
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null); //puts window in the centre of the screen.
		game.frame.setVisible(true);
		
		game.start();
	}
}
