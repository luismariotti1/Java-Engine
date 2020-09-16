package com.VortexGames.world;

import java.awt.image.BufferedImage;

import com.VortexGames.main.Game;

public class WallTile extends Tile{

	private BufferedImage style1 = Game.spritesheet.getSprite(16, 0, 16, 16);
	private BufferedImage style2 = Game.spritesheet.getSprite(16, 16, 16, 16);
	
	public WallTile(int x, int y, BufferedImage sprite, int type) {
		super(x, y, sprite);
		if(type == 1)
		{
			this.sprite = style1; 			
		}
		else if(type == 2)
		{
			this.sprite = style2;
		}
	}
}
