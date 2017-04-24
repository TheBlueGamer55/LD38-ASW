package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Shark { 

	public float x, y;
	public float velX, velY;

	public final float moveSpeedX = 2.0f;
	public final float moveSpeedY = 2.0f;

	public boolean isActive;
	public boolean facingLeft, facingRight;

	public Sprite left1, left2, right1, right2;
	public Animation<Sprite> sharkLeft, sharkRight, sharkCurrent;
	public final float animationSpeed = 0.2f; 

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public Shark(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = 0;
		isActive = true;
		this.level = level;
		type = "Shark";

		right1 = new Sprite(new Texture(Gdx.files.internal("shark_right01.png")));
		right2 = new Sprite(new Texture(Gdx.files.internal("shark_right02.png")));
		left1 = new Sprite(new Texture(Gdx.files.internal("shark_left01.png")));
		left2 = new Sprite(new Texture(Gdx.files.internal("shark_left02.png")));		

		adjustSprite(right1, right2, left1, left2);
		resizeSprite(right1, right2, left1, left2);

		sharkLeft = new Animation<Sprite>();
		sharkRight = new Animation<Sprite>();

		sharkLeft.addFrame(left1, animationSpeed);
		sharkLeft.addFrame(left2, animationSpeed);
		sharkLeft.setLooping(true);

		sharkRight.addFrame(right1, animationSpeed);
		sharkRight.addFrame(right2, animationSpeed);
		sharkRight.setLooping(true);

		facingLeft = true;
		facingRight = false;
		sharkCurrent = sharkLeft;

		hitbox = new Rectangle(x, y, 32, 32);
	}

	public void render(Graphics g){
		sharkCurrent.draw(g, this.x, this.y);
	}

	public void update(float delta){
		sharkCurrent.update(delta);
		sharkMovement();

		if(facingLeft){
			sharkCurrent = sharkLeft;
		}
		else if(facingRight){
			sharkCurrent = sharkRight;
		}
		
		if(velX > 0){
			facingRight = true;
			facingLeft = false;
		}
		if(velX < 0){
			facingLeft = true;
			facingRight = false;
		}

		hitbox.setX(this.x);
		hitbox.setY(this.y);
	}

	public void sharkMovement(){
		//checkBridgeCollision();
		move();
	}

	public void checkBridgeCollision(){
		for(int i = 0; i < level.bridges.size(); i++){
			Bridge temp = level.bridges.get(i);
			if(temp != null && temp.isActive){
				if(isColliding(temp.hitbox, x + velX, y)){
					temp.isActive = false;
					level.player.deleteBridge(temp);
					velX = -velX;
				}
				else if(isColliding(temp.hitbox, x, y + velY)){
					temp.isActive = false;
					level.player.deleteBridge(temp);
					velY = -velY;
				}
			}
		}
	}

	public boolean isColliding(Rectangle other, float x, float y){
		if(other == this.hitbox){ //Make sure solid isn't stuck on itself
			return false;
		}
		if(x < other.getX() + other.getWidth() && x + hitbox.getWidth() > other.getX() 
				&& y < other.getY() + other.getHeight() && y + hitbox.getHeight() > other.getY()){
			return true;
		}
		return false;
	}

	public boolean collisionExistsAt(float x, float y){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x, y)){
				return true;
			}
		}
		return false;
	}

	public void move(){
		moveX();
		moveY();
	}

	public void moveX(){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x + velX, y)){
				while(!isColliding(solid, x + Math.signum(velX), y)){
					x += Math.signum(velX);
				}
				velX = -velX;
			}
		}
		for(int i = 0; i < level.bridges.size(); i++){ //Also check for bridge hitboxes
			Rectangle solid = level.bridges.get(i).hitbox;
			if(isColliding(solid, x + velX, y)){
				while(!isColliding(solid, x + Math.signum(velX), y)){
					x += Math.signum(velX);
				}
				velX = -velX;
				level.player.deleteBridge(level.bridges.get(i));
				System.out.println("Deleting bridge....");
			}
		}
		x += velX;
	}

	public void moveY(){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x, y + velY)){
				while(!isColliding(solid, x, y + Math.signum(velY))){
					y += Math.signum(velY);
				}
				velY = -velY;
			}
		}
		for(int i = 0; i < level.bridges.size(); i++){ //Also check for bridge hitboxes
			Rectangle solid = level.bridges.get(i).hitbox;
			if(isColliding(solid, x, y + velY)){
				while(!isColliding(solid, x, y + Math.signum(velY))){
					y += Math.signum(velY);
				}
				velY = -velY;
				level.player.deleteBridge(level.bridges.get(i));
			}
		}
		y += velY;
	}

	public Bridge getBridgeAt(int x, int y){
		Bridge selectedBridge = null;
		for(int i = 0; i < level.bridges.size(); i++){
			Bridge b = level.bridges.get(i);
			if(b.x / Gameplay.TILE_SIZE == x / Gameplay.TILE_SIZE && b.y / Gameplay.TILE_SIZE == y / Gameplay.TILE_SIZE){
				selectedBridge = b;
				break;
			}
		}
		return selectedBridge;
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