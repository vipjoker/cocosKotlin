package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vipjokerstudio.cocoskotlin.util.BitmapUtil;
import com.vipjokerstudio.cocoskotlin.util.Converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Singleton that handles the loading of textures
 * Once the texture is loaded, the next time it will return
 * a reference of the previously loaded texture reducing GPU & CPU memory
 */
public class CCTextureCache {
    private Map<String, Bitmap> textures;

    private static CCTextureCache INSTANCE;

    /**
     * Retruns ths shared instance of the cache
     */
    public static CCTextureCache getInstance() {
        synchronized (CCTextureCache.class) {
            if (INSTANCE == null) {
                INSTANCE = new CCTextureCache();
            }
            return INSTANCE;
        }
    }

    /**
     * purges the cache. It releases the retained instance.
     *
     * @since v0.99.0
     */
    public static void purgeSharedTextureCache() {
        if (INSTANCE != null) {
            INSTANCE.removeAllTextures();
        }
    }

    private CCTextureCache() {

        synchronized (CCTextureCache.class) {
            textures = new HashMap<String, Bitmap>(10);
        }
    }

    /**
     * Returns a Texture2D object given an file image
     * If the file image was not previously loaded, it will create a new CCTexture2D
     * object and it will return it. It will use the filename as a key.
     * Otherwise it will return a reference of a previosly loaded image.
     * Supported image extensions: .png, .bmp, .tiff, .jpeg, .pvr, .gif
     */
    public Bitmap addImage(String path) {
        assert path != null : "TextureMgr: path must not be null";

        Bitmap tex = textures.get(path);


        if (tex == null) {
            tex = BitmapUtil.INSTANCE.createTextureFromFilePath(path);
            textures.put(path, tex);
        }
        return tex;
    }

    public Bitmap getImage(String key){
        return textures.get(key);
    }

    /**
     * Returns a Texture2D object given an file image from external path.
     */


    /**
     * Returns a Texture2D object given an CGImageRef image
     * If the image was not previously loaded, it will create a new CCTexture2D object and it will return it.
     * Otherwise it will return a reference of a previously loaded image
     * The "key" parameter will be used as the "key" for the cache.
     * If "key" is nil, then a new texture will be created each time.
     * <p>
     * BE AWARE OF the fact that copy of image is stored in memory,
     * use assets method if you can.
     *
     * @since v0.8
     */
    public Bitmap addImage(Bitmap image, String key) {
        assert (image != null) : "TextureCache: image must not be null";

        Bitmap tex = textures.get(key);

        if (key != null && tex != null) {
            return tex;
        }

        final Bitmap copy = image.copy(image.getConfig(), false);

        if (copy != null) {

            if (key != null) {
                textures.put(key, image);
            }

            return image;
        } else {
            Converter.CCLOG("cocos2d", "Couldn't add Bitmap in CCTextureCache");
            return null;
        }
    }

    public Bitmap addVectorImage(Context context,int imageRes, String key){
        Bitmap bitmap = BitmapUtil.INSTANCE.getBitmap(context,imageRes);

        textures.put(key,bitmap);
        return bitmap;

    }
    /**
     * Purges the dictionary of loaded textures.
     * Call this method if you receive the "Memory Warning"
     * In the short term: it will free some resources preventing your app from being killed
     * In the medium term: it will allocate more resources
     * In the long term: it will be the same
     */
    public void removeAllTextures() {
        /* Do nothing, or do all.*/
        for (Bitmap texSR : textures.values()) {
    		if(!texSR.isRecycled()) {
                texSR.recycle();
            }
        }
        textures.clear();
    }

    /**
     * Deletes a texture from the cache given a texture
     */
    public void removeTexture(Bitmap tex) {
        if (tex == null)
            return;

        textures.values().remove(tex);
    }

    /*
     * Add a texture to the cache so it gets managed
     */
    public void addTexture(Bitmap tex) {
        if (tex == null)
            return;
        textures.put(String.valueOf(tex.hashCode()), tex);
    }

    /**
     * Deletes a texture from the cache given a its key name
     *
     * @since v0.99.4
     */
    public void removeTexture(String textureKeyName) {
        if (textureKeyName == null)
            return;
        textures.remove(textureKeyName);
    }



}


