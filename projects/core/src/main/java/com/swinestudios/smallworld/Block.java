package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.graphics.Color;

public class Block extends Rectangle{

	public boolean isActive;
	public String type;
	private Gameplay level;

	public Block(float x, float y, float width, float height, Gameplay level){
		super(x, y, width, height);
		isActive = true;
		type = "Block";
		this.level = level;
	}

	public void render(Graphics g){
		g.setColor(Color.GRAY);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
	}

	public void update(float delta){
		//Empty for now
	}

}
