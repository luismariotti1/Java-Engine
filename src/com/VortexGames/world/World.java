package com.VortexGames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.VortexGames.entities.*;
import com.VortexGames.world.*;
import com.VortexGames.graficos.Spritesheet;
import com.VortexGames.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	public String lv2 = "/level2.png";

	/*
	 * Guia Criação do mapa Preto chao Branco parede Azul jogador Vermelho inimigo
	 * Verde vida laranja arma Amarelo munição
	 */

	// construtor do map, usa varredura de sprite
	public World(String path) {
		// System.out.println(lv2);
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];

			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();

			tiles = new Tile[WIDTH * HEIGHT];

			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

			// percorre o vetor no sentido de y, depois x.
			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int pixelAtual = xx + (yy * WIDTH);
					// System.out.printf("%d ",pixelAtual);
					tiles[pixelAtual] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR, 1);
					if (path.compareTo(lv2) == 0) {
						tiles[pixelAtual] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR, 2);
					}
					if (pixels[pixelAtual] == 0xFFFFFFFF) {
						// wall
						tiles[pixelAtual] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL, 1);
						if (path.compareTo(lv2) == 0) {
							tiles[pixelAtual] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL, 2);
						}
					} else if (pixels[pixelAtual] == 0xFF1911FF) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if (pixels[pixelAtual] == 0xFFFF0A16) {
						// Enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if (pixels[pixelAtual] == 0xFF05FF11) {
						// LifePack
						LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
						Game.entities.add(pack);
					} else if (pixels[pixelAtual] == 0xFFFF6A00) {
						// Weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if (pixels[pixelAtual] == 0xFFFFF659) {
						// Bullet
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					}
				}
				// System.out.printf("\n");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//

	// verifica se o player pode caminhar para uma direção ou terá colisao com um
	// tile
	public static boolean isFree(int xnext, int ynext) {
		
		return true;
	}

	public static void restartGame(String level) {
		Game.entities.clear();
		Game.enemies.clear();
		Game.bullets.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
		return;
	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH / 16);
		int yfinal = ystart + (Game.HEIGHT / 16);

		for (int xx = xstart; xx <= xfinal; xx++) {
			for (int yy = ystart; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}

}
