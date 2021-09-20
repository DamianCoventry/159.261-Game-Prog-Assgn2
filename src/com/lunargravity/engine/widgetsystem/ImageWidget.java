package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.graphics.GlTexture;

public class ImageWidget extends WidgetObserver {
    private GlTexture _texture;
    public ImageWidget(WidgetManager widgetManager) {
        super(widgetManager);
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) {
        String imageFileName = wci._properties.get("imageFileName");
        if (imageFileName != null) {
            _texture = new GlTexture(imageFileName);
        }
        // else throw
    }

    // TODO: how is the painting done?
}
