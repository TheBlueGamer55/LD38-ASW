package com.swinestudios.smallworld;

import org.mini2Dx.core.game.GameContainer;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.screen.GameScreen;
import org.mini2Dx.core.screen.ScreenManager;
import org.mini2Dx.core.screen.Transition;
import org.mini2Dx.core.screen.transition.FadeOutTransition;
import org.mini2Dx.core.screen.transition.NullTransition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class MainMenu implements GameScreen{

	public static int ID = 1;

	@Override
	public int getId(){
		return ID;
	}

	@Override
	public void initialise(GameContainer gc){
		
	}

	@Override
	public void postTransitionIn(Transition t){

	}

	@Override
	public void postTransitionOut(Transition t){

	}

	@Override
	public void preTransitionIn(Transition t){

	}

	@Override
	public void preTransitionOut(Transition t){

	}

	@Override
	public void render(GameContainer gc, Graphics g){
		g.drawString("This is the main menu", 40, 40);
	}

	@Override
	public void update(GameContainer gc, ScreenManager<? extends GameScreen> sm, float delta){		
		if(Gdx.input.isKeyJustPressed(Keys.ENTER)){
			sm.enterGameScreen(Gameplay.ID, new FadeOutTransition(), new NullTransition());
		}
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	@Override
	public void interpolate(GameContainer gc, float delta){
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResize(int width, int height){
	}

	@Override
	public void onResume() {
	}
}
