package com.swinestudios.smallworld;

import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Person{ 

	public float x, y;
	public float velX, velY;
	public float nextX, nextY;

	public final float moveSpeedX = 0.5f;
	public final float moveSpeedY = 0.5f;
	public final float SPEED = 0.5f;

	public boolean isActive;
	public boolean facingLeft, facingRight;

	public Sprite idle1, idle2;
	public Animation<Sprite> idle;
	public final float animationSpeed = 0.2f;

	public float moveTimer;
	public float minMoveTimer = 1f;
	public float maxMoveTimer = 3f;
	public float currentMoveTimer = minMoveTimer;
	public boolean canMove, isMoving;

	public Rectangle hitbox;
	public Gameplay level;
	public String type;

	public Person(float x, float y, char col, Gameplay level){
		this.x = x;
		this.y = y;
		this.nextX = x;
		this.nextY = y;
		velX = 0;
		velY = 0;
		isActive = true;
		canMove = false;
		this.level = level;
		type = "Person";

		if(col == 'r' || col == 'R'){
			idle1 = new Sprite(new Texture(Gdx.files.internal("person_red1.png")));
			idle2 = new Sprite(new Texture(Gdx.files.internal("person_red2.png")));
		}
		else if(col == 'b' || col == 'B'){
			idle1 = new Sprite(new Texture(Gdx.files.internal("person_blue1.png")));
			idle2 = new Sprite(new Texture(Gdx.files.internal("person_blue2.png")));
		}
		else if(col == 'g' || col == 'G'){
			idle1 = new Sprite(new Texture(Gdx.files.internal("person_green1.png")));
			idle2 = new Sprite(new Texture(Gdx.files.internal("person_green2.png")));
		}
		else{
			int choice = (int) (Math.random() * 3 + 1);
			String spr1 = "person_blue1.png";
			String spr2 = "person_blue2.png";
			if(choice == 1){
				spr1 = "person_red1.png";
				spr2 = "person_red2.png";
			}
			else if(choice == 2){
				spr1 = "person_green1.png";
				spr2 = "person_green2.png";
			}
			idle1 = new Sprite(new Texture(Gdx.files.internal(spr1)));
			idle2 = new Sprite(new Texture(Gdx.files.internal(spr2)));
		}

		adjustSprite(idle1, idle2);
		resizeSprite(idle1, idle2);

		idle = new Animation<Sprite>();

		idle.addFrame(idle1, animationSpeed);
		idle.addFrame(idle2, animationSpeed);
		idle.setLooping(true);

		hitbox = new Rectangle(x, y, 32, 32);
	}

	public void render(Graphics g){
		idle.draw(g, this.x, this.y);
	}

	public void update(float delta){
		idle.update(delta);
		wanderMovement(delta);

		hitbox.setX(this.x);
		hitbox.setY(this.y);
	}

	public void wanderMovement(float delta){
		if(canMove){
			if(!isMoving){ //Determine the next point to move towards
				isMoving = true;
				//Choose a random direction
				int choice = getRandomInt(1, 4);
				int dx = 0;
				int dy = 0;
				if(choice == 1){ //left
					dx = -1;
					dy = 0;
				}
				else if(choice == 2){ //right
					dx = 1;
					dy = 0;
				}
				else if(choice == 3){ //up
					dx = 0;
					dy = -1;
				}
				else if(choice == 4){ //down
					dx = 0;
					dy = 1;
				}
				int[][] mapData = level.currentLevel;
				int mx = (int) ((this.x + 4 + (int) level.camX) / Gameplay.TILE_SIZE);
				int my = (int) ((this.y + 4 + (int) level.camY)/ Gameplay.TILE_SIZE);
				mx += dx;
				my += dy;
				if(mx >= 0 && my >= 0 && mx < mapData[0].length && my < mapData.length){
					if(mapData[my][mx] > 0){ //TODO should bridges be crossable
						nextX = mx * Gameplay.TILE_SIZE;
						nextY = my * Gameplay.TILE_SIZE;
						float deltaX = nextX - this.x;
						float deltaY = nextY - this.y;
						float theta = (float) Math.atan2(deltaY, deltaX); 

						velX = (float) Math.cos(theta) * SPEED;
						velY = (float) Math.sin(theta) * SPEED;
					}
				}
			}
			else{ //Moving towards the next point
				float dist = getDistance(x, y, nextX, nextY);
				//System.out.println(x + ", " + y + " to " + nextX + ", " + nextY + " Dist: " + dist);
				if(dist > 4f){ //If haven't reached new destination
					x += velX;
					y += velY;
				}
				else{ //Stop moving, pick new target
					canMove = false;
					isMoving = false;
					velX = 0;
					velY = 0;
				}
			}
		}
		else{
			moveTimer += delta;
			if(moveTimer > currentMoveTimer){
				canMove = true;
				moveTimer = 0;
				currentMoveTimer = getRandomInt((int)minMoveTimer, (int)maxMoveTimer);
			}
		}
	}

	public int getRandomInt(int min, int max) {
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}
	
	public float getDistance(float x1, float y1, float x2, float y2){
		return ((float)Math.pow(Math.pow((y2 - y1), 2.0) + Math.pow((x2 - x1), 2.0), 0.5));
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
		y += velY;
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