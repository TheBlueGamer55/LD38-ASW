package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
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

	public static Sound buildSfx = Gdx.audio.newSound(Gdx.files.internal("bridge_built.wav"));

	//Controls/key bindings
	public static final int LEFT = Keys.A;
	public static final int RIGHT = Keys.D;
	public static final int UP = Keys.W;
	public static final int DOWN = Keys.S;

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

		if(!Gdx.input.isKeyPressed(LEFT) && !Gdx.input.isKeyPressed(RIGHT)){
			velX = 0;
		}
		if(!Gdx.input.isKeyPressed(UP) && !Gdx.input.isKeyPressed(DOWN)){
			velY = 0;
		}

		hitbox.setX(this.x);
		hitbox.setY(this.y);
	}

	public void playerMovement(){
		//Move Left
		if(Gdx.input.isKeyPressed(LEFT)){
			velX = -moveSpeedX;
		}
		//Move Right
		if(Gdx.input.isKeyPressed(RIGHT)){
			velX = moveSpeedX;
		}
		//Move Up
		if(Gdx.input.isKeyPressed(UP)){
			velY = -moveSpeedY;
		}
		//Move Down
		if(Gdx.input.isKeyPressed(DOWN)){
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

	public void deleteBridge(Bridge b){
		int mx = (int) ((b.x + 4 + (int) level.camX) / Gameplay.TILE_SIZE);
		int my = (int) ((b.y + 4 + (int) level.camY)/ Gameplay.TILE_SIZE);
		int[][] mapData = level.currentLevel;
		//Check array bounds
		if(mx >= 0 && my >= 0 && mx < mapData[0].length && my < mapData.length){
			if(mapData[my][mx] < 0){
				mapData[my][mx] = 0;
				//If there is a bridge above this deleted bridge, draw the bottom part
				if(my - 1 >= 0){
					if(mapData[my - 1][mx] < 0){
						Bridge above = getBridgeAt(mx * Gameplay.TILE_SIZE, (my - 1) * Gameplay.TILE_SIZE);
						above.drawBottom = true;
					}
				}
				//Delete the bridge object
				Bridge selectedBridge = getBridgeAt(mx * Gameplay.TILE_SIZE, my * Gameplay.TILE_SIZE);
				if(selectedBridge != null){
					level.bridges.remove(selectedBridge);
				}
				else{
					System.out.println("Error in attempting to remove bridge at cell " + mx + ", " + my);
				}
				//TODO debug print remove later
				System.out.println("=================================");
				System.out.println("# of islands: " + level.countIslands(level.currentLevel));
				level.printMapData();
			}
		}
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
			//Bridge spawning controls
			if(!level.paused && !level.movingToNextLevel && level.bridgeSelected){
				int[][] mapData = level.currentLevel;
				int mx = (Gdx.input.getX() + (int) level.camX) / Gameplay.TILE_SIZE;
				int my = (Gdx.input.getY() + (int) level.camY)/ Gameplay.TILE_SIZE;
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
						level.bridgeCount++;
						buildSfx.play(0.75f);
						//If all islands connected, move to next level
						if(level.countIslands(level.currentLevel) == 1){
							if(!level.movingToNextLevel){
								level.movingToNextLevel = true;
							}
							//level.moveToNextLevel();
						}

						//TODO debug print remove later
						System.out.println("=================================");
						System.out.println("# of islands: " + level.countIslands(level.currentLevel));
						level.printMapData();
					}
				}
			}
		}

		if(button == Buttons.RIGHT){
			if(level.bridgeSelected){
				if(!level.paused && !level.movingToNextLevel && level.bridgeSelected){
					int[][] mapData = level.currentLevel;
					int mx = (Gdx.input.getX() + (int) level.camX) / Gameplay.TILE_SIZE;
					int my = (Gdx.input.getY() + (int) level.camY)/ Gameplay.TILE_SIZE;
					//Check array bounds
					if(mx >= 0 && my >= 0 && mx < mapData[0].length && my < mapData.length){
						if(mapData[my][mx] < 0){ //If clicked on a bridge
							mapData[my][mx] = 0;
							//If there is a bridge above this deleted bridge, draw the bottom part
							if(my - 1 >= 0){
								if(mapData[my - 1][mx] < 0){
									Bridge above = getBridgeAt(mx * Gameplay.TILE_SIZE, (my - 1) * Gameplay.TILE_SIZE);
									above.drawBottom = true;
								}
							}
							//Delete the bridge object
							Bridge selectedBridge = getBridgeAt(mx * Gameplay.TILE_SIZE, my * Gameplay.TILE_SIZE);
							if(selectedBridge != null){
								level.bridges.remove(selectedBridge);
							}
							else{
								System.out.println("Error in attempting to remove bridge at cell " + mx + ", " + my);
							}
							//TODO debug print remove later
							System.out.println("=================================");
							System.out.println("# of islands: " + level.countIslands(level.currentLevel));
							level.printMapData();
						}
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