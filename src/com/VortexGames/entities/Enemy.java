package com.VortexGames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.VortexGames.main.Game;
import com.VortexGames.world.Camera;
import com.VortexGames.world.World;

public class Enemy extends Entity{

	private double speed = 1;
	private int life = 6;
	
	private boolean isDamaged = false;
	
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 3;
	private int damageFrames = 10, damageCurrent = 0;
	
	private BufferedImage[] spritesFullLife;
	private BufferedImage[] spritesMediumLife;
	private BufferedImage[] spritesLowLife;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		spritesFullLife = new BufferedImage[4];
		spritesMediumLife = new BufferedImage[4];
		spritesLowLife = new BufferedImage[4];
		
		for(int i =0; i<4;i++)
		{
			this.spritesFullLife[i] = Game.spritesheet.getSprite(16*2+16*i, 16*4,16, 16);			
			this.spritesMediumLife[i] = Game.spritesheet.getSprite(16*2+16*i, 16*3,16, 16);			
			this.spritesLowLife[i] = Game.spritesheet.getSprite(16*2+16*i, 16*2,16, 16);			
		}
		// TODO Auto-generated constructor stub
	}

	public void tick()
	{
		//movimentEnemy();
		Damage();
		collidingBullet();
		movimentEnemy();
		
		frames++;
		if(frames == maxFrames)
		{
			frames = 0;
			index++;
			if(index>maxIndex)
			{
				index = 0;
			}
		}
		
		
		if(life<=0)
		{
			destroySelf();
			return;
		}
		
		if(isDamaged)
		{
			damageCurrent++;
			{
				if(this.damageFrames==damageCurrent)
				{
					damageCurrent=0;
					isDamaged=false;
				}
			}
		}
	}
	
	public void movimentEnemy()
	{
		if(this.isCollidingWithPlayer()==false) {
			
			if((int)x<Game.player.getX() && World.isFree((int)(this.getX()+speed),this.getY()))
			{
				if(!isColliding((int)(this.getX()+speed),this.getY()))
				x+=speed;
			}
			else if((int)x>Game.player.getX() && World.isFree((int)(this.getX()-speed),this.getY()))
			{
				if(!isColliding((int)(this.getX()-speed),this.getY()))
				x-=speed;
			}
			if((int)y<Game.player.getY() && World.isFree(this.getX(),(int)(this.getY()+speed)))
			{
				if(!isColliding(this.getX(),(int)(this.getY()+speed)))
				y+=speed;
			}
			else if((int)y>Game.player.getY() && World.isFree(this.getX(),(int)(this.getY()-speed)))
			{
				if(!isColliding(this.getX(),(int)(this.getY()-speed)))
				y-=speed;
			}
			
			}
	}
	
	public void Damage()
	{
		if(this.isCollidingWithPlayer()==true)
		{
			if(Game.rand.nextInt(100)<15)
			{
				Game.player.life-=Game.rand.nextInt(5);
				Game.player.isDamage=true;
			}
	
		}
	}
	
	public void destroySelf()
	{
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet()
	{
			for(int i =0; i<Game.bullets.size(); i++)
			{
				Entity e = Game.bullets.get(i);
				if(e instanceof BulletShoot)
				{
					if(Entity.isColliding(this, e))
					{
						isDamaged=true;
						life--;
						Game.bullets.remove(i);
						return;
					}
				}
			}
	} 
	
	public boolean isCollidingWithPlayer()
	{
		Rectangle enemyCurrent = new Rectangle(this.getX(),this.getY(),World.TILE_SIZE,World.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return enemyCurrent.intersects(player);
		
	}

	public boolean isColliding(int xnext, int ynext)
	{
		Rectangle enemyCurrent = new Rectangle(xnext,ynext,World.TILE_SIZE,World.TILE_SIZE);
		for(int i=0; i<Game.enemies.size(); i++)
		{
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX(),e.getY(),World.TILE_SIZE,World.TILE_SIZE);
			if(enemyCurrent.intersects(targetEnemy))
			{
				return true;						
			}
		}
		return false;
	}
	
	public void render(Graphics g)
	{
		if(!isDamaged)
		{	
			if(life > 4)
			{
				g.drawImage(this.spritesFullLife[index], this.getX()-Camera.x,this.getY()-Camera.y,null);				
			}
			if(life>2 && life<=4)
			{
				g.drawImage(this.spritesMediumLife[index], this.getX()-Camera.x,this.getY()-Camera.y,null);				
			}
			if(life<=2)
			{
				g.drawImage(this.spritesLowLife[index], this.getX()-Camera.x,this.getY()-Camera.y,null);				
			}
		}
		else
		{
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX()-Camera.x,this.getY()-Camera.y,null);			
		}
		
	}
}
