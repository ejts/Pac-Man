package com.everton.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.everton.entity.Cherry;
import com.everton.entity.Enemy;
import com.everton.entity.Entity;
import com.everton.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			World.tiles = new Tile[map.getWidth() * map.getHeight()];
			
			World.WIDTH = map.getWidth();
			World.HEIGHT = map.getHeight();
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_FLOOR);
					
					if(pixelAtual == 0xFF000000) {
						//Chão
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_FLOOR);
					} else if(pixelAtual == 0xFFFFFFFF) {
						//Parede
						tiles[xx + (yy * World.WIDTH)] = new WallTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_WALL);
					} else if(pixelAtual == 0xFF0026FF) {
						//Player
						Game.player.setX(xx * World.TILE_SIZE);
						Game.player.setY(yy * World.TILE_SIZE);
						tiles[xx + (yy * World.WIDTH)] = new FloorTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_FLOOR);
					} else if(pixelAtual == 0xFFFFD800) {
						//Cereja
						Game.entities.add(new Cherry(xx * World.TILE_SIZE, yy * World.TILE_SIZE, 16, 16, 0, Entity.CHERRY_SPRITE));
						Game.countFruits++;
					} else if(pixelAtual == 0xFFE0281A) {
						//Inimigo Vermelho
						Enemy e = new Enemy(xx * World.TILE_SIZE, yy * World.TILE_SIZE, 16, 16, 1, Entity.ENEMY_RED);
						Game.entities.add(e);
					} else if(pixelAtual == 0xFFDBABC4) {
						//Inimigo Rosa
						Enemy e = new Enemy(xx * World.TILE_SIZE, yy * World.TILE_SIZE, 16, 16, 1, Entity.ENEMY_PINK);
						Game.entities.add(e);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / World.TILE_SIZE;
		int y1 = yNext / World.TILE_SIZE;
		
		int x2 = (xNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		int y2 = yNext / World.TILE_SIZE;
		
		int x3 = xNext / World.TILE_SIZE;
		int y3 = (yNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		
		int x4 = (xNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		int y4 = (yNext + World.TILE_SIZE - 1) / World.TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public static void restartGame(String level) {
		new Game();
		
		return;
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= World.WIDTH || yy >= World.HEIGHT) {
					continue;
				}
				Tile tile = World.tiles[xx + (yy * World.WIDTH)];
				tile.render(g);
			}
		}
	}
	
	public static void renderMiniMap() {
		/*
		for(int i = 0; i < Game.miniMapPixels.length; i++) {
			Game.miniMapPixels[i] = 0;
		}
		
		for(int xx = 0; xx < World.WIDTH; xx++) {
			for(int yy = 0; yy < World.HEIGHT; yy++) {
				if(World.tiles[xx + (yy * World.WIDTH)] instanceof WallTile) {
					Game.miniMapPixels[xx + (yy * World.WIDTH)] = 0x0B3749;
				}
			}
		}
		
		int xPlayer = Game.player.getX() / 16;
		int yPlayer = Game.player.getY() / 16;
		
		Game.miniMapPixels[xPlayer + (yPlayer * World.WIDTH)] = 0xffffff;
	
		for(Entity e : Game.entities) {
			int xx = e.getX() / 16;
			int yy = e.getY() / 16;
			
			if(e instanceof Enemy) {
				Game.miniMapPixels[xx + (yy * World.WIDTH)] = 0x912B05;
			} else if(e instanceof Bow) {
				Game.miniMapPixels[xx + (yy * World.WIDTH)] = 0xFFD800;
			} else if(e instanceof Arrow) {
				Game.miniMapPixels[xx + (yy * World.WIDTH)] = 0xB56C00;
			} else if(e instanceof Potion) {
				Game.miniMapPixels[xx + (yy * World.WIDTH)] = 0xB50000;
			}
		}*/
	}
}
