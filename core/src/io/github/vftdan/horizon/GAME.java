package io.github.vftdan.horizon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.maps.tiled.tiles.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import io.github.vftdan.horizon.gameMap.*;
import io.github.vftdan.horizon.gameMap.GameMap.GameObject;
import io.github.vftdan.horizon.gameMap.GameMap.GraphicRepresentationVariant;
import io.github.vftdan.horizon.gameMap.objects.AbstractTiledGameObject;
import io.github.vftdan.horizon.gameMap.objects.CreatureGameObject;
import io.github.vftdan.horizon.gameMap.objects.ExitGameObject;
import io.github.vftdan.horizon.gameMap.objects.FloorGameObject;
import io.github.vftdan.horizon.gameMap.objects.IGameObject;
import io.github.vftdan.horizon.gameMap.objects.PlayerGameObject;
import io.github.vftdan.horizon.gameMap.objects.PuzzleGateGameObject;
import io.github.vftdan.horizon.gameMap.objects.WallGameObject;
import io.github.vftdan.horizon.screens.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.script.*;

public class GAME extends AbstractAppAdapter {
	//SpriteBatch batch;
	//Texture img;
	//TextureAtlas atl;
	//AtlasRegion reg_cloud;
	//float winHeight;
	//float winWidth;
	//Sprite spr_cloud;
	public static GAME instance;
	TextureAtlas atlas;
	//public ArrayList<Vector2> touches;
	//public ArrayList<Sprite> touchSprites;
	public AppScreen currentScreen;
	public HashMap<String, AppScreen> screens = new HashMap<String, AppScreen>();
	public Language curLang;
	
	//<tiled test>
	public Texture img;
	public TiledMap tiledMap;
	public TiledMapRenderer tiledMapRenderer;
	public int[] bg_layers;
	public int[] fg_layers;
	public GameSession session;
	public GameInitializer initializer;
	//TiledMapTileLayer gameLayer;
	//</tiled test>
	
	//public class MyActor extends Actor {
	//	Texture texture = new Texture(Gdx.files.internal("ter.png"));
	//	float speedX = (float)(Math.random() * 80 - 40), speedY = (float)(Math.random() * 80 - 40);
	//	public boolean started;
		
		/*public MyActor() {
			addListener(new InputListener() {
				public boolean touchDown(InputEvent e, float x, float y, int touchIndex, int button) {
					MyActor ma = ((MyActor)e.getTarget());
					ma.started = !ma.started;
					return true;
				}
			});
			setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
		}
		@Override
		public void draw(Batch batch, float alpha) {
			batch.draw(texture, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
					this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
					texture.getWidth(), texture.getHeight(), false, false);
		}*/
		
		/*@Override
		public void act(float delta) {
			if(!started) return;
			actorX += speedX * delta;
			actorY += speedY * delta;
			float h = GAME.instance.screenDims.y, w = GAME.instance.screenDims.x;
			if(actorX > w) actorX -= w;
			if(actorX < 0) actorX += w;
			if(actorY > h) actorY -= h;
			if(actorY < 0) actorY += h;
		}*/
	//}
	
	public void stdToGl(Vector2 v) {
		v.y = screenDims.y - v.y;
	}
	public void glToStd(Vector2 v) {
		v.y = screenDims.y - v.y;
	}
	
	public GAME() {
		instance = this;
	}
	
	//@SuppressWarnings("serial")
	public void create () {
		super.create();
		FileHandle testTask = GameSaver.child("testtask.js");
		if(!testTask.exists()) {
			try {
				testTask.writeString(Gdx.files.internal("demotask.js").readString(), true);
			} catch(RuntimeException e) {
				e.printStackTrace();
			}
		}
		atlas = new TextureAtlas(Gdx.files.internal("ter.atlas"));
		/*batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		addDisposable(batch);
		addDisposable(img);
		//winHeight = Gdx.graphics.getHeight();
		//winWidth = Gdx.graphics.getWidth();
		bgc = Color.RED;
		atl = new TextureAtlas(Gdx.files.internal("ter.atlas"));
		reg_cloud = atl.findRegion("cloudSmall");
		spr_cloud = new Sprite(reg_cloud);
		spr_cloud.setScale(16);
		spr_cloud.setCenter(64,  64);*/
		/*
		try {
			ScriptExecutor e = new ScriptExecutor();
			ScriptExecutor.JsObject o = new ScriptExecutor.JsObject(e, e.engine.eval("function(a){return a + 1}"));
			System.out.println(((Array<Integer>)e.toJsArray(new Object[]{1, 2, 3, 4, 5}).to(Array.class)).get(0));
			System.out.println(e.eval("Array"));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		*/
		/*
		InputMultiplexer im = new InputMultiplexer();
		GestureDetector gd = new GestureDetector(this);
		im.addProcessor(gd);
		im.addProcessor(this);
		
		Gdx.input.setInputProcessor(im);
		*/
		//touches = new ArrayList<Vector2>();
		//touchSprites =  new ArrayList<Sprite>();
		
		//MyActor ma = new MyActor();
		//Gdx.input.setInputProcessor(stage);
		//ma.setTouchable(Touchable.enabled);
        //System.out.println(SpriteBatch.createDefaultShader().getFragmentShaderSource());
		/*shd = new ShaderProgram(Gdx.files.internal("vertex.glsl").readString(), Gdx.files.internal("fragment.glsl").readString());
		
        cam = new OrthographicCamera();
        cam.setToOrtho(false, 480, 320);
        viewport = new FitViewport(screenDims.x, screenDims.y, cam);
		stage = new Stage(viewport);
        cam.update();
        guiStage = new Stage();
		Texture t = new Texture("char.png");
		SimpleActor ma = new SimpleActor(){{addListener(new InputListener() {
			public boolean touchDown(InputEvent e, float x, float y, int touchIndex, int button) {
				SimpleActor ma = ((SimpleActor)e.getTarget());
				ma.flipX();
				return true;
			}});
			}};
		ma.setTexture(t);
		stage.addActor(ma);
		ma.updateBounds();
		addDisposable(stage, t);
		ma.addAction(new MoveToAction(){{setPosition(300f, 0f); setDuration(10f);}});
		
        tiledMap = new TmxMapLoader().load("map1.tmx");
        //gameLayer = (TiledMapTileLayer) tiledMap.getLayers().get("game_dynamic");
        bg_layers = new int[]{0, 1, 2};
        fg_layers = new int[]{4};
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //Gdx.input.setInputProcessor(this);
        
        guiStage = new Stage();
        addDisposable(guiStage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        TextButton tb = new TextButton("Text", skin);
        guiStage.addActor(tb);
        inpProcessors.setProcessors(new Array<InputProcessor>(){{addAll(GAME.instance, guiStage, stage);}});
        Gdx.input.setInputProcessor(inpProcessors);*/
		curLang = new Language(){{loadFile(Gdx.files.internal("en-us.lang"));}};
		initializer = new GameInitializer();
		
        Gdx.input.setInputProcessor(inpProcessors);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
		MenuScreen mainMenu, pauseMenu;
		//GameScreen gameMain;
		//final GameMapGenerator gmg = new GameMapGenerator();
		//gmg.initRand(new GameSession(), 2);
		//final StaticTiledMapTile tile = new StaticTiledMapTile(new TextureRegion(new Texture("ter.png"), 0, 8, 8, 8));
		//Texture terPng = new Texture("ter.png");
		GameObject.cellHeight = 16;
		GameObject.cellWidth = 16;
		//System.out.println(new WallGameObject().isOpaque());
		/*PuzzleGateGameObject.setDefaultTextureRegion(new TextureRegion(terPng, 24, 8, 8, 8));
		PuzzleGateGameObject.setDefaultOpenedTextureRegion(new TextureRegion(terPng, 16, 8, 8, 8));
		WallGameObject.setDefaultTextureRegion(new TextureRegion(terPng, 0, 8, 8, 8));
		ExitGameObject.setDefaultTextureRegion(new TextureRegion(terPng, 32, 0, 8, 8));*/
		PuzzleGateGameObject.setDefaultTextureRegion(atlas.findRegion("puzzleDoor"));
		PuzzleGateGameObject.setDefaultTextureRegion(atlas.findRegion("puzzleDoorOpened"), 1);
		FloorGameObject.setDefaultTextureRegion(atlas.findRegion("floor"));
		WallGameObject.setDefaultTextureRegion(atlas.findRegion("wall"));
		WallGameObject.setDefaultTextureRegion(atlas.findRegion("wallSide"), 1);
		ExitGameObject.setDefaultTextureRegion(atlas.findRegion("exit"));
		
		//System.out.println(new Vector2(0, 0).equals(new Vector2(0, 0)));
		//new HashSet<Vector2>(){{add(new Vector2(0, 0)); System.out.println(contains(new Vector2(0, 0)));}};
		PlayerGameObject.setDefaultTextureRegion(new TextureRegion(new Texture("char.png")));
		/*
		gameMain = new GameScreen(){
			//GameMap gmap;
			{
				//gmap = new GameMap(64, 64);
				//gmap.tiledMap = this.tiledMap = new TiledMap();//*/ new TmxMapLoader().load("map1.tmx");
				//gmap.stage = this.stage = new Stage();
				/*
				player = new PlayerGameObject(){{}};
				this.stage.addActor(player.actor);
				stage.setViewport(viewport);
				player.actor.setScaleX((float) .5);
				player.actor.setScaleY((float) .5);
				player.actor.updateBounds();
				addDisposable(player.actor.textureRegion.getTexture());
				this.guiStage = new Stage();
				instance.addDisposable(guiStage, stage, tiledMap);
				gmap.generator = gmg;
				bg_layers = new int[]{0};
				tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		        cam = new OrthographicCamera();
		        cam.setToOrtho(false, 512, 512);
		        //player.chunk = gmap.mainChunk;
				player.boundCam = cam;
				gmap.generate(player);
				//player.moveToCell(10, 25);
				player.physics = true;
		        viewport = new FitViewport(screenDims.x, screenDims.y, cam);
				//player.physics = true;
				//TiledMapTileLayer.Cell cell;
				TiledMapTileLayer layer = new TiledMapTileLayer(512, 512, 8, 8);
				//System.out.println(gmap.mainChunk.size());
				for(GameObject obj: this.gmap.mainChunk) {
					/*obj.grv = GraphicRepresentationVariant.TILEDMAP;
					cell = new TiledMapTileLayer.Cell(){{setTile(tile);}};
					obj.cells = new HashMap<Integer, TiledMapTileLayer.Cell>();
					obj.cells.put(0, cell);*/
					//System.out.println("Cell: " + obj.cellX + ", " + obj.cellY);
				/*
					if(obj instanceof AbstractTiledGameObject) {
						layer.setCell(obj.cellX, obj.cellY, ((AbstractTiledGameObject)obj).mainCell);
						//System.out.println(obj.opaque + "\t" + obj.getClass());
					}
					
				}
				gmap.tiledLayers.add(layer);
				inpProcArray.add(player);
				inpProcArray.add(GAME.instance);

				//player.prepareData();;
				//GameSaver.saveObject("save2.bin", GAME.instance.session);
				//for(Object L: gmap.tiledLayers) System.out.println(L);
			}
		};
		*/
		/*GameInitializer gi = new GameInitializer();
		gi.initLoaded("save4.bin");
		//gi.initNew(0);
		gameMain = gi.screen;
		//System.out.println(gameMain);
		screens.put("gameMain", gameMain);*/
		final TextField savetf = new TextField("currentsave.bin", skin);
		pauseMenu = new MenuScreen(){{
			TextButton save = new TextButton(curLang.get("pausemenu.save"), skin), tomain = new TextButton(curLang.get("pausemenu.tomain"), skin);
			save.addListener(new ClickListener(){
				public void clicked(InputEvent e, float x, float y) {
					((GameScreen)screens.get("gameMain")).player.prepareData();
					GameSaver.saveGameState(savetf.getText());
				}
			});
			tomain.addListener(new ClickListener(){
				public void clicked(InputEvent e, float x, float y) {
					AppScreen.openScreen(instance.screens.get("mainMenu"));
				}
			});
			putActors(savetf, save, tomain);
		}};
		screens.put("pause", pauseMenu);
		
		mainMenu = new MenuScreen(){
			//Vector2 playButPos, quitButPos;
			//Cell<TextButton> playButCell, quitButCell;
			TextButton newGameBut = new TextButton(curLang.get("mainmenu.newgame"), instance.skin){{setWidth(100); setVisible(false);}};
			TextField loadtf = new TextField("currentsave.bin", instance.skin){{setWidth(100); setVisible(false);}};
			TextButton loadGameBut = new TextButton(curLang.get("mainmenu.loadgame"), instance.skin){{setWidth(100); setVisible(false);}};
			TextButton quitBut = new TextButton(curLang.get("mainmenu.quit"), instance.skin){{setWidth(100); setVisible(false);}};
			//Table table = new Table(){{add(playBut).padBottom(30).width(100).padTop(30); row(); add(quitBut).width(100);}};
			public void stopAnim(Timer.Task t) {
				//playBut.addAction(sequence(moveBy(instance.screenDims.x, 0, 1f), visible(false), moveTo(playButPos.x, playButPos.y, .1f)));
				//quitBut.addAction(sequence(moveBy(-instance.screenDims.x, 0, 1f), visible(false), moveTo(quitButPos.x, quitButPos.y, .1f)));
				/*newGameBut.addAction(sequence(alpha(0, 0.8f), visible(false), alpha(1)));
				loadGameBut.addAction(sequence(alpha(0, 0.8f), visible(false), alpha(1)));
				quitBut.addAction(sequence(alpha(0, 0.8f), visible(false), alpha(1)));*/
				for(Actor a: UIactors) {
					a.addAction(sequence(alpha(0, 0.8f), visible(false), alpha(1)));
				}
				if(t != null) Timer.schedule(t, 1);
			}
			public void startAnim(Timer.Task t) {
				//playBut.moveBy(0.1f, 0);
				//playButPos = new Vector2(playBut.getX() + playButCell.getPadLeft(), playBut.getY() - playButCell.getPadTop());
				//playBut.setPosition(playButPos.x, playButPos.y);
				//System.out.println(playButPos);
				//playBut.addAction(sequence(moveBy(instance.screenDims.x, 0, 1.1f), visible(true), moveToAligned(playButPos.x, playButPos.y, playButCell.getAlign(), 1f)));
				//quitBut.addAction(sequence(moveBy(-instance.screenDims.x, 0, .5f), visible(true), moveBy(instance.screenDims.x, 0, .5f)));
				/*newGameBut.addAction(sequence(alpha(0), visible(true), alpha(1, 0.8f)));
				loadGameBut.addAction(sequence(alpha(0), visible(true), alpha(1, 0.8f)));
				quitBut.addAction(sequence(alpha(0), visible(true), alpha(1, 0.8f)));*/
				for(Actor a: UIactors) {
					a.addAction(sequence(alpha(0), visible(true), alpha(1, 0.8f)));
				}
				if(t != null) Timer.schedule(t, 1);
			}
			{
				/*table.add(newGameBut).width(100).padTop(30);
				table.row();
				table.add(loadGameBut).width(100).padTop(30);
				table.row();
				table.add(quitBut).width(100).padTop(30);*/
				putActors(newGameBut, loadtf, loadGameBut, quitBut);
				//guiStage = new Stage();
				instance.addDisposable(guiStage);
				//table.padTop(30);
				/*table.setWidth(guiStage.getWidth());
				table.align(Align.center | Align.top);
				table.setPosition(0, instance.screenDims.y);*/
				//table.act(.1f);
				//playBut.act(.1f);
				//System.out.println(playButPos);
				//guiStage.addActor(table);
				/*playBut.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent e, float x, float y) {
						AppScreen.openScreen(instance.screens.get("gameMain"));
						//System.out.println(((GameScreen)instance.screens.get("gameMain")).player);
					}
				});*/
				newGameBut.addListener(new ClickListener() {
					public void clicked(InputEvent e, float x, float y) {
						instance.initializer.initNew(0);
						GameScreen gameMain = GAME.instance.initializer.screen;
						instance.screens.put("gameMain", gameMain);
						AppScreen.openScreen(gameMain);
					}
				});
				loadGameBut.addListener(new ClickListener() {
					public void clicked(InputEvent e, float x, float y) {
						if(instance.initializer.initLoaded(loadtf.getText())) {
							GameScreen gameMain = GAME.instance.initializer.screen;
							instance.screens.put("gameMain", gameMain);
							AppScreen.openScreen(gameMain);
						}
					}
				});
				quitBut.addListener(new ClickListener(){
					@Override
					public void clicked(InputEvent e, float x, float y) {
						Gdx.app.exit();
					}
				});
				inpProcArray.add(guiStage);
			}
			
		};
		screens.put("mainMenu", mainMenu);
		AppScreen.openScreen(mainMenu);
		
		/*for(int i = 0; i < 64; i++) {
		gmg.initRand(new GameSession(), i);
		System.out.println("i=" + i);
			for(int j = 0; j < 64; j++) {
				System.out.println(gmg.randInt());
			}
		}*/
		/*{@SuppressWarnings("unused")
		public long seed = 0xABCDEF;});*/
		//System.out.println(tmpf.path());
		//tmpf.writeString("Qwerty", true);
		//<trying deserialize>
		//GameSession gs = (GameSession)GameSaver.loadObject("save3.bin");
		//System.out.println(gs.playerData.health);
		//</trying deserialize>
	}
	
	public void resize(int width, int height) {
		super.resize(width, height);
		if(currentScreen != null) currentScreen.resize();
	}

	@Override
	public void render() {
		/*int i;
		Vector2 v;
		Sprite s;
		cls();
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(img, (winWidth - img.getWidth()) / 2, (winHeight - img.getHeight()) / 2);
		spr_cloud.draw(batch);
		//batch.draw(img, (480 - img.getWidth()) / 2, (320 - img.getHeight()) / 2);
		while(touches.size() > touchSprites.size()) {
			s = new Sprite();
			s.set(spr_cloud);
			touchSprites.add(s);
		}
		for(i = 0; i < touches.size(); i++) {
			v = touches.get(i);
			if(v == null) continue;
			v = v.cpy();
			stdToGl(v);
			s = touchSprites.get(i);
			s.setX(v.x);
			s.setY(v.y);
			s.draw(batch);
		}
		batch.end();*/
		cls();
		
		//stage.getBatch().setShader(shd);
		//((BatchTiledMapRenderer)tiledMapRenderer).getBatch().setShader(shd);;
        //((FitViewport)stage.getViewport()).update(screenDims.x, screenDims.y, cam);
		if(cam != null && viewport != null) {
	        cam.update();
	        if(tiledMapRenderer != null) {
	        	tiledMapRenderer.setView(cam);
	        	if(bg_layers != null) tiledMapRenderer.render(bg_layers);
	        }
	        stage.setViewport(viewport);
	        if(stage != null) {
				stage.act(1.0f / 60.0f);
				stage.draw();
	        }
	        if(tiledMapRenderer != null && fg_layers != null) tiledMapRenderer.render(fg_layers);
		}
        if(guiStage != null) {
			guiStage.act(1.0f / 60.0f);
			guiStage.getViewport().update((int)screenDims.x, (int)screenDims.y);
        	guiStage.draw();
        }
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		//return false;
		switch(keycode) {
		/*case Keys.LEFT: cam.translate(-32, 0); break;
		case Keys.RIGHT: cam.translate(32, 0); break;
		case Keys.UP: cam.translate(0, 32); break;
		case Keys.DOWN: cam.translate(0, -32); break;
		case Keys.NUM_1: tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible()); break;
		case Keys.NUM_2: tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible()); break;
		case Keys.NUM_3: tiledMap.getLayers().get(2).setVisible(!tiledMap.getLayers().get(2).isVisible()); break;
		case Keys.NUM_4: tiledMap.getLayers().get(3).setVisible(!tiledMap.getLayers().get(3).isVisible()); break;
		case Keys.NUM_5: tiledMap.getLayers().get(4).setVisible(!tiledMap.getLayers().get(4).isVisible()); break;*/
		case Keys.EQUALS: case Keys.PLUS: cam.zoom /= 1.5; break;
		case Keys.MINUS: cam.zoom *= 1.5; break;
		default: return false;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int touchIndex, int button) {
		// TODO Auto-generated method stub
		return false;
		/*if(button != Buttons.LEFT) return false;
		while(touchIndex >= touches.size()) {
			touches.add(null);
		}
		//touches.ensureCapacity(touchIndex + 1);
		touches.set(touchIndex, new Vector2(screenX, screenY));
		return true;*/
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int touchIndex, int button) {
		// TODO Auto-generated method stub
		return false;
		/*if(button != Buttons.LEFT) return false;
		while(touchIndex >= touches.size()) {
			touches.add(null);
		}
		touches.set(touchIndex, null);
		return true;*/
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int touchIndex) {
		// TODO Auto-generated method stub
		return false;
		/*while(touchIndex >= touches.size()) {
			touches.add(null);
		}
		Vector2 v = touches.get(touchIndex);
		if(v == null) return false;
		v.x = screenX;
		v.y = screenY;
		return true;*/
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
	long lastZoomTimeStamp = (new Date()).getTime();
	@Override
	public boolean zoom(float initialDistance, float distance) {
		long ts = (new Date()).getTime();
		if(ts < lastZoomTimeStamp) lastZoomTimeStamp = ts;
		if(ts - lastZoomTimeStamp < 100) return false;
		lastZoomTimeStamp = ts;
		if(distance < initialDistance) return keyDown(Keys.MINUS);
		else return keyDown(Keys.PLUS);
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}
}
