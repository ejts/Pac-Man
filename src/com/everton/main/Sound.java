package com.everton.main;

import java.io.*;
import javax.sound.sampled.*;

public class Sound {
	
	public static class Clips {
		public Clip[] clips;
		private int p;
		private int count;
		
		public Clips(byte[] buffer, int count) throws LineUnavailableException, 
		IOException, UnsupportedAudioFileException {
			if(buffer == null) {
				return;
			}
			this.clips = new Clip[count];
			this.count = count;
			
			for(int i = 0; i < this.count; i++) {
				this.clips[i] = AudioSystem.getClip();
				this.clips[i].open(AudioSystem.getAudioInputStream(
						new ByteArrayInputStream(buffer)));
			}
		}
		
		public void play() {
			if(this.clips == null) {
				return;
			}
			
			this.clips[p].stop();
			this.clips[p].setFramePosition(0);
			this.clips[p].start();
			
			this.p++;
			if(this.p >= this.count) {
				this.p = 0;
			}
		}
		
		public void loop() {
			if(this.clips == null) {
				return;
			}
			this.clips[p].loop(300);
		}
		
		public void stop() {
			if(this.clips == null) {
				return;
			}
			
			this.clips[p].stop();
			this.clips[p].setFramePosition(0);
		}
	}

	public final static Clips PLAYER_HURT = Sound.load("/player_hurt.wav", 1);
	
	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(
					Sound.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >= 0){
				baos.write(buffer, 0, read);
			}
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(data, count);
		} catch(Exception e) {
			try {
				return new Clips(null, 0);
			} catch(Exception ee) {
				return null;
			}
		}
	}

}
