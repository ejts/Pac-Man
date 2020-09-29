package com.everton.world;

public class Vector2i {

	public int x, y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		Vector2i vec = (Vector2i) o;
		
		return this.x == vec.x && this.y == vec.y;
	}
}
