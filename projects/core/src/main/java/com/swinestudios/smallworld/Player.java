package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;

public class Player implements InputProcessor{ 

	public float x, y;
	public float velX, velY;

	public final float moveSpeedX = 2.0f;
	public final float moveSpeedY = 2.0f;

	public boolean isActive;
	public boolean walking;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	//Controls/key bindings
	public final int LEFT = Keys.A;
	public final int RIGHT = Keys.D;
	public final int UP = Keys.W;
	public final int DOWN = Keys.S;

	public Player(float x, float y, Gameplay level){
		this.x = x;
		this.y = y;
		velX = 0;
		velY = 0;
		isActive = true;
		walking = false;
		this.level = level;
		type = "Player";
		
		hitbox = new Rectangle(x, y, 32, 32); 
	}

	public void render(Graphics g){
		g.setColor(Color.GREEN);
		g.drawRect(x, y, hitbox.getWidth(), hitbox.getHeight());
	}

	public void update(float delta){
		playerMovement();

		if(!Gdx.input.isKeyPressed(this.LEFT) && !Gdx.input.isKeyPressed(this.RIGHT)){
			velX = 0;
		}
		if(!Gdx.input.isKeyPressed(this.UP) && !Gdx.input.isKeyPressed(this.DOWN)){
			velY = 0;
		}

		hitbox.setX(this.x);
		hitbox.setY(this.y);
	}

	public void playerMovement(){
		//Move Left
		if(Gdx.input.isKeyPressed(this.LEFT)){
			velX = -moveSpeedX;
		}
		//Move Right
		if(Gdx.input.isKeyPressed(this.RIGHT)){
			velX = moveSpeedX;
		}
		//Move Up
		if(Gdx.input.isKeyPressed(this.UP)){
			velY = -moveSpeedY;
		}
		//Move Down
		if(Gdx.input.isKeyPressed(this.DOWN)){
			velY = moveSpeedY;
		}
		move();
	}

	/*
	 * Checks if there is a collision if the player was at the given position.
	 */
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

	/*
	 * Helper method for checking whether there is a collision if the player moves at the given position
	 */
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

	/*
	 * Move horizontally in the direction of the x-velocity vector. If there is a collision in
	 * this direction, step pixel by pixel up until the player hits the solid.
	 */
	public void moveX(){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x + velX, y)){
				while(!isColliding(solid, x + Math.signum(velX), y)){
					x += Math.signum(velX);
				}
				velX = 0;
			}
		}
		x += velX;
	}

	/*
	 * Move vertically in the direction of the y-velocity vector. If there is a collision in
	 * this direction, step pixel by pixel up until the player hits the solid.
	 */
	public void moveY(){
		for(int i = 0; i < level.solids.size(); i++){
			Rectangle solid = level.solids.get(i);
			if(isColliding(solid, x, y + velY)){
				while(!isColliding(solid, x, y + Math.signum(velY))){
					y += Math.signum(velY);
				}
				velY = 0;
			}
		}
		y += velY;
	}

	//========================================Input Methods==============================================

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(button == Buttons.LEFT){
			int[][] mapData = level.currentLevel;
			int mx = Gdx.input.getX() / Gameplay.TILE_SIZE;
			int my = Gdx.input.getY() / Gameplay.TILE_SIZE;
			//Check array bounds
			if(mx >= 0 && my >= 0 && mx < mapData[0].length && my < mapData.length){
				if(mapData[my][mx] == 0){
					mapData[my][mx] = -1;
					Bridge newBridge = new Bridge(mx * Gameplay.TILE_SIZE, my * Gameplay.TILE_SIZE, level);
					//If spawned at the bottom OR there's no land below, draw bottom part of the bridge
					if(my == mapData.length - 1 || mapData[my+1][mx] == 0){
						newBridge.drawBottom = true;
					}
					else{
						newBridge.drawBottom = false;
					}
					level.bridges.add(newBridge);
					
					//TODO debug print remove later
					System.out.println("=================================");
					//Print map data
					for(int i = 0; i < mapData.length; i++){
						for(int j = 0; j < mapData[i].length; j++){
							System.out.print(mapData[i][j] + ", ");
						}
						System.out.println();
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}