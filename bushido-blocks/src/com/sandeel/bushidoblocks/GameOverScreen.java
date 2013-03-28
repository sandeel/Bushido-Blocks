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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.Preferences;

/*
 * the screen shown when the game is over
 * displays score etc and allows player to return to main menu or try again
 */

public class GameOverScreen implements Screen {

	// reference to the game
	BushidoBlocks game;

	Stage stage;

	// rendering stuff
	OrthographicCamera camera;
	SpriteBatch batch;

	//background image
	Texture bg;

	// the game preferences
	Preferences prefs;

	// font used for rendering text
	BitmapFont font;

	int lastScore;
	int highScore;

	Texture headerTexture;

	// to stop bug causing jump to main menu
	GameTimer timer;

	public GameOverScreen (final BushidoBlocks game) {

		this.game = game;

		stage = new Stage(480, 800, false);

		timer = new GameTimer(200);
		timer.start();

		/* the rendering stuff */
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		batch = new SpriteBatch();

		/* font
		 * needs to be changed */
		font = new BitmapFont(Gdx.files.internal("data/nuku_small.fnt"), Gdx.files.internal("data/nuku_small.png"), false);

		/* preferences */
		prefs = Gdx.app.getPreferences("My Preferences");

		/* load the background image */
		bg = new Texture(Gdx.files.internal("game_over_background.png"));

		Texture tryAgainButtonUpTexture = new Texture(Gdx.files.internal("try_again_button.png"));
		Texture tryAgainButtonDownTexture = new Texture(Gdx.files.internal("try_again_button_down.png"));
		TextureRegionDrawable tryAgainButtonUpRegion = new TextureRegionDrawable(new TextureRegion(tryAgainButtonUpTexture));
		TextureRegionDrawable tryAgainButtonDownRegion = new TextureRegionDrawable(new TextureRegion(tryAgainButtonDownTexture));

		Button tryAgainButton = new Button(tryAgainButtonUpRegion, tryAgainButtonDownRegion);
		tryAgainButton.setPosition(480-173, -4);
		tryAgainButton.addListener(new InputListener() {

			Sound slash = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));

			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (prefs.getBoolean("soundOn")) slash.play();
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new PlayScreen(game));
				slash.dispose();
			}
		}
		);
		stage.addActor(tryAgainButton);

		Texture menuButtonUpTexture = new Texture(Gdx.files.internal("menu_button.png"));
		Texture menuButtonDownTexture = new Texture(Gdx.files.internal("menu_button_down.png"));
		TextureRegionDrawable menuButtonUpRegion = new TextureRegionDrawable(new TextureRegion(menuButtonUpTexture));
		TextureRegionDrawable menuButtonDownRegion = new TextureRegionDrawable(new TextureRegion(menuButtonDownTexture));

		Button menuButton = new Button(menuButtonUpRegion, menuButtonDownRegion);
		menuButton.setPosition(0, -4);
		menuButton.addListener(new InputListener() {

			Sound slash = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));

			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (prefs.getBoolean("soundOn")) slash.play();
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (timer.getTimeRemaining() <= 0) { game.setScreen(new MainMenuScreen(game)); }
				slash.dispose();
			}
		}
		);
		stage.addActor(menuButton);

		headerTexture = new Texture(Gdx.files.internal("game_over_header.png"));

		lastScore = prefs.getInteger("lastScore");
		highScore = prefs.getInteger("highScore");

		game.getLeaderboard().submitScore(lastScore);

		Gdx.input.setInputProcessor(stage);

		// app should exit if back key pressed here
		Gdx.input.setCatchBackKey(false);
	}

	public void update (float deltaTime) {
	}

	public void draw (float deltaTime) {
	}

	@Override
	public void render (float delta) {
	Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

	camera.update();

	batch.setProjectionMatrix(camera.combined);

	// do the rendering
	batch.begin();
	//draw background
	batch.draw(bg, 0, 0);
	batch.draw(headerTexture, 0, 110);

	font.draw(batch, "your score: " + lastScore, 140, 215);
	font.draw(batch, "high score: " + highScore, 140, 185);
	batch.end();

	   // draw the actors
	   stage.act(Gdx.graphics.getDeltaTime());
	   stage.draw();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void show () {
	}

	@Override
	public void hide () {
		dispose();
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
		bg.dispose();
		font.dispose();
		headerTexture.dispose();
		batch.dispose();

		stage.clear();
		stage.dispose();
	}
}
