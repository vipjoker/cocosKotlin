package com.vipjokerstudio.cocoskotlin.core.nodes;


import android.graphics.Bitmap;
import android.util.Log;

import com.vipjokerstudio.cocoskotlin.core.types.CGRect;
import com.vipjokerstudio.cocoskotlin.core.types.Size;

import org.jbox2d.common.Vec2;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/*
 * To create sprite frames and texture atlas, use this tool:
 * http://zwoptex.zwopple.com/
 */

/** Singleton that handles the loading of the sprite frames.
 It saves in a cache the sprite frames.
 @since v0.9
 */
public class CCSpriteFrameCache {
    HashMap<String, CCSpriteFrame> spriteFrames;

    private static CCSpriteFrameCache sharedSpriteFrameCache_= null;

    /** Retruns ths shared instance of the Sprite Frame cache */
    public static CCSpriteFrameCache sharedSpriteFrameCache() {
        if (sharedSpriteFrameCache_ == null) {
            sharedSpriteFrameCache_ = new CCSpriteFrameCache();
        }
        return sharedSpriteFrameCache_;
    }

    protected CCSpriteFrameCache() {
        spriteFrames = new HashMap<String, CCSpriteFrame>();
    }

    public static CCSpriteFrame spriteFrameByName(String name) {
        return CCSpriteFrameCache.sharedSpriteFrameCache().getSpriteFrame(name);
    }

    /** Purges the cache. It releases all the Sprite Frames and the retained instance.
    */
    public static void purgeSharedSpriteFrameCache() {
    	if (sharedSpriteFrameCache_ != null) {
    		sharedSpriteFrameCache_.removeAllSpriteFrames();
    		sharedSpriteFrameCache_ = null;
    	}
    }

    /** Adds multiple Sprite Frames with a dictionary.
     * The texture will be associated with the created sprite frames.
     */
    public Set<String> addSpriteFrames(HashMap<String, Object> dictionary, Bitmap texture) {
    	/*
   	 Supported Zwoptex Formats:
   	 ZWTCoordinatesFormatOptionXMLLegacy = 0, // Flash Version
   	 ZWTCoordinatesFormatOptionXML1_0 = 1, // Desktop Version 0.0 - 0.4b
   	 ZWTCoordinatesFormatOptionXML1_1 = 2, // Desktop Version 1.0.0 - 1.0.1
   	 ZWTCoordinatesFormatOptionXML1_2 = 3, // Desktop Version 1.0.2+
   	*/
    	
		@SuppressWarnings("unchecked")
		HashMap<String, Object> metadataDict = (HashMap<String, Object>)dictionary.get("metadata");
		@SuppressWarnings("unchecked")
		HashMap<String, Object> framesDict = (HashMap<String, Object>)dictionary.get("frames");
        
    	int format = 0;

    	// get the format
    	if(metadataDict != null)
    		format = (Integer)metadataDict.get("format");

    	// check the format
      if (!(format >= 0 && format <= 3)) {
	      Log.e("CCSpriteFrameCache",
	          "Unsupported Zwoptex plist file format.");
      }

    	// add real frames
    	for(Entry<String, Object> frameDictEntry : framesDict.entrySet()) {
    		@SuppressWarnings("unchecked")
			HashMap<String, Object> frameDict = (HashMap<String, Object>)frameDictEntry.getValue();
    		CCSpriteFrame spriteFrame = null;
    		if(format == 0) {
    			float x = ((Number)frameDict.get("x")).floatValue();
    			float y = ((Number)frameDict.get("y")).floatValue();
    			float w = ((Number)frameDict.get("width")).floatValue();
    			float h = ((Number)frameDict.get("height")).floatValue();
    			float ox = ((Number)frameDict.get("offsetX")).floatValue();
    			float oy = ((Number)frameDict.get("offsetY")).floatValue();
    			
    			int ow = 0;
    			int oh = 0;
    			// check ow/oh
    			try {
	    			ow = ((Number)frameDict.get("originalWidth")).intValue();
	    			oh = ((Number)frameDict.get("originalHeight")).intValue();
    			} catch (Exception e) {
    				Log.i("cocos2d", "WARNING: originalWidth/Height not found on the CCSpriteFrame. AnchorPoint won't work as expected. Regenerate the .plist");
				}
    			
    			// abs ow/oh
    			ow = Math.abs(ow);
    			oh = Math.abs(oh);
    			// create frame

    			spriteFrame = CCSpriteFrame.frame(texture, CGRect.make(x, y, w, h), false, new Vec2(ox, oy), Size.make(ow, oh));

    		}

//    		else if(format == 1 || format == 2) {
//    			CGRect frame = GeometryUtil.CGRectFromString( (String)frameDict.get("frame") );
//    			boolean rotated = false;
//
//    			// rotation
//    			if(format == 2)
//    				rotated = (Boolean)frameDict.get("rotated");
//
//    			Vec2 offset = GeometryUtil.CGPointFromString( (String)frameDict.get("offset") );
//    			Size sourceSize = GeometryUtil.CGSizeFromString( (String)frameDict.get("sourceSize") );
//
//    			// create frame
//    			spriteFrame = CCSpriteFrame.frame(texture, frame, rotated, offset, sourceSize);
//    		} else if(format == 3) {
//    			// get values
//    			Size spriteSize = GeometryUtil.CGSizeFromString( (String)frameDict.get("spriteSize") );
//    			Vec2 spriteOffset = GeometryUtil.CGPointFromString( (String)frameDict.get("spriteOffset") );
//    			Size spriteSourceSize = GeometryUtil.CGSizeFromString( (String)frameDict.get("spriteSourceSize") );
//    			CGRect textureRect = GeometryUtil.CGRectFromString( (String)frameDict.get("textureRect") );
//    			boolean textureRotated = (Boolean)frameDict.get("textureRotated");
//
//// Aliases are not supported in this version while.
//
////    			// get aliases
////    			ArrayList<Object> aliases = frameDict.get("aliases");
////    			for(NSString *alias in aliases) {
////    				if( [spriteFramesAliases_ objectForKey:alias] )
////    					CCLOG(@"cocos2d: WARNING: an alias with name %@ already exists",alias);
////
////    				[spriteFramesAliases_ setObject:frameDictKey forKey:alias];
////    			}
//
//    			// create frame
//    			spriteFrame = CCSpriteFrame.frame(texture,
//							CGRect.make(textureRect.origin.x, textureRect.origin.y, spriteSize.width, spriteSize.height),
//							textureRotated, spriteOffset, spriteSourceSize);
//    		}

    		// add sprite frame
    		spriteFrames.put(frameDictEntry.getKey(), spriteFrame);
    	}
    	return framesDict.keySet();
    }

    /** Adds multiple Sprite Frames from a plist file.
     * A texture will be loaded automatically.
     * The texture name will composed by replacing the .plist suffix with .png
     * If you want to use another texture, you should use the addSpriteFramesWithFile:texture method.
     */
    public Set<String> addSpriteFrames(String plist) {
        String texturePath = null;
        int i = plist.lastIndexOf('.');
        if (i > 0 && i <= plist.length() - 2)
		      texturePath = plist.substring(0, i) + ".png";
        Bitmap texture =
            CCTextureCache.getInstance().addImage(texturePath);
        return addSpriteFrames(plist, texture);
    }

    /** Adds multiple Sprite Frames from a plist file.
     * The texture will be associated with the created sprite frames.
     */
    public Set<String> addSpriteFrames(String plist, Bitmap texture) {
//		  try {
//			HashMap<String, Object> dict = PlistParser.parse(plist);
//            return addSpriteFrames(dict, texture);
//        } catch (Exception e) {
//                Macros.CCLOG("CCSpriteFrameCache",
//					     "Unable to read Zwoptex plist: " + e);
//		  }
		return null;
    }

    /** Adds an sprite frame with a given name.
      If the name already exists, then the contents of the old name will be replaced with the new one.
      */
    public void addSpriteFrame(CCSpriteFrame frame, String frameName) {
	    spriteFrames.put(frameName, frame);
    }

    /** Purges the dictionary of loaded sprite frames.
     * Call this method if you receive the "Memory Warning".
     * In the short term: it will free some resources preventing your app from being killed.
     * In the medium term: it will allocate more resources.
     * In the long term: it will be the same.
     */
    public void removeSpriteFrames() {
        spriteFrames.clear();
    }

    /** Removes unused sprite frames.
     * Sprite Frames that have a retain count of 1 will be deleted.
     * It is convinient to call this method after when starting a new Scene.
     */
    public void removeAllSpriteFrames() {
        // Don't know what to do here.
    	spriteFrames.clear();
    }

    /** Deletes an sprite frame from the sprite frame cache.
    */
    public void removeSpriteFrame(String name) {
	    spriteFrames.remove(name);
    }

    /** Returns an Sprite Frame that was previously added.
      If the name is not found it will return nil.
      You should retain the returned copy if you are going to use it.
      */
    public CCSpriteFrame getSpriteFrame(String name) {
        CCSpriteFrame frame = spriteFrames.get(name);

        if( frame == null )
            Log.v("CCSpriteFrameCache", "Frame not found: " + name);

        return frame;
    }

    /** Creates an sprite with the name of an sprite frame.
      The created sprite will contain the texture, rect and offset of the sprite frame.
      It returns an autorelease object.
      @deprecated use [CCSprite spriteWithSpriteFrameName:name]. This method will be removed on final v0.9
      */
    public CCSprite createSprite(String name) {
        CCSpriteFrame frame = spriteFrames.get(name);
        return CCSprite.Companion.sprite(frame);
    }
}

