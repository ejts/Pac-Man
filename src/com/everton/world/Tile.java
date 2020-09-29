package com.everton.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.everton.main.Game;

public class Tile {
	
	public static final BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 0, 16, 16);
	public static final BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 0, 16, 16);
	
	private BufferedImage sprite;
	private int x, y;
	
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, this.x - Camera.x, this.y - Camera.y, null);
	}
}
