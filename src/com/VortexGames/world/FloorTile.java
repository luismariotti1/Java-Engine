package com.VortexGames.world;

import java.awt.image.BufferedImage;

import com.VortexGames.main.Game;


public class FloorTile extends Tile{

	private BufferedImage style1 = Game.spritesheet.getSprite(0, 0, 16, 16);
	private BufferedImage style2 = Game.spritesheet.getSprite(0, 16, 16, 16);

	public FloorTile(int x, int y, BufferedImage sprite, int type) {
		super(x, y, sprite);
		if(type == 1)
		{
			this.sprite = style1; 			
		}
		else if(type == 2)
		{
			this.sprite = style2;
		}
		// TODO Auto-generated constructor stub
	}
	
}
 