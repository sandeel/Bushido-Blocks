/*
	Copyright © 2012 Sandeel Software
	
	This file is part of Bushido Blocks.
	    
	Bushido Blocks is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.sandeel.bushidoblocks;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

/*
 * the input processor for gameplay (PlayScreen)
 */

public class GameInputProcessor implements InputProcessor {
	
	// reference to the game
	BushidoBlocks game;

	public GameInputProcessor(BushidoBlocks game) {
		this.game = game;
	}

	@Override
	public boolean keyDown (int keycode) {
		
		//if escape or back key pressed go to main menu
		if (keycode == (Keys.BACK) || keycode == (Keys.ESCAPE)){
			game.setScreen(new MainMenuScreen(game));
		}   
		
		return false;
	}

	@Override
	public boolean keyUp (int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped (char character) {
		return false;
	}

	@Override
	public boolean touchDown (int x, int y, int pointer, int button) {
      return false;
   }

   @Override
   public boolean touchUp (int x, int y, int pointer, int button) {
      return false;
   }

   @Override
   public boolean touchDragged (int x, int y, int pointer) {
      return false;
   }

   public boolean touchMoved (int x, int y) {
      return false;
   }

   @Override
   public boolean scrolled (int amount) {
      return false;
   }

   @Override
   public boolean mouseMoved(int screenX, int screenY) {
	   return false;
   }
}