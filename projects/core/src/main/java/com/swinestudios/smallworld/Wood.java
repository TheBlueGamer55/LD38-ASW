package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Wood{ 

	public float x, y;

	public boolean isActive;

	public Sprite woodSprite;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public Wood(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		isActive = true;
		this.level = level;
		type = "Tree";

		woodSprite = new Sprite(new Texture(Gdx.files.internal("wood.png")));

		adjustSprite(woodSprite);
		resizeSprite(woodSprite);

		hitbox = new Rectangle(x, y, woodSprite.getWidth(), woodSprite.getHeight());
	}

	public void render(Graphics g){
		g.drawSprite(woodSprite, x, y - (woodSprite.getHeight() * 3 / 4) );
	}

	public void update(float delta){
		hitbox.setX(this.x);
		hitbox.setY(this.y);
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