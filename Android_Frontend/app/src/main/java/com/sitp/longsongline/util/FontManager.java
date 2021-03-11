package com.sitp.longsongline.util;

import android.content.Context;
import android.graphics.Typeface;

public class FontManager {
    public static final String root = "fonts/";
    public static final String FONTAWESOME = root + "fontawesome-webfont.ttf";


    public static Typeface getTypeFace(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
