package com.everton.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.everton.main.Game;
import com.everton.world.Camera;
import com.everton.world.World;

public class Player extends Entity {
	
	public boolean right, left, up, down;
	public static enum Directions {
		RIGHT, LEFT, UP, DOWN;
	}
	public Directions lastDir = Directions.RIGHT;
	
	private int frames = 0, maxFrames = 7, index = 0, maxIndex = 3;
	private boolean moved = false;
	public BufferedImage[] spriteRight, spriteLeft, spriteUp, spriteDown;

	public Player(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed,sprite);
		this.depth = 2;

		this.spriteRight = new BufferedImage[3];
		this.spriteLeft = new BufferedImage[3];
		this.spriteUp = new BufferedImage[3];
		this.spriteDown = new BufferedImage[3];

		this.spriteRight[0] = Game.spritesheet.getSprite(64, 0, 16, 16);
		this.spriteRight[1] = Game.spritesheet.getSprite(32, 0, 16, 16);
		this.spriteRight[2] = Game.spritesheet.getSprite(48, 0, 16, 16);

		this.spriteLeft[0] = this.spriteRight[0];
		this.spriteLeft[1] = Game.spritesheet.getSprite(32, 16, width, height);
		this.spriteLeft[2] = Game.spritesheet.getSprite(48, 16, width, height);

		this.spriteUp[0] = this.spriteRight[0];
		this.spriteUp[1] = Game.spritesheet.getSprite(32, 32, width, height);
		this.spriteUp[2] = Game.spritesheet.getSprite(48, 32, width, height);

		this.spriteDown[0] = this.spriteRight[0];
		this.spriteDown[1] = Game.spritesheet.getSprite(32, 48, width, height);
		this.spriteDown[2] = Game.spritesheet.getSprite(48, 48, width, height);
	}
	
	@Override
	public void tick() {
		
		this.moved = false;
		if(this.right && World.isFree((int)(this.x + this.speed), this.getY())) {
			super.x += this.speed;
			this.lastDir = Directions.RIGHT;
			this.moved = true;
		} else if(this.left && World.isFree((int)(this.x - this.speed), this.getY())) {
			super.x -= this.speed;
			this.lastDir = Directions.LEFT;
			this.moved = true;
		}
		
		if(this.up && World.isFree(this.getX(), (int)(this.y - this.speed))) {
			super.y -= this.speed;
			this.lastDir = Directions.UP;
			this.moved = true;
		} else if(this.down && World.isFree(this.getX(), (int)(this.y + this.speed))) {
			super.y += this.speed;
			this.lastDir = Directions.DOWN;
			this.moved = true;
		}
		
		this.isCollisionFruit();
		
		if(Game.countFruits == Game.curFruits) {
			//Pegou todas as frutas
		}
		
		if(this.moved) {
			this.frames++;
			if(this.frames == this.maxFrames) {
				this.frames = 0;
				this.index++;
				if(this.index == this.maxIndex) {
					this.index = 0;
				}
			}
		}
	}
	
	public void isCollisionFruit() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Cherry) {
				if(Entity.isColidding(this, e)) {
					Game.curFruits++;
					Game.entities.remove(e);
					return;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics g) {
		if(this.lastDir.equals(Directions.RIGHT)) {
			g.drawImage(this.spriteRight[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(this.lastDir.equals(Directions.LEFT)) {
			g.drawImage(this.spriteLeft[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(this.lastDir.equals(Directions.UP)) {
			g.drawImage(this.spriteUp[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(this.lastDir.equals(Directions.DOWN)) {
			g.drawImage(this.spriteDown[this.index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}

}
