package com.VortexGames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.VortexGames.entities.BulletShoot;
import com.VortexGames.entities.Enemy;
import com.VortexGames.entities.Entity;
import com.VortexGames.entities.Player;
import com.VortexGames.graficos.Spritesheet;
import com.VortexGames.graficos.UI;
import com.VortexGames.world.Camera;
import com.VortexGames.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener{
	
	private static final long serialVersionUID = 1L;
	
	// variaveis
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning;
	public final static int WIDTH = 240;
	public final static int HEIGHT = 160;
	private final int SCALE = 3;
	
	private BufferedImage image;
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	
	public static List<Entity>entities;
	public static List<Enemy>enemies;
	public static List<BulletShoot> bullets;
	
	public static Player player;	
	
	public static Spritesheet spritesheet;
	public static World world;
			
	public static Random rand;
	
	public UI ui;
	//
	
	// construtor
	public Game()
	{
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE)); 
		initiFrame();
		
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		
		spritesheet = new Spritesheet("/spritesheet.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/level1.png");
		
		
	}
	//		
	
	// inicializar janela
	public void initiFrame()
	{
		frame = new JFrame("Game_01");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		//frame.setLocation(2500,300);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	//
	
	//inicia jogo
	public synchronized void start()
	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	//
	
	//encerra jogo
	public synchronized void stop()
	{
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//
	
	//main
	public static void main(String args[])
	{
		Game game = new Game();
		game.start();
	}
	//
	
	// logica
	public void tick()
	{
		for(int i =0; i<entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.tick();
		}
		
		for(int i =0; i<bullets.size();i++)
		{
			bullets.get(i).tick();
		}
		
		if(Game.enemies.size()==0)
		{
			//avançar proximo level
			this.CUR_LEVEL++;
			if(this.CUR_LEVEL>this.MAX_LEVEL)
			{
				CUR_LEVEL = 1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			World.restartGame(newWorld);
		}
		
	}
	//
	
	//graficos
	public void render()
	{
			BufferStrategy bs = this.getBufferStrategy();
			if(bs==null)
			{
				this.createBufferStrategy(3);
				return;
			}
			Graphics g = image.getGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,WIDTH*SCALE, HEIGHT*SCALE);
			
			//Renderização do jogo
			world.render(g);
			for(int i =0; i<entities.size(); i++)
			{
				Entity e = entities.get(i);
				e.render(g);
			}
			ui.render(g);
			for(int i =0; i<bullets.size();i++)
			{
				bullets.get(i).render(g);
			}

			//
			
			g.dispose();
			g = bs.getDrawGraphics();
			g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null);
			g.setFont(new Font("arial",Font.BOLD, 20));
			g.setColor(Color.white);
			g.drawString("Bullets: "+player.ammo,600,20);
			bs.show();
			
	}
	//
	
	//game looping
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000/amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while(isRunning)
		{
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if(delta>=1)
			{
				tick();
				render(); 
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer>=1000)
			{
				//System.out.println("FPS: " + frames);
				frames = 0;
				timer+=1000;
			}
		}
	}
	//
	
	
	//entradas do teclado 
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT||e.getKeyCode( )== KeyEvent.VK_D)
		{
				player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT||e.getKeyCode( )== KeyEvent.VK_A)
		{
				player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode( )== KeyEvent.VK_W)
		{
				player.up = true;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN||e.getKeyCode( )== KeyEvent.VK_S)
		{	
				player.down = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
				player.shoot = true; 
		}
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT||e.getKeyCode( )== KeyEvent.VK_D)
		{
				player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT||e.getKeyCode( )== KeyEvent.VK_A)
		{
				player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP||e.getKeyCode( )== KeyEvent.VK_W)
		{
				player.up = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN||e.getKeyCode( )== KeyEvent.VK_S)
		{	
				player.down = false;
		}
	}
	//
	
	//entradas mouse
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	//
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			player.mouseShoot = true;
			player.mx = (e.getX()/3);
			player.my = (e.getY()/3);
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//
}
