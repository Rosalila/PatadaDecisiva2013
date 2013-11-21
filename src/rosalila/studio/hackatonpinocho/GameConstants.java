package rosalila.studio.hackatonpinocho;

import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameConstants {

	public static final int CAMERA_HEIGHT = 720;
	public static final int CAMERA_WIDTH = 1280;
	
	public static final float SPLASH_DURATION = 5.0f;
	
	public static final float DEMO_VELOCITY = 100.0f;
	public static final FixtureDef GROUND_FIXTURE = PhysicsFactory.createFixtureDef(0.5f, 0.0f, 0.5f);
}
