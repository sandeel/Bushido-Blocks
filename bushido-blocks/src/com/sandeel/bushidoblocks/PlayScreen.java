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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.Preferences;

/*
 * the main gameplay screen
 */
public class PlayScreen implements Screen {
    //reference to the application
    private BushidoBlocks game;

    //Scene2d stage
    private Stage stage;

    //background image for behind the blocks
    private Texture background;

    //background song
    private Music music;

    //sound effects
    private Sound whoosh;
    private Sound crack;

    //fonts
    private BitmapFont font;
    private BitmapFont numbersFont;
    private BitmapFont scoreFont;
    private BitmapFont scorePopupFont;

    //countdown
    private GameTimer timer;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    //grid the blocks go on
    private Grid grid;

    //the player's points/score
    private int points;

    //game preferences
    private Preferences prefs;

    private GridSpace space;
    private List<GridSpace> gridSpaces;
    private Iterator<GridSpace> it;

    //style for the labels (score etc.)
    private LabelStyle labelStyle;

    //is the game paused (used when starting game)
    private boolean paused;

    //coloured block images
    private Texture[] blockImages;

    //points to a block texture
    private Texture blockPointer;

    public PlayScreen (BushidoBlocks game) {
        this.game = game;
        stage = new Stage(480, 800, false);
        
        //back key shouldn't exit app here
        Gdx.input.setCatchBackKey(true);

        //preferences
        prefs = Gdx.app.getPreferences("My Preferences");

        //pause the game intially until player touches screen
        paused = true;

        //load the images
        background = new Texture(Gdx.files.internal("sky.png"));
        blockImages = new Texture[] {
            new Texture(Gdx.files.internal("GREEN.png")),
            new Texture(Gdx.files.internal("RED.png")),
            new Texture(Gdx.files.internal("YELLOW.png")),
            new Texture(Gdx.files.internal("PINK.png")),
            new Texture(Gdx.files.internal("BLUE.png")),
            new Texture(Gdx.files.internal("super_horizontal.png")),
            new Texture(Gdx.files.internal("super_vertical.png")),
            new Texture(Gdx.files.internal("super_2ways.png"))
        };

        //font
        font = new BitmapFont(Gdx.files.internal("data/nuku.fnt"), Gdx.files.internal("data/nuku.png"), false);
        numbersFont = new BitmapFont(Gdx.files.internal("data/countdown.fnt"), Gdx.files.internal("data/countdown.png"), false);
        scoreFont = new BitmapFont(Gdx.files.internal("data/score.fnt"), Gdx.files.internal("data/score.png"), false);
        scorePopupFont = new BitmapFont(Gdx.files.internal("data/score_popup.fnt"), Gdx.files.internal("data/score_popup.png"), false);

        //style
        com.badlogic.gdx.graphics.Color color = new com.badlogic.gdx.graphics.Color(255f, 255f, 255f, 50);
        labelStyle = new LabelStyle(scorePopupFont, color);

        //sound effects
        whoosh = Gdx.audio.newSound(Gdx.files.internal("whoosh.wav"));
        crack = Gdx.audio.newSound(Gdx.files.internal("crack.wav"));

        //music (not loaded unless sound turned on!!)
        if (prefs.getBoolean("musicOn") == true) {
            music = Gdx.audio.newMusic(Gdx.files.internal("crystals.ogg"));
        }
        
        System.err.println("here");

        //play music if setting turned on
        if (prefs.getBoolean("musicOn") == true) {
            music.setLooping(true);
            music.play();
        }

        //the rendering stuff
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);
        batch = new SpriteBatch();

        //the grid for the blocks
        grid = new Grid();

        //set initial points to 0
        points = 0;

        //load timer
        timer = new GameTimer(60000);

        Gdx.input.setCatchBackKey(true);

        GameInputProcessor inputProcessor = new GameInputProcessor(game);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render (float delta) {
           Gdx.gl.glClearColor(0, 0, 0.2f, 1);
           Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

           //if out of time
           if (timer.getTimeRemaining() <= 0) {

               music.stop();

               //save the score
               prefs.putInteger("lastScore", points);
               prefs.flush();

               //update high score
               if (points > prefs.getInteger("highScore")) {
                   prefs.putInteger("highScore", points);
                   prefs.flush();
               }

               game.setScreen(new GameOverScreen(game));
           }

           camera.update();
           batch.setProjectionMatrix(camera.combined);
           batch.begin();

           //draw background
           batch.draw(background, 0, 0);

           //display remaining time
           font.draw(batch, "time", 60, (800 - 100));
           numbersFont.draw(batch, String.valueOf(timer.getTimeRemainingInSeconds()), 80, (800 - 150));

           //display score
           font.draw(batch, "score", (480 - 180), (800 - 100));
           scoreFont.draw(batch, String.valueOf(points), (480 - 170), (800 - 150));

           gridSpaces = grid.getSpaces();
           it = gridSpaces.iterator();

           while(it.hasNext()) {
                    space = it.next();

                    // move block down if needed
                    if (space.hasBlock()) {
                        if (!space.onBottomRow() && !space.getGridSpaceBelow().hasBlock()) {
                            space.getGrid().moveBlock(space,space.getGridSpaceBelow());
                        }
                    }

                    if (space.hasBlock()) {
                        blockPointer = blockImages[space.getBlock().getColour().ordinal()];

                        //update block
                        space.getBlock().update();

                        //draw block
                        batch.draw(blockPointer, space.getBlock().getRectangle().getX(), space.getBlock().getRectangle().getY() + space.getBlock().getYOffset());
                    }
           }
           batch.end();

           /* add new blocks if spaces in top row empty */
           for (GridSpace space : grid.getTopRow()) {

               if (!space.hasBlock()) {
                   boolean hasSuperBlock = grid.hasSuperBlock();

                   /* add a super block if there are no matches and no super blocks and no spaces left
                    * get out of jail card!!
                    */
                   if (!hasSuperBlock && grid.countEmptySpaces() == 1 && !grid.hasMatch()) {
                       space.spawnBlock(COLOURS.SUPER_2WAYS);
                   }
                   // chance of super block if none on table
                   else if (!hasSuperBlock) {
                            int chance = 18;
                            int c = (int)(Math.random() * (chance));
                            if (c == 1) {
                                // if the space is on an edge make it a two ways block
                                // if not, a random super block
                                if (!space.hasGridSpaceBelow()
                                        || !space.hasGridSpaceLeft()
                                        || !space.hasGridSpaceRight()) {
                                    space.spawnBlock(COLOURS.SUPER_2WAYS);
                                } else {
                                    space.spawnSuperBlock();
                                }
                            } else {
                                space.spawnBlock();
                            }
                            //normal block if already superblocks
                        } else {
                            space.spawnBlock();
                   }

                   // give the block an offset to make it fall
                   space.getBlock().setYOffset(space.getBlock().getYOffset() + (60));
                   if (space.getGridSpaceBelow().hasBlock()) {
                       space.getBlock().setYOffset(space.getGridSpaceBelow().getBlock().getYOffset() + 200);
                   }
               }
           }

           /* if user has tapped screen or clicked */
           if(Gdx.input.justTouched()) {

               if (paused == true) {
                   paused = false;
                   timer.start();
               }

                  Vector3 touchPos = new Vector3();
                  touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                  camera.unproject(touchPos);

                  gridSpaces = grid.getSpaces();
                  it = gridSpaces.iterator();
                  while(it.hasNext()) {
                      space = it.next();
                      if(space.hasBlock() && space.getBlock().getRectangle().contains(touchPos.x, touchPos.y)) {

                          /* handle super blocks */
                          if (space.getBlock().getColour() == COLOURS.SUPER_HORIZONTAL) {
                              if (prefs.getBoolean("soundOn") == true) {
                                  crack.play();
                              }
                              Gdx.input.vibrate(200);
                              for (GridSpace targetSpace : grid.getRow(space.getY())) {
                                  targetSpace.deleteBlock();
                              }
                          } else if (space.getBlock().getColour() == COLOURS.SUPER_VERTICAL) {
                              if (prefs.getBoolean("soundOn") == true) {
                                  crack.play();
                              }
                              Gdx.input.vibrate(200);
                              for (GridSpace targetSpace : grid.getColumn(space.getX())) {
                                  targetSpace.deleteBlock();
                              }
                          } else if (space.getBlock().getColour() == COLOURS.SUPER_2WAYS) {
                              if (prefs.getBoolean("soundOn") == true) {
                                  crack.play();
                                  }
                              Gdx.input.vibrate(200);
                              for (GridSpace targetSpace : grid.getRow(space.getY())) {
                                  targetSpace.deleteBlock();
                              }
                              for (GridSpace targetSpace : grid.getColumn(space.getX())) {
                                  targetSpace.deleteBlock();
                              }
                              Gdx.input.vibrate(200);
                              /* now handle normal blocks */
                          } else {
                              HashSet<GridSpace> emptyMatches = new HashSet<GridSpace>();
                              HashSet<GridSpace> matches = space.getGrid().getMatches(space.getX(), space.getY(), space.getBlock().getColour(), emptyMatches);

                              /* if got a match */
                              if (matches.size() >= 3) {
                                  // play a sound
                                  if (prefs.getBoolean("soundOn") == true) {
                                      whoosh.play();
                                  }
                                  Gdx.input.vibrate(50);

                                  for (GridSpace match : matches) {
                                      // delete the blocks
                                      match.deleteBlock();
                                  }
                                  // add time
                                  int timeReceived = (1 * (matches.size()) / 4);
                                  timer.addTime(timeReceived);

                                  // give player points for each block
                                  int amount = 10*matches.size();

                                  // add a multiplier for large amounts of blocks
                                  int bonus = amount * (matches.size() - 2);

                                  points += (amount + bonus);

                                  Label label = new Label(String.valueOf(amount + bonus), labelStyle);

                                  label.setPosition(touchPos.x - (label.getWidth()/2), touchPos.y);

                                  MoveToAction action = new MoveToAction();
                                  action.setPosition(touchPos.x - (label.getWidth()/2), (touchPos.y + 800));
                                  action.setDuration(1.2f);

                                  label.addAction(Actions.sequence(action, new RemoveActorAction()));

                                  stage.addActor(label);
                              }
                          }
                      }
                   }
               }

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
        background.dispose();

        music.stop();
        music.dispose();

        whoosh.stop();
        whoosh.dispose();

        crack.stop();
        crack.dispose();

        // dispose of all the images in blockImage array
        for (Texture blockImage : blockImages) {
            blockImage.dispose();
        }

        font.dispose();
        numbersFont.dispose();
        scoreFont.dispose();
        scorePopupFont.dispose();

        stage.clear();
        stage.dispose();
    }
}
