package com.lunargravity.application;

import com.lunargravity.engine.graphics.GlRenderer;
import com.lunargravity.engine.graphics.NumberFont;

import java.io.IOException;

public class LargeNumberFont extends NumberFont {
    public static final float FRAME_WIDTH = 26.0f;
    public static final float FRAME_HEIGHT = 37.0f;

    public LargeNumberFont(GlRenderer renderer) throws IOException {
        super(renderer, FRAME_WIDTH, FRAME_HEIGHT, "images/LargeNumbers.png");
    }
}
