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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.Preferences;

/*
 * the game's first screen
 * displays buttons
 */
public class MainMenuScreen implements Screen {

	// reference to the game
	BushidoBlocks game;

	Stage stage;

	// rendering stuff
	OrthographicCamera camera;
	SpriteBatch batch;

	// background image
	Texture background;

	// buttons
	Texture buttonOff;
	Rectangle buttonOffRectangle;

	// game preferences
	Preferences prefs;

	public MainMenuScreen(final BushidoBlocks game) {
		this.game = game;

		stage = new Stage(480, 800, false);
		stage.setViewport(480, 800, false);
		stage.getCamera().position.set(480 / 2, 800 / 2, 0);

		// back button should exit the app here
		Gdx.input.setCatchBackKey(false);

		/* the rendering stuff */
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 480, 800);
		batch = new SpriteBatch();

		/* load the background image */
		background = new Texture(Gdx.files.internal("main_menu_background.png"));

		/* buttons */
		buttonOff = new Texture(Gdx.files.internal("button_off.png"));

		/* the preferences */
		prefs = Gdx.app.getPreferences("My Preferences");

		// set all the prefs if they don't exist
		if (!prefs.contains("soundOn")) {
			prefs.putBoolean("soundOn", true);
			prefs.flush();
		}
		if (!prefs.contains("musicOn")) {
			prefs.putBoolean("musicOn", true);
			prefs.flush();
		}
		if (!prefs.contains("lastScore")) {
			prefs.putInteger("lastScore", 0);
			prefs.flush();
		}
		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
			prefs.flush();
		}

		Skin skin;
		FileHandle skinFile = Gdx.files.internal("data/uiskin.json");
		skin = new Skin(skinFile);

		Texture startButtonUpTexture = new Texture(
				Gdx.files.internal("main_menu_begin_button.png"));
		Texture startButtonDownTexture = new Texture(
				Gdx.files.internal("main_menu_begin_button_down.png"));
		TextureRegionDrawable startButtonUpRegion = new TextureRegionDrawable(
				new TextureRegion(startButtonUpTexture));
		TextureRegionDrawable startButtonDownRegion = new TextureRegionDrawable(
				new TextureRegion(startButtonDownTexture));

		Button startButton = new Button(startButtonUpRegion,
				startButtonDownRegion);
		startButton.setPosition((480 / 2) - (255 / 2), 300);
		startButton.addListener(new InputListener() {

			Sound slash = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (prefs.getBoolean("soundOn"))
					slash.play();
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.setScreen(new PlayScreen(game));
				slash.dispose();
			}
		});
		stage.addActor(startButton);

		Texture musicButtonUpTexture = new Texture(
				Gdx.files.internal("main_menu_music_button.png"));
		Texture musicButtonDownTexture = new Texture(
				Gdx.files.internal("main_menu_music_button_down.png"));
		TextureRegionDrawable musicButtonUpRegion = new TextureRegionDrawable(
				new TextureRegion(musicButtonUpTexture));
		TextureRegionDrawable musicButtonDownRegion = new TextureRegionDrawable(
				new TextureRegion(musicButtonDownTexture));

		Button musicButton = new Button(musicButtonUpRegion,
				musicButtonDownRegion);
		musicButton.setPosition((480 / 2) - 171, 200);
		musicButton.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (prefs.getBoolean("musicOn") == false) {
					prefs.putBoolean("musicOn", true);
					prefs.flush();
				} else if (prefs.getBoolean("musicOn") == true) {
					prefs.putBoolean("musicOn", false);
					prefs.flush();
				}
			}
		});
		stage.addActor(musicButton);

		Texture soundButtonUpTexture = new Texture(
				Gdx.files.internal("main_menu_sound_button.png"));
		Texture soundButtonDownTexture = new Texture(
				Gdx.files.internal("main_menu_sound_button_down.png"));
		TextureRegionDrawable soundButtonUpRegion = new TextureRegionDrawable(
				new TextureRegion(soundButtonUpTexture));
		TextureRegionDrawable soundButtonDownRegion = new TextureRegionDrawable(
				new TextureRegion(soundButtonDownTexture));

		Button soundButton = new Button(soundButtonUpRegion,
				soundButtonDownRegion);
		soundButton.setPosition((480 / 2), 200);
		soundButton.addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (prefs.getBoolean("soundOn") == false) {
					prefs.putBoolean("soundOn", true);
					prefs.flush();
				} else if (prefs.getBoolean("soundOn") == true) {
					prefs.putBoolean("soundOn", false);
					prefs.flush();
				}
			}
		});
		stage.addActor(soundButton);

		TextButton swarmButton = new TextButton("Leaderboards", skin);
		swarmButton.setPosition(90, 70);
		swarmButton.addListener(new InputListener() {

			Sound slash = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (prefs.getBoolean("soundOn"))
					slash.play();
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				game.getLeaderboard().showDash();
				slash.dispose();
			}
		});
		stage.addActor(swarmButton);

		Gdx.input.setInputProcessor(stage);
	}

	public void update(float deltaTime) {
	}

	public void draw(float deltaTime) {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		// rendering
		batch.begin();
		// draw background
		batch.draw(background, 0, 0);
		batch.end();

		// handle input
		if (Gdx.input.justTouched()) {

			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
		}

		// draw the actors
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		batch.begin();
		if (prefs.getBoolean("soundOn") == false) {
			batch.draw(buttonOff, (480 / 2) - 20, 210);
		}
		if (prefs.getBoolean("musicOn") == false) {
			batch.draw(buttonOff, (480 / 2) - 151, 210);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(480, 800, false);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		background.dispose();
		buttonOff.dispose();
		batch.dispose();
		stage.clear();
		stage.dispose();
	}
}
