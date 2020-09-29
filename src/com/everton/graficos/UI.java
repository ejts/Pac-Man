package com.everton.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.everton.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 18));
		g.drawString("Frutas: " + Game.curFruits + "/" + Game.countFruits,
				30, 30);
	}
}
