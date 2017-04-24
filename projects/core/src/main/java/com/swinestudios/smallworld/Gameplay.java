package com.swinestudios.smallworld;

import java.util.ArrayList;
import java.util.Arrays;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.screen.transition.NullTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Gameplay implements GameScreen{

	public static int ID = 2;

	public static final int TILE_SIZE = 32;

	public final int[][] level00 = {
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,2,0,0,0,0},
			{0,0,0,0,1,1,0,0,0,0,0,0,2,2,4,2,2,0,0,0},
			{0,0,0,4,4,1,1,1,0,0,0,0,1,4,4,4,1,0,0,0},
			{0,0,2,2,3,1,1,1,0,0,0,0,0,1,1,1,0,0,0,0},
			{0,0,2,2,3,4,2,1,1,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,2,2,4,2,2,2,1,0,0,0,0,2,4,2,0,0,0,0},
			{0,0,4,2,2,2,1,1,0,0,0,0,1,1,1,1,1,0,0,0},
			{0,0,0,1,1,1,0,0,0,0,0,0,0,1,1,1,2,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,2,2,2,2,2,2,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,1,2,2,2,2,2,1,1,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};

	public final int[][] level01 = {
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,4,3,0,0,0,0,0,0,0,0,0,0},
			{0,3,3,2,2,3,2,2,0,0,0,0,0,0,1,1,1,0,0,0,0,0,2,2,3,2,0},
			{0,1,2,4,4,2,4,2,1,0,0,0,0,0,0,0,0,0,0,0,3,3,4,3,2,4,3},
			{0,0,1,1,1,4,2,1,0,0,0,0,0,0,0,0,0,0,0,1,4,1,2,4,2,1,1},
			{0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,3,2,2,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,3,4,1,4,1,4,3,0,0,0,0,2,1,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,3,1,4,1,4,1,3,0,0,0,3,3,0,0,0},
			{0,0,0,0,3,3,3,0,0,0,0,0,1,2,1,2,1,2,1,0,0,0,4,3,0,0,0},
			{0,0,0,3,3,4,3,0,0,0,0,0,0,1,0,1,0,1,0,0,0,0,1,2,0,0,0},
			{0,0,0,3,4,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0},
			{0,0,0,3,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,3,4,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,3,4,3,3,0,0,0,0,0,0,0,0,3,3,3,0,0,0,0,0,0,0,0},
			{0,0,1,2,2,2,1,1,0,0,0,0,0,0,0,1,1,4,1,1,0,0,0,0,0,0,0},
			{0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
	};

	public int[][] currentLevel;
	public int levelCount;
	public int bridgeCount;

	public boolean paused = false;
	public boolean gameOver = false;
	public boolean movingToNextLevel = false;

	public float camX, camY;

	public ArrayList<Block> solids;
	public ArrayList<Bridge> bridges;
	public ArrayList<Shark> sharks;
	public ArrayList<Person> people;
	public ArrayList<Wood> logs;
	public Sprite map00, map01;
	public Sprite currentMap;
	
	public Sprite scoreMenu, pauseMenu;

	public Sprite bridgeTile;
	public boolean bridgeSelected;

	public Player player;

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){
		bridgeTile = new Sprite(new Texture(Gdx.files.internal("island_bridge.png")));
		bridgeTile.setAlpha(0.5f);
		
		scoreMenu = new Sprite(new Texture(Gdx.files.internal("level_score_menu.png")));
		pauseMenu = new Sprite(new Texture(Gdx.files.internal("pause_menu.png")));

		map00 = new Sprite(new Texture(Gdx.files.internal("level00.png")));
		map01 = new Sprite(new Texture(Gdx.files.internal("level01.png")));

		adjustSprite(map00, map01, bridgeTile);
		resizeSprite(map00, map01, bridgeTile);

		currentMap = map00;

		bridges = new ArrayList<Bridge>();
		sharks = new ArrayList<Shark>();
		solids = new ArrayList<Block>();
		people = new ArrayList<Person>();
		logs = new ArrayList<Wood>();
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){
		paused = false;
		gameOver = false;
		bridgeSelected = false;
		movingToNextLevel = false;
	}

	@Override
	public void preTransitionIn(Transition t){
		paused = false;
		gameOver = false;
		bridgeSelected = true;
		movingToNextLevel = false;
		levelCount = 0;
		bridgeCount = 0;
		bridges.clear();
		solids.clear();
		sharks.clear();
		people.clear();
		logs.clear();

		currentLevel = new int[level00.length][level00[0].length];
		for(int i = 0; i < currentLevel.length; i++){
			currentLevel[i] = Arrays.copyOf(level00[i], level00[i].length);
		}

		currentMap = map00;
		generateSolidsFrom(currentLevel);
		//Hard-coded spawn points for people in lvl1
		people.add(new Person(190, 160, 'x', this));
		people.add(new Person(150, 200, 'x', this));
		people.add(new Person(142, 114, 'x', this));
		people.add(new Person(102, 158, 'x', this));
		people.add(new Person(215, 223, 'x', this));
		
		people.add(new Person(437, 90, 'x', this));
		people.add(new Person(490, 112, 'x', this));
		people.add(new Person(460, 230, 'x', this));
		
		people.add(new Person(226, 380, 'x', this));
		people.add(new Person(313, 386, 'x', this));
		
		//TODO test remove later
		logs.add(new Wood(157, 159, this));
		
		Shark testShark = new Shark(320, 240, this);
		testShark.velX = 0.5f;
		sharks.add(testShark);

		player = new Player(320, 240, this);
		camX = player.x - Gdx.graphics.getWidth() / 2;
		camY = player.y - Gdx.graphics.getHeight() / 2;
		Gdx.input.setInputProcessor(player);
	}

	@Override
	public void preTransitionOut(Transition t){

	}

	@Override
	public void render(GameContainer gc, Graphics g){
		g.setBackgroundColor(new Color(97 / 255f, 162 / 255f, 255 / 255f, 1));

		g.translate((float) Math.round(camX), (float) Math.round(camY));

		g.drawSprite(currentMap, 0, 0);
		
		/*g.drawRect(Gdx.input.getX() + camX, Gdx.input.getY() + camY, 8, 18);
		System.out.println((Gdx.input.getX() + camX) + ", " + (Gdx.input.getY() + camY));*/

		//Solids rendering 
		/*for(int i = 0; i < solids.size(); i++){
			solids.get(i).render(g);
		}*/

		renderSharks(g);
		renderBridges(g);
		renderLogs(g);
		renderPeople(g);

		player.render(g);

		//Draw transparent bridge icon
		if(!paused && !movingToNextLevel && bridgeSelected){
			int mx = Gdx.input.getX() / TILE_SIZE * TILE_SIZE;
			int my = Gdx.input.getY() / TILE_SIZE * TILE_SIZE;
			g.drawSprite(bridgeTile, mx + camX, my + camY);
		}
		
		g.setColor(Color.BLACK);
		g.drawString("Bridge count: " + bridgeCount, camX + 8, camY + 12);

		if(movingToNextLevel){
			float middleX = Gdx.graphics.getWidth() / 2 - scoreMenu.getWidth() / 2 + camX;
			float middleY = Gdx.graphics.getHeight() / 2 - scoreMenu.getHeight() / 2 + camY;
			g.drawSprite(scoreMenu, middleX, middleY);
			//g.drawString("BRIDGES USED: " + bridges.size(), middleX + camX + 142, middleY + camY + 117);
			g.drawString(bridgeCount + "", middleX + 156, middleY + 67);
			g.drawString((bridgeCount * 5) + "", middleX + 128, middleY + 117);
		}
		if(paused){
			//g.setColor(Color.RED);
			//g.drawString("Are you sure you want to quit? Y or N", camX + 220, camY + 240);
			float middleX = Gdx.graphics.getWidth() / 2 - pauseMenu.getWidth() / 2 + camX;
			float middleY = Gdx.graphics.getHeight() / 2 - pauseMenu.getHeight() / 2 + camY;
			g.drawSprite(pauseMenu, middleX, middleY);
		}
		if(gameOver){
			g.drawString("Game over! Press Escape to go back to the main menu", camX + 160, camY + 240);
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){
		if(!paused && !gameOver && !movingToNextLevel){
			player.update(delta);
			updateSharks(delta);
			updateBridges(delta);
			updateLogs(delta);
			updatePeople(delta);

			//Camera movement, viewport is 20 x 15 tiles, limit to current level size
			if(Gdx.input.isKeyJustPressed(Player.LEFT)){
				if(camX > 0){
					camX -= TILE_SIZE;
				}
			}
			if(Gdx.input.isKeyJustPressed(Player.RIGHT)){
				if(camX + 20 * TILE_SIZE < currentLevel[0].length * TILE_SIZE){
					camX += TILE_SIZE;
				}
			}
			if(Gdx.input.isKeyJustPressed(Player.UP)){
				if(camY > 0){
					camY -= TILE_SIZE;
				}
			}
			if(Gdx.input.isKeyJustPressed(Player.DOWN)){
				if(camY + 15 * TILE_SIZE < currentLevel.length * TILE_SIZE){
					camY += TILE_SIZE;
				}
			}

			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				paused = true;
				MainMenu.selectSound.play();
			}
		}
		else{
			if(gameOver){
				if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new NullTransition());
				}
			}
			else if(paused){
				if(Gdx.input.isKeyJustPressed(Keys.Y)){
					sm.enterGameScreen(MainMenu.ID, new FadeOutTransition(), new NullTransition());
				}
				if(Gdx.input.isKeyJustPressed(Keys.N)){
					paused = false;
				}
			}
			else if(movingToNextLevel){
				if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
					MainMenu.selectSound.play(); //TODO start of level sound?
					moveToNextLevel();
				}
			}
		}
	}

	public void moveToNextLevel(){
		if(levelCount == 0){
			people.clear();
			bridges.clear();
			sharks.clear();
			bridgeCount = 0;
			solids.clear();
			movingToNextLevel = false;
			player.x = 320;
			player.y = 240;
			levelCount = 1;
			currentLevel = new int[level01.length][level01[0].length];
			for(int i = 0; i < currentLevel.length; i++){
				currentLevel[i] = Arrays.copyOf(level01[i], level01[i].length);
			}

			currentMap = map01;
			generateSolidsFrom(currentLevel);
			//Hard-coded shark spawns for new level
			Shark s1 = new Shark(330, 110, this);
			s1.velX = 0.5f;
			Shark s2 = new Shark(610, 370, this);
			s2.velX = 0.5f;
			sharks.add(s1);
			sharks.add(s2);
			
			spawnPeopleFor(level01);
		}
		else if(levelCount == 1){
			System.out.println("Game won!");
		}
	}

	//Returns the number of islands in a 2D array
	public int countIslands(int[][] map){
		boolean[][] visited = new boolean[currentLevel.length][currentLevel[0].length];
		int count = 0;
		for(int i = 0; i < visited.length; i++){
			for(int j = 0; j < visited[i].length; j++){
				//Don't count bridges (negative values) as starting points for islands
				if(map[i][j] > 0 && !visited[i][j]) {
					DFS(map, i, j, visited);
					count++;
				}
			}
		}
		return count;
	}

	private void DFS(int[][] map, int row, int col, boolean[][] visited){
		int[] neighborRow = {-1, 1, 0, 0};
		int[] neighborCol = {0, 0, -1, 1};

		visited[row][col] = true;

		for(int i = 0; i < 4; i++){
			int nRow = row + neighborRow[i];
			int nCol = col + neighborCol[i];
			//Check bounds and DFS unvisited solid cells
			if(nRow >= 0 && nRow < visited.length && nCol >= 0 && nCol < visited[0].length
					&& !visited[nRow][nCol] && map[nRow][nCol] != 0){
				DFS(map, nRow, nCol, visited);
			}
		}
	}

	public void renderBridges(Graphics g){
		for(int i = 0; i < bridges.size(); i++){
			bridges.get(i).render(g);
		}
	}

	public void updateBridges(float delta){
		for(int i = 0; i < bridges.size(); i++){
			bridges.get(i).update(delta);
		}
	}
	
	public void renderSharks(Graphics g){
		for(int i = 0; i < sharks.size(); i++){
			sharks.get(i).render(g);
		}
	}

	public void updateSharks(float delta){
		for(int i = 0; i < sharks.size(); i++){
			sharks.get(i).update(delta);
		}
	}
	
	public void renderPeople(Graphics g){
		for(int i = 0; i < people.size(); i++){
			people.get(i).render(g);
		}
	}

	public void updatePeople(float delta){
		for(int i = 0; i < people.size(); i++){
			people.get(i).update(delta);
		}
	}
	
	public void renderLogs(Graphics g){
		for(int i = 0; i < logs.size(); i++){
			logs.get(i).render(g);
		}
	}

	public void updateLogs(float delta){
		for(int i = 0; i < logs.size(); i++){
			logs.get(i).update(delta);
		}
	}
	
	public void generateSolidsFrom(int[][] map){
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j] > 0){
					Block s = new Block(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
					solids.add(s);
				}
			}
		}
	}
	
	public void spawnPeopleFor(int[][] map){
		//Random spawning based on tiles
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j] == 4){
					Person randPerson = new Person(j*TILE_SIZE, i*TILE_SIZE, 'x', this);
					people.add(randPerson);
				}
			}
		}
	}

	public void printMapData(){
		for(int i = 0; i < currentLevel.length; i++){
			for(int j = 0; j < currentLevel[i].length; j++){
				System.out.print(currentLevel[i][j] + ", ");
			}
			System.out.println();
		}
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

	@Override
	public void interpolate(GameContainer gc, float delta){
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResize(int width, int height) {
	}

	@Override
	public void onResume() {
	}

}
