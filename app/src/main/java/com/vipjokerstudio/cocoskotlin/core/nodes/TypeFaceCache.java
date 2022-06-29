package com.vipjokerstudio.cocoskotlin.core.nodes;

import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class TypeFaceCache {

    private final Map<String, Typeface> cache = new HashMap<>();
    private static TypeFaceCache INSTANCE = new TypeFaceCache();
    public static TypeFaceCache getInstance(){
        return INSTANCE;
    }

    public Typeface getFont(String name){
        Typeface typeface = cache.get(name);
        if(typeface == null){
             typeface = Typeface.createFromAsset(Director.getInstance().getActivity().getAssets(),name);
             cache.put(name,typeface);
        }
        return typeface;
    }

}
