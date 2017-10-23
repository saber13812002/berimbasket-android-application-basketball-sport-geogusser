package ir.berimbasket.app.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class TypefaceManager {

private static final Hashtable<String, Typeface> cache = new Hashtable<>();

        /**
         * @param c where your context is
         * @param name full path of your font (containing file name and extension)
         * @return typeface of your font
         */
        public static Typeface get(Context c, String name){
                synchronized(cache){ 
                        if(!cache.containsKey(name)){ 
                                Typeface t = Typeface.createFromAsset(c.getAssets(), name);
                                cache.put(name, t); 
                        } 
                        return cache.get(name); 
                } 
        }
}