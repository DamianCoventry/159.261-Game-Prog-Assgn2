package com.lunargravity.engine.widgetsystem;

import com.lunargravity.engine.graphics.BitmapImage;
import com.lunargravity.engine.graphics.GlDiffuseTextureProgram;
import com.lunargravity.engine.graphics.GlTexture;
import com.lunargravity.engine.graphics.PolyhedraVxTc;
import org.joml.Matrix4f;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class ImageWidget extends WidgetObserver {
    public static final String IMAGE_FILE_NAME = "imageFileName";

    private final GlDiffuseTextureProgram _program;
    private GlTexture _texture;
    private PolyhedraVxTc _polyhedra;
    private Matrix4f _modelMatrix;

    public ImageWidget(WidgetManager widgetManager) {
        super(widgetManager);
        _program = _widgetManager.getRenderer().getDiffuseTextureProgram();
        _modelMatrix = new Matrix4f();
    }

    public void setImage(String imageFileName) throws IOException {
        _texture = new GlTexture(BitmapImage.fromFile(imageFileName));
    }

    @Override
    protected void createChildWidgets(WidgetCreateInfo wci) throws IOException {
        String imageFileName = wci._properties.get(IMAGE_FILE_NAME);
        if (imageFileName == null) {
            throw new RuntimeException("Widget create info does not contain an entry for [" + IMAGE_FILE_NAME + "]");
        }
        _texture = new GlTexture(BitmapImage.fromFile(imageFileName));
        _polyhedra = createPolyhedraVxTc(0.0f, 0.0f, wci._size.x, wci._size.y);
    }

    @Override
    public void freeResources() {
        _polyhedra.freeResources();
        _texture.freeResources();
    }

    @Override
    public void widgetDraw2d(int viewport, Matrix4f projectionMatrix) {
        _widgetManager.getRenderer().activateTextureImageUnit(0);
        glBindTexture(GL_TEXTURE_2D, _texture.getId());

        _modelMatrix = _modelMatrix.identity().translate(_widget.getPosition().x, _widget.getPosition().y, 0.0f);
        // there is no view matrix when using an orthographic projection matrix
        Matrix4f mvpMatrix = projectionMatrix.mul(_modelMatrix);

        _program.activate(mvpMatrix);
        _polyhedra.draw();
    }
}
