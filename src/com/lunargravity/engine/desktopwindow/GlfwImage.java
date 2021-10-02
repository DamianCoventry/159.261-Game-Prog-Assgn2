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

package com.lunargravity.engine.desktopwindow;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public class GlfwImage {
    static public GLFWImage fromFile(String fileName) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(fileName));
        if (bufferedImage == null) {
            throw new RuntimeException("Unable to load the file [" + fileName + "]");
        }

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

        GLFWImage image = GLFWImage.malloc();
        image.set(bufferedImage.getWidth(), bufferedImage.getHeight(), buffer);
        return image;
    }
}
