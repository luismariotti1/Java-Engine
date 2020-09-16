package com.VortexGames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.VortexGames.main.Game;

public class Tile {

	// objetos estaticos do cenario do game
	public static BufferedImage TILE_FLOOR;
	public static BufferedImage TILE_WALL;
	//
	
	protected BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite)
	{ 
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g)
	{
		g.drawImage(sprite, x-Camera.x, y-Camera.y, null);
	}
}
