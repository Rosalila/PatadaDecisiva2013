package rosalila.studio.hackatonpinocho;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public  class Player extends AnimatedSprite {

	private static final String TAG = Player.class.getSimpleName();
	public Body mBody;
	private PhysicsWorld mWorld;
	private FixtureDef mFixture = PhysicsFactory.createFixtureDef(0.5f, 0.5f, 0.5f);
	private FacingSide mSide;
	private boolean mIsOnGround;
	int number;
	boolean was_moving;
	
	private Vector2 mDesiredSpeed = new Vector2(0.0f, 45.0f);
	
	private boolean mPendingJump;
	private boolean mPendingDive;
	
	Player opponent;
	
	public static enum FacingSide {
	    Right, Left	
	}
	
	public Player(final float pX, final float pY, final TiledTextureRegion pTextureRegion, 
			final VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld world, int number) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		mWorld = world;
		
		//Hitbox 
		Rectangle hitbox = new Rectangle(pX, pY, 150, 200, getVertexBufferObjectManager());
		mBody = PhysicsFactory.createBoxBody(mWorld, hitbox, BodyType.DynamicBody, mFixture);
		mBody.setFixedRotation(true);
		mBody.setUserData(this);
		mWorld.registerPhysicsConnector(new PhysicsConnector(this, mBody));
		mPendingJump = false;
		mSide = number == 1? FacingSide.Right : FacingSide.Left;
		this.number=number;
	}
	
	public void jump() {
		mPendingJump = true;
		setCurrentTileIndex(1);
	}
	
	public void dive() {
		mPendingDive = true;
		setCurrentTileIndex(2);
	}
	
	public void onManagedUpdate(float elapsedSeconds) {
		super.onManagedUpdate(elapsedSeconds);
		
		if(this.mBody.getPosition().x<opponent.mBody.getPosition().x)
			this.setFlipped(false, false);
		else
			this.setFlipped(true, false);
	
		
		if (mPendingJump)
		{
			mDesiredSpeed.x=(float)((Math.random()*1000.0f)%60)-30.0f;
			changeSpeed(mDesiredSpeed);
			mPendingJump = false;
		}
			
		if (mPendingDive) 
		{
			Vector2 desiredVel = Vector2Pool.obtain();
			if(this.mBody.getPosition().x<opponent.mBody.getPosition().x)
			{
				desiredVel.x = 20.0f;
				desiredVel.y = 30.0f;
			}else
			{
				desiredVel.x = -20.0f;
				desiredVel.y = 30.0f;
			}
			changeSpeed(desiredVel);			
			Vector2Pool.recycle(desiredVel);
			mPendingDive = false;
		}
		
//		if(was_moving && !isMoving())
//			this.mBody.setLinearVelocity(new Vector2(0,0));
//		was_moving=isMoving();
		
//		if(this.getY() < GameConstants.CAMERA_HEIGHT - 300)
//			this.mBody.setTransform(this.mBody.getTransform().getPosition().x,
//					(GameConstants.CAMERA_HEIGHT - 300)/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
//					0.0f);
		
//		if (mPendingDive) {
//		Vector2 desiredVel = Vector2Pool.obtain();
//		switch (mSide) {
//		case Right:
//			desiredVel.x = 30.0f;
//			desiredVel.y = 30.0f;
//			break;
//			
//		case Left:
//			desiredVel.x = -30.0f;
//			desiredVel.y = 30.0f;
//			break;
//		}
//		
//		changeSpeed(desiredVel);			
//		Vector2Pool.recycle(desiredVel);
//		mPendingDive = false;
//	}
	}
	
	public boolean isMoving() {
		return this.getY() <= GameConstants.CAMERA_HEIGHT - 500;
	}
	
	private void changeSpeed(Vector2 desiredSpeed) {
		Vector2 velocity = mBody.getLinearVelocity();
		
		float velocityChangeY = desiredSpeed.y - velocity.y;
		float velocityChangeX = desiredSpeed.x - velocity.x;
		
		float impulseY = mBody.getMass() * velocityChangeY;
		float impulseX = mBody.getMass() * velocityChangeX;
		mBody.applyLinearImpulse(new Vector2(impulseX, impulseY), mBody.getWorldCenter());
	}	
}