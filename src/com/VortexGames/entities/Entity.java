package com.VortexGames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.VortexGames.main.Game;
import com.VortexGames.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16,0,16,16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16,0,16,16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16,16,16,16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(3*16,2*16,16,16);
	public static BufferedImage GUN_LEFT = Game.spritesheet.getSprite(16*8,0, 16, 16);
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(16*7,0, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(16*8,16*1, 16, 16);
	public static BufferedImage PLAYER_FEEDBACK = Game.spritesheet.getSprite(16*7,16*1, 16, 16);
	public static BufferedImage WEAPON_FEEDBACK_RIGHT = Game.spritesheet.getSprite(16*9,16*0, 16, 16);
	public static BufferedImage WEAPON_FEEDBACK_LEFT = Game.spritesheet.getSprite(16*9,16*1, 16, 16);
	
	//atributos
	protected double x;
	protected double y;
	protected int width;
	protected int height;

	private BufferedImage sprite;
	
	private int maskx, masky, mwidth, mheight;
	//
	
	//construtor
	public Entity(int x, int y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}//
	
	public void setMask(int maskx, int masky, int mwidth, int mheight)
	{
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.height  = mheight;
	}
	
	//setters
	public void setX(int newX)
	{
		this.x = newX;
	}
	
	public void setY(int newY)
	{
		this.y = newY;
	}
	//
	
	//getters
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public double getXdouble() {
		return this.x;
	}
	
	public double getYdouble() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	//
	
	//logica
	public void tick()
	{
		
	}
	//
	
	public static boolean isColliding(Entity e1, Entity e2)
	{
		Rectangle e1Mask = new Rectangle(e1.getX()+e1.maskx, e1.getY()+e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX()+e2.maskx, e2.getY()+e2.masky, e2.mwidth, e2.mheight);
		return 	e1Mask.intersects(e2Mask);
	}
	
	//graficos
	public void render(Graphics g)
	{
		g.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
	}
	//
}
