package rosalila.studio.hackatonpinocho;

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
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import tv.ouya.console.api.OuyaController;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.iozm.rwei160144.AdCallbackListener;
import com.iozm.rwei160144.AdView;
import com.iozm.rwei160144.AirPlay;

public class GameActivity extends BaseGameActivity {

	private static final String TAG = GameActivity.class.getSimpleName();

	private Camera mCamera;
	
	private ITextureRegion mSplashBackground;
	private ITextureRegion mMainMenuBackground;
	
	private Scene mSplashScene;
	private Scene mMainMenuScene;
	
	private AirPlay airPlay;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, GameConstants.CAMERA_WIDTH, GameConstants.CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), mCamera);
		
		return options;
	}
	
	AdCallbackListener.MraidCallbackListener adlistener = new AdCallbackListener.MraidCallbackListener() {

	     @Override
	     public void onAdClickListener()
	     {
	     //This will get called when ad is clicked.
	    	 Log.d("test", "onAdClickListener");
	     }

	     @Override
	     public void onAdLoadedListener()
	     {
	     //This will get called when an ad has loaded.
	    	 Log.d("test", "onAdLoadedListener");
	     }

	     @Override
	     public void onAdLoadingListener()
	     {
	     //This will get called when a rich media ad is loading.
	    	 Log.d("test", "onAdLoadingListener");
	     }

	     @Override
	     public void onAdExpandedListner()
	     {
	     //This will get called when an ad is showing on a user's screen. This may cover the whole UI.
	    	 Log.d("test", "onAdExpandedListner");
	     }

	     @Override
	     public void onCloseListener()
	     {
	     //This will get called when an ad is closing/resizing from an expanded state.
	    	 Log.d("test", "onCloseListener");
	     }

	     @Override
	     public void onErrorListener(String message)
	     {
	     //This will get called when any error has occurred. This will also get called if the SDK notices any integration mistakes.
	    	 Log.d("test", "onErrorListener");
	     }
	     @Override
	      public void noAdAvailableListener() {
		//this will get called when ad is not available 
	    	 Log.d("test", "noAdAvailableListener");
			
		}
	};

	@Override
	protected void onSetContentView() {
		this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
        
        final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
                new FrameLayout.LayoutParams(super.createSurfaceViewLayoutParams());
        
		final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                                             FrameLayout.LayoutParams.FILL_PARENT);
        
        
        final AdView airpushBanner =  new AdView(this, AdView.BANNER_TYPE_IN_APP_AD, AdView.PLACEMENT_TYPE_INTERSTITIAL, false, false, 
       	     AdView.ANIMATION_TYPE_LEFT_TO_RIGHT);
        airpushBanner.setAdListener(adlistener);
	   
        
        final FrameLayout.LayoutParams adViewLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                             FrameLayout.LayoutParams.WRAP_CONTENT,
                                             Gravity.CENTER_HORIZONTAL|Gravity.TOP);
        
 
        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(airpushBanner, adViewLayoutParams);
        
        this.setContentView(frameLayout, frameLayoutLayoutParams);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		
//		setContentView(R.layout.main);
//		
//		if(airPlay==null)
//			airPlay=new AirPlay(this, null, false);
		
		//setContentView(R.layout.ad);
//	     AdView adView=(AdView)findViewById(R.id.myAdView);
//		adView.setAdListener(adlistener);
		
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
						Intent intent = new Intent(GameActivity.this, FightActivity.class);
						startActivity(intent);      
//						finish();
				}
				return false;
			}
		});
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
