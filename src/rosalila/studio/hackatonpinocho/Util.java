package rosalila.studio.hackatonpinocho;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

public class Util {
	public static TiledTextureRegion createTiledTextureRegionFromAsset(
			TextureManager pTextureManager, final Context pContext,
			final int onePictWidth, final int onePichHeight,
			final String... paths){
		BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(
				pTextureManager, onePictWidth * paths.length, onePichHeight,
				TextureOptions.BILINEAR);

		ITextureRegion[] iTextureRegions = new ITextureRegion[paths.length];

		for (int i = 0; i < paths.length; i++)
		{
			iTextureRegions[i] = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(mBitmapTextureAtlas, pContext, paths[i],
							onePictWidth * i, 0);
		}

		TiledTextureRegion result = new TiledTextureRegion(mBitmapTextureAtlas,iTextureRegions);
		mBitmapTextureAtlas.load();
		return result;
	}
}
