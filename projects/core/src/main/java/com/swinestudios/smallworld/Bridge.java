package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bridge{ 

	public float x, y;

	public boolean isActive;

	public Gameplay level;
	public Rectangle hitbox;
	public String type;
	public Sprite bridgeTop, bridgeWhole;
	public boolean drawBottom;

	public Bridge(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		isActive = true;
		drawBottom = false;
		this.level = level;
		type = "Bridge";
		
		bridgeTop = new Sprite(new Texture(Gdx.files.internal("island_bridge2.png")));
		bridgeWhole = new Sprite(new Texture(Gdx.files.internal("island_bridge.png")));
		adjustSprite(bridgeTop, bridgeWhole);
		resizeSprite(bridgeTop, bridgeWhole);
		
		hitbox = new Rectangle(x, y, 32, 40);
	}

	public void render(Graphics g){
		//If land below this tile, don't draw the bottom part
		if(drawBottom){
			g.drawSprite(bridgeWhole, this.x, this.y);
		}
		else{
			g.drawSprite(bridgeTop, this.x, this.y);
		}
		/*g.setColor(Color.FIREBRICK);
		g.drawCircle(x + 16, y + 20, 12);
		g.fillRect(x, y, hitbox.getWidth(), hitbox.getHeight());*/
	}

	public void update(float delta){
		
	}
	
	public void adjustSprite(Sprite... s){
		for(int i = 0; i < s.length; i++){
			if(s != null){
				s[i].setOrigin(0, 0);
			}
		}
	}

	public void resizeSprite(Sprite... s){
		for(int i = 0; i < s.length; i++){
			if(s != null){ 
				s[i].setSize(s[i].getWidth()*2, s[i].getHeight()*2);
			}
		}
	}

}