package com.everton.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.everton.main.Game;
import com.everton.world.Camera;
import com.everton.world.Node;
import com.everton.world.Vector2i;
import com.everton.world.World;

public class Entity {
	
	public final static BufferedImage CHERRY_SPRITE = Game.spritesheet.getSprite(80, 0, 16, 16);
	public final static BufferedImage ENEMY_RED = Game.spritesheet.getSprite(32, 64, 16, 16);
	public final static BufferedImage ENEMY_PINK = Game.spritesheet.getSprite(32, 80, 16, 16);
	
	public double x;
	public double y;
	protected int width;
	protected int height;
	protected double speed;
	
	public int depth;
	
	protected List<Node> path;
	
	private BufferedImage sprite;
	
	public static final Random RAND = new Random();
	
	public static final Comparator<Entity> entitySorter = new Comparator<>() {
		@Override
		public int compare(Entity e0, Entity e1) {
			if(e1.depth < e0.depth) {
				return 1;
			}
			if(e1.depth > e0.depth) {
				return -1;
			}
			return 0;
		}
	};
	
	public Entity(double x, double y, int width, int height, double speed,BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}

	public int getX() {
		return (int) x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public int getY() {
		return (int) y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public BufferedImage getSprite() {
		return this.sprite;
	}
	
	public void tick() {
		
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + 
				(y1 - y2) * (y1 - y2));
	}
	
	public void followPath(List<Node> path) {
		if(path != null) {
			if(path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;		
				if(this.x < (target.x * 16)) {
					this.x++;
				} else if(this.x > (target.x * 16)) {
					this.x--;
				} 
				
				if(this.y < (target.y * 16)) {
					this.y++;
				} else if(this.y > (target.y * 16)) {
					this.y--;
				}
				
				if((this.x == target.x * 16) && (this.y == target.y * 16)) {
					path.remove(path.size() - 1);
				}
			}
		}
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());
	
		if(e1Mask.intersects(e2Mask)) {
			return true;
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		g.drawImage(this.sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	
}
