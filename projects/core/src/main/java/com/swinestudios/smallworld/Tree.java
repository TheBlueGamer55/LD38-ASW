package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tree{ 

	public float x, y;

	public boolean isActive;

	public Sprite treeSprite;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public Tree(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		isActive = true;
		this.level = level;
		type = "Tree";

		treeSprite = new Sprite(new Texture(Gdx.files.internal("tree.png")));

		adjustSprite(treeSprite);
		resizeSprite(treeSprite);

		hitbox = new Rectangle(x, y, treeSprite.getWidth(), treeSprite.getHeight());
	}

	public void render(Graphics g){
		g.drawSprite(treeSprite, x, y - (treeSprite.getHeight() * 3 / 4) );
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