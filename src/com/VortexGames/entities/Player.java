package com.VortexGames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.VortexGames.graficos.Spritesheet;
import com.VortexGames.main.Game;
import com.VortexGames.world.Camera;
import com.VortexGames.world.World;

public class Player extends Entity{

	//atributos do personagem
	public double speed = 1.4;
	public double life = 100, maxLife=100;
	public int ammo = 0;
	//
	
	//estados
	private boolean hasGun = false;
	public boolean moved = false;
	public boolean shoot = false;
	public boolean mouseShoot = false;
	public boolean isDamage = false;
	public boolean right, up, left, down;
	public boolean isShootingWithMouse = false;
	//
	
	//atributos de mecanica
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private int damageFrames=0;
	private int timer=0, maxTime = 20;
	public int mx, my;
	//
	
	//Sprites
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage playerDamage;
	//
	
	//construtor
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = this.PLAYER_FEEDBACK;
		
		for(int i =0; i<4; i++)
		{
			rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
	}//

	//logica
	public void tick()
	{
		movimentPlayer();
		shootKeyboard();
		shootMouse();		
		checkCollisionLifePack();
		checkCollisionAmmo();
		checkCollisionGun();
		FeedBackDamage();
		gameOver();
		
		Camera.x = Camera.clamp(this.getX()-(Game.WIDTH/2),0,World.WIDTH*16-Game.WIDTH);
		Camera.y = Camera.clamp(this.getY()-(Game.HEIGHT/2),0,World.HEIGHT*16-Game.HEIGHT);
	}
	
	//metodos logica
	public void shootMouse()
	{
		if(mouseShoot)
		{
			mouseShoot = false;
			this.isShootingWithMouse = false;
			//criar bala atirar
			if(hasGun && ammo>0)
			{
				this.isShootingWithMouse = true;
				double angle = Math.atan2(my-(this.getY()+8-Camera.y),mx-(this.getX()+8-Camera.x));
				double dx = Math.cos(angle); 
				double dy = Math.sin(angle);
				int py = 6, px = 0;
				double angleSide = Math.toDegrees(angle);
				if(angleSide<=90 && angleSide>=-90)
				{
					timer = 0;
					dir = right_dir;
					px = 16;
					Math.atan2(my-(this.getY()+py-Camera.y),mx-(this.getX()+px-Camera.x));
				}
				else
				{
					timer = 0;
					dir = left_dir;
					px = 2;
					Math.atan2(my-(this.getY()+py-Camera.y),mx-(this.getX()+px-Camera.x));
				}
				ammo--;
				BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
				Game.bullets.add(bullet);
			}
			
		}
		if(this.isShootingWithMouse==true)
		{
			timer++;
			if(timer==this.maxTime)
			{
				this.isShootingWithMouse = false;
				timer = 0;
			}
		}
	}
	public void shootKeyboard()
	{
		if(shoot)
		{
			shoot = false;
			//criar bala atirar
			if(hasGun && ammo>0)
			{
				int dx = 0;
				int px = 0;
				int py = 6; 
				if(dir==right_dir)
				{
					px = 16;
					dx = 1; 				
				}
				else
				{
					px = 2;
					dx = -1;
				}
				ammo--;
				BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
				Game.bullets.add(bullet);				
			}
		}	
	}
	public void movimentPlayer()
	{
		moved = false;
		// Movimentos do player
		if(right)
		{
			moved = true;
			if(!this.isShootingWithMouse)
			{
				dir = right_dir;				
			}
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY()))
		{
			moved = true;
			if(!this.isShootingWithMouse)
			{
			dir = left_dir;
			}
			x-=1;
		}
		if(up)
		{
			moved = true;
			y-=speed;
		}
		else if(down)
		{
			moved = true;
			y+=speed;
		}
		
		if(moved)
		{
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
		}
	}
	public void checkCollisionGun()
	{
		for(int i =0; i<Game.entities.size();i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon)
			{
				if(Entity.isColliding(this, atual))
				{
					hasGun = true;
					Game.entities.remove(i);
				}
			}
		}
		
	}
	public void checkCollisionLifePack()
	{
		for(int i =0; i<Game.entities.size();i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof LifePack)
			{
				if(Entity.isColliding(this, atual))
				{
					life+=20;
					if(life>maxLife)
					{
						life = maxLife;
					}
					Game.entities.remove(i);
				}
			}
		}
	}
	public void checkCollisionAmmo()
	{
		for(int i =0; i<Game.entities.size();i++)
		{
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet)
			{
				if(Entity.isColliding(this, atual))
				{
					ammo+=10;
					Game.entities.remove(i);
				}
			}
		}
		
	}
	public void gameOver()
	{
		if(life<=0)
		{
			//gamer over
			return;
		}
	}
	public void FeedBackDamage()
	{
		if(isDamage)
		{
			this.damageFrames++;
			if(this.damageFrames==4)
			{
				this.damageFrames = 0;
				isDamage=false;
			}
		}
	}
	//
	
	//graficos
	public void render(Graphics g)
	{
		if(!isDamage)
		{
		if(dir==right_dir)
		{
			g.drawImage(rightPlayer[index], this.getX()-Camera.x,this.getY()-Camera.y,null);
			if(hasGun)
			{//desenhar arma direita
				g.drawImage(Entity.GUN_RIGHT, this.getX()+8-Camera.x,this.getY()-Camera.y,null);
			}
		}
		else if(dir==left_dir)
		{
			g.drawImage(leftPlayer[index], this.getX()+8-Camera.x,this.getY()-Camera.y,null);
			if(hasGun)
			{//desenhar arma esquerda				
				g.drawImage(Entity.GUN_LEFT, this.getX()-Camera.x,this.getY()-Camera.y,null);
			}
		}
		}
		else
		{
			g.drawImage(playerDamage, this.getX()-Camera.x,this.getY()-Camera.y,null);
			if(dir==right_dir)
			{
				g.drawImage(Entity.WEAPON_FEEDBACK_RIGHT, this.getX()-Camera.x,this.getY()-Camera.y,null);			
			}
			else if(dir==left_dir)
			{
				g.drawImage(Entity.WEAPON_FEEDBACK_LEFT, this.getX()-Camera.x,this.getY()-Camera.y,null);			
			}
		}
	}
	//
}
