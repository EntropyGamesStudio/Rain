package com.EntropyGamesStudio.rain.graphics;

public class Screen {
	
	private int width, height;
	public int[] pixels;
	
	public Screen(int width, int height){
		this.width = width;
		this.width = height;
		pixels = new int[width * height];
	}

}
