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

	public int[][] currentLevel;

	public boolean paused = false;
	public boolean gameOver = false;

	public float camX, camY;

	public ArrayList<Block> solids;
	public ArrayList<Bridge> bridges;
	public Sprite map00;
	public Sprite currentMap;

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

		map00 = new Sprite(new Texture(Gdx.files.internal("level00.png")));

		adjustSprite(map00, bridgeTile);
		resizeSprite(map00, bridgeTile);

		currentMap = map00;

		bridges = new ArrayList<Bridge>();

		solids = new ArrayList<Block>();

		solids.add(new Block(100, 50, 30, 55, this));
		solids.add(new Block(0, 234, 100, 16, this));
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){
		paused = false;
		gameOver = false;
		bridgeSelected = false;
	}

	@Override
	public void preTransitionIn(Transition t){
		paused = false;
		gameOver = false;
		bridgeSelected = true;

		currentLevel = new int[level00.length][level00[0].length];
		for(int i = 0; i < currentLevel.length; i++){
			currentLevel[i] = Arrays.copyOf(level00[i], level00[i].length);
		}

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

		g.translate((float) Math.round(camX), (float) Math.round(camY)); //Camera movement TODO make it tile-based

		g.drawSprite(currentMap, 0, 0);

		if(!paused && bridgeSelected){
			int mx = Gdx.input.getX() / TILE_SIZE * TILE_SIZE;
			int my = Gdx.input.getY() / TILE_SIZE * TILE_SIZE;
			g.drawSprite(bridgeTile, mx, my);
		}

		//Solids rendering 
		for(int i = 0; i < solids.size(); i++){
			solids.get(i).render(g);
		}

		renderBridges(g);

		player.render(g);

		if(paused){
			g.setColor(Color.RED);
			g.drawString("Are you sure you want to quit? Y or N", camX + 220, camY + 240);
		}
		if(gameOver){
			g.drawString("Game over! Press Escape to go back to the main menu", camX + 160, camY + 240);
		}
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){
		if(!paused && !gameOver){
			player.update(delta);
			updateBridges(delta);

			camX = player.x - Gdx.graphics.getWidth() / 2;
			camY = player.y - Gdx.graphics.getHeight() / 2;

			if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
				paused = true;
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
		}
	}

	//Returns the number of islands in a 2D array
	//TODO isolated bridges count as islands, need to fix
	public int countIslands(int[][] map){
		boolean[][] visited = new boolean[currentLevel.length][currentLevel[0].length];
		int count = 0;
		for(int i = 0; i < visited.length; i++){
			for(int j = 0; j < visited[i].length; j++){
				if(map[i][j] != 0 && !visited[i][j]) {
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
