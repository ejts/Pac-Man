package com.everton.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.everton.entity.Entity;
import com.everton.entity.Player;
import com.everton.graficos.Spritesheet;
import com.everton.graficos.UI;
import com.everton.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	
	public final static int WIDTH = 240;
	public final static int HEIGHT = 240;
	public final static int SCALE = 2;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	public static List<Entity> fruits;
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	
	public UI ui;
	
	public static int countFruits = 0, curFruits = 0;
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		this.setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		this.initFrame();
		this.image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
	
		//Inicializando Objetos
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.entities = new ArrayList<>();
		Game.fruits = new ArrayList<>();
		Game.player = new Player(0, 0, 16, 16, 1, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.world = new World("/level_1.png");
		this.ui = new UI();
		Game.entities = new ArrayList<>();
		
		Game.entities.add(player);
	}
	
	public void initFrame() {
		Game.frame = new JFrame("Pac-Man");
		Game.frame.add(this);
		
		Game.frame.setResizable(false);
		Game.frame.pack();
		
		//Icone da Janela
		Image img_window = null;
		try {
			img_window = ImageIO.read(getClass().getResource("/icon_window.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Game.frame.setIconImage(img_window);
		Game.frame.setAlwaysOnTop(true);
				
		Game.frame.setLocationRelativeTo(null);
		Game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Game.frame.setVisible(true);
	}
	
	public synchronized void start() {
		this.thread = new Thread(this);
		this.isRunning = true;
		this.thread.start();
	}
	
	public synchronized void stop() {
		this.isRunning = false;
		
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			e.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = this.image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		/*Renderização do Game*/
		//Graphics2D g2 = (Graphics2D) g;
		Game.world.render(g);
		
		Collections.sort(Game.entities, Entity.entitySorter);
		
		for(int i = 0; i < Game.fruits.size(); i++) {
			Entity e = Game.fruits.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			e.render(g);
		}
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		
		g.drawImage(this.image, 0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE, null);
		ui.render(g);
		
		bs.show();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		
		this.requestFocus();
		
		while(this.isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}
		
		this.stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			Game.player.right = true;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			Game.player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			Game.player.up = true;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			Game.player.down = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			Game.player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			Game.player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			Game.player.up = false;
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			Game.player.down = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}
