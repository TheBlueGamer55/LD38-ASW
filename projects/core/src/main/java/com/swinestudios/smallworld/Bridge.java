package com.swinestudios.smallworld;

import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Bridge{ 

	public float x, y;

	public boolean isActive;

	public Gameplay level;
	public String type;
	public Sprite bridgeTop, bridgeWhole;

	public Bridge(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		isActive = true;
		this.level = level;
		type = "Bridge";
		
		bridgeTop = new Sprite(new Texture(Gdx.files.internal("island_bridge.png")));
		bridgeWhole = new Sprite(new Texture(Gdx.files.internal("island_bridge2.png")));
	}

	public void render(Graphics g){
		//TODO if land below this tile, don't draw the bottom part
		g.drawSprite(bridgeWhole, this.x, this.y);
	}

	public void update(float delta){
		
	}

}