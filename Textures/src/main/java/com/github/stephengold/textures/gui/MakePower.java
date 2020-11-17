/*
 Copyright (c) 2020, Stephen Gold
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
 notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 notice, this list of conditions and the following disclaimer in the
 documentation and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its contributors
 may be used to endorse or promote products derived from this software without
 specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.stephengold.textures.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;

/**
 * A console application to generate the textures "power-off.png" and
 * "power-on.png" for a power button.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakePower {
    // *************************************************************************
    // constants and loggers

    /**
     * size of the texture map (pixels per side)
     */
    final private static int textureSize = 256;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakePower.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakePower application.
     *
     * @param arguments array of command-line arguments (not null)
     */
    public static void main(String[] arguments) {
        /*
         * Mute the chatty loggers found in some imported packages.
         */
        Heart.setLoggingLevels(Level.WARNING);
        /*
         * Set the logging level for this class and also for writeImage().
         */
        //logger.setLevel(Level.INFO);
        //Logger.getLogger(Heart.class.getName()).setLevel(Level.INFO);
        /*
         * Instantiate the application.
         */
        MakePower application = new MakePower();
        /*
         * Log the working directory.
         */
        String userDir = System.getProperty("user.dir");
        logger.log(Level.INFO, "working directory is {0}",
                MyString.quote(userDir));
        /*
         * Define colors.
         */
        float opacity = 1f;
        Color white = new Color(1f, 1f, 1f, opacity);
        Color red = new Color(0.3f, 0f, 0f, opacity);
        Color green = new Color(0f, 0.7f, 0f, opacity);
        /*
         * Generate color image maps.
         */
        application.makePower(white, red, "power-off.png");
        application.makePower(white, green, "power-on.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a power button.
     */
    private void makePower(Color fgColor, Color bgColor, String assetPath) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureSize, textureSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();

        // Start with all pixels fgColor, to provide a border.
        graphics.setColor(fgColor);
        graphics.fillRect(0, 0, textureSize, textureSize);

        // circular background for the button itself
        double r = 0.44;
        int xy = (int) Math.round(textureSize * (0.5 - r));
        int diameter = (int) Math.round(textureSize * 2.0 * r);
        graphics.setColor(bgColor);
        graphics.fillOval(xy, xy, diameter, diameter);

        // outer edge of the "0"
        r = 0.25;
        xy = (int) Math.round(textureSize * (0.5 - r));
        diameter = (int) Math.round(textureSize * 2.0 * r);
        graphics.setColor(fgColor);
        graphics.fillOval(xy, xy, diameter, diameter);

        // the inner portion of the "0"
        double lineWidth = 0.04;
        r -= lineWidth;
        xy = (int) Math.round(textureSize * (0.5 - r));
        diameter = (int) Math.round(textureSize * 2.0 * r);
        graphics.setColor(bgColor);
        graphics.fillOval(xy, xy, diameter, diameter);

        // the gap at the top of the "0"
        double gapHalfWidth = 0.08;
        int x1 = (int) Math.round(textureSize * (0.5 - gapHalfWidth));
        int x2 = (int) Math.round(textureSize * (0.5 + gapHalfWidth));
        int y1 = (int) Math.round(textureSize * 0.1);
        int y2 = (int) Math.round(textureSize * 0.5);
        graphics.setColor(bgColor);
        graphics.fillRect(x1, y1, x2 - x1, y2 - y1);

        // the "1"
        double halfWidth = lineWidth / 2.0;
        x1 = (int) Math.round(textureSize * (0.5 - halfWidth));
        x2 = (int) Math.round(textureSize * (0.5 + halfWidth));
        y1 = (int) Math.round(textureSize * 0.13);
        y2 = (int) Math.round(textureSize * 0.47);
        graphics.setColor(fgColor);
        graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
        /*
         * Write the image to the asset file.
         */
        String filePath = String.format("%s/%s", assetDirPath, assetPath);
        try {
            Heart.writeImage(filePath, image);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
