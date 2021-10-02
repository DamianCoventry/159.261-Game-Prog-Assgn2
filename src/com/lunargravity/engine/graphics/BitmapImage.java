//
// Lunar Gravity
//
// This game is based upon the Amiga video game Gravity Force that was
// released in 1989 by Stephan Wenzler
//
// https://www.mobygames.com/game/gravity-force
// https://www.youtube.com/watch?v=m9mFtCvnko8
//
// This implementation is Copyright (c) 2021, Damian Coventry
// All rights reserved
// Written for Massey University course 159.261 Game Programming (Assignment 2)
//

package com.lunargravity.engine.graphics;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapImage {
    public static BitmapImage fromFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new RuntimeException("File system item [" + fileName + "] does not exist");
        }
        if (!file.isFile()) {
            throw new RuntimeException("The file system item [" + fileName + "] exists but is not a file");
        }
        return new BitmapImage(fileName, ImageIO.read(file));
    }

    private final String _fileName;
    private final int _width;
    private final int _height;
    private final ByteBuffer _byteBuffer;

    private BitmapImage(String fileName, BufferedImage bufferedImage) {
        _fileName = fileName;
        _width = bufferedImage.getWidth();
        _height = bufferedImage.getHeight();
        _byteBuffer = toByteBuffer(bufferedImage);
    }

    public String getFileName() {
        return _fileName;
    }
    public int getWidth() {
        return _width;
    }
    public int getHeight() {
        return _height;
    }
    public ByteBuffer getByteBuffer() {
        return _byteBuffer;
    }

    private ByteBuffer toByteBuffer(BufferedImage bufferedImage) {
        int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);
        for(int y = 0; y < bufferedImage.getHeight(); y++){
            for(int x = 0; x < bufferedImage.getWidth(); x++){
                int pixel = pixels[y * bufferedImage.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));             // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));     // Alpha component. Only for RGBA
            }
        }

        // After a sequence of channel-read or put operations, invoke this method to prepare for a
        // sequence of channel-write or relative get operations.
        buffer.flip();

        return buffer;
    }
}
