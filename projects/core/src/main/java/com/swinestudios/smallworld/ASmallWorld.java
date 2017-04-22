package com.swinestudios.smallworld;

import org.mini2Dx.core.game.ScreenBasedGame;

public class ASmallWorld extends ScreenBasedGame {
	
	public static final String GAME_IDENTIFIER = "com.swinestudios.smallworld";
	
	@Override
    public void initialise() {
		this.addScreen(new MainMenu());
		this.addScreen(new Gameplay());
    }
    
    @Override
    public int getInitialScreenId(){
    	return MainMenu.ID;
    }
}
