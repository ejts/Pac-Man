package com.everton.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.everton.main.Game;
import com.everton.world.AStar;
import com.everton.world.Vector2i;

public class Enemy extends Entity {
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.depth = 1;
	}
	
	@Override
	public void tick() {
		if(this.path == null || this.path.size() == 0) {
			Vector2i start = new Vector2i((int)(this.x / 16), (int)(this.y / 16));
			Vector2i end = new Vector2i((int)(Game.player.x / 16), (int)(Game.player.y / 16));
					
			this.path = AStar.findPath(Game.world, start, end);
		}
			
		if(Entity.RAND.nextInt(100) < 85) {
			this.followPath(this.path);
		}
			
		if(this.x % 16 == 0 && this.y % 16 == 0) {
			if(Entity.RAND.nextInt(100) < 5) {
				Vector2i start = new Vector2i((int)(this.x / 16), (int)(this.y / 16));
				Vector2i end = new Vector2i((int)(Game.player.x / 16), (int)(Game.player.y / 16));
					
				this.path = AStar.findPath(Game.world, start, end);
			}
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
	}
}
