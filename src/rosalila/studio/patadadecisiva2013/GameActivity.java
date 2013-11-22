package rosalila.studio.patadadecisiva2013;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import tv.ouya.console.api.OuyaController;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.startapp.android.publish.StartAppAd;

public class GameActivity extends BaseGameActivity {

	private static final String TAG = GameActivity.class.getSimpleName();

	private Camera mCamera;
	
	private ITextureRegion mSplashBackground;
	private ITextureRegion mMainMenuBackground;
	
	private Scene mSplashScene;
	private Scene mMainMenuScene;
	
	private StartAppAd startAppAd = new StartAppAd(this);
	
	@Override
	public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), mCamera);
		
		return options;
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BuildableBitmapTextureAtlas splashTextureAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT);
		mSplashBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "rosalila_logo.png");
		
		try {
			splashTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		} catch (TextureAtlasBuilderException exception) {
			Log.e(TAG, exception.getMessage());
		}		
		splashTextureAtlas.load();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		mSplashScene = new Scene();
		
		pOnCreateSceneCallback.onCreateSceneFinished(mSplashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		pScene.setBackgroundEnabled(false);
		
		Sprite background = new Sprite(0, 0, mSplashBackground, getVertexBufferObjectManager());
		pScene.attachChild(background);
		
		pScene.registerUpdateHandler(new TimerHandler(GameConstants.SPLASH_DURATION, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mSplashScene.unregisterUpdateHandler(pTimerHandler);
				
				createMenuResources();
				createMenuScene();
				
				mEngine.setScene(mMainMenuScene);
				
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	/* 
	 * Loads in memory the resources used in the main menu scene
	 * */
	private void createMenuResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BuildableBitmapTextureAtlas mainMenuAtlas = new BuildableBitmapTextureAtlas(getTextureManager(), GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT);
		mMainMenuBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuAtlas, this, "main_menu_bg.png");
		
		try {
		    mainMenuAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
		} catch (TextureAtlasBuilderException exception) {
			Log.e(TAG, exception.getMessage());
		}
		mainMenuAtlas.load();		
	}
	

	@Override
	protected void onSetContentView() {
		super.onSetContentView();
		StartAppAd.init(this, "111281878", "211380146");
	}
	
	/* 
	 * Creates and populates the Scene used as main menu
	 * */
	private void createMenuScene() {
		mMainMenuScene = new Scene();
		mMainMenuScene.setBackgroundEnabled(false);
		
		Sprite background = new Sprite(0, 0, GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT, mMainMenuBackground, getVertexBufferObjectManager());
		mMainMenuScene.attachChild(background);
		
		mMainMenuScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
			{
				if(pSceneTouchEvent.isActionDown())
				{			
					
					
					GameActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                        	startAppAd.showAd(); // show the ad
        					startAppAd.loadAd(); // load the next ad
                        }
                    });
					
					
					Intent intent = new Intent(GameActivity.this, FightActivity.class);
					startActivity(intent);
//						finish();
				}
				return false;
			}
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		startAppAd.onResume();
	}
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if(OuyaController.BUTTON_L1==event.getKeyCode()
    	   ||OuyaController.BUTTON_R1==event.getKeyCode())
    	{
			Intent intent = new Intent(GameActivity.this, FightActivity.class);
			startActivity(intent);
			return true;
    	}
        return super.onKeyDown(keyCode, event);
    }
}
