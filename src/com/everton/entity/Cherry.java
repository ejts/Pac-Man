package com.everton.entity;

import java.awt.image.BufferedImage;

public class Cherry extends Entity {

	public Cherry(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.depth = 0;
	}
}
