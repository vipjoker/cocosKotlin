package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.graphics.Bitmap;

public interface CCTextureProtocol  {
    /** returns the used texture */
    public Bitmap getTexture();

    /** sets a new texture. it will be retained */
    public void setTexture(Bitmap texture);

}