/*
 Copyright (c) 2020-2023, Stephen Gold
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
package com.github.stephengold.textures.fc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;
import org.imgscalr.Scalr;

/**
 * A console application to generate the "rock.png" texture for Fuze Creek 2-D.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeRock {
    // *************************************************************************
    // constants and loggers

    /**
     * size of the texture map (pixels per side)
     */
    final private static int finalSize = 64;
    final private static int textureSize = 640;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeRock.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeBanks application.
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
         * Log the working directory.
         */
        String userDir = System.getProperty("user.dir");
        logger.log(Level.INFO, "working directory is {0}",
                MyString.quote(userDir));
        /*
         * Define colors.
         */
        float opacity = 1f;
        Color wakeColor = new Color(0.24f, 0.24f, 0.73f, opacity); // light blue
        Color waterColor = new Color(0f, 0f, 0.73f, opacity); // dark blue
        float bright = 1f;
        Color color1 = new Color(bright, bright, bright, opacity); // white
        Color landColor = new Color(0.66f, 0.48f, 0.35f, opacity);
        Color color3 = new Color(0.33f, 0.24f, 0.17f, opacity);
        /*
         * Generate the color image map.
         */
        makeRock(color1, landColor, color3, wakeColor, waterColor);
    }
    // *************************************************************************
    // private methods

    /**
     * Generate a color image map to visualize a sharp rock protruding from the
     * water.
     */
    private static void makeRock(Color rockColor1, Color rockColor2,
            Color rockColor3, Color wakeColor, Color waterColor) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureSize, textureSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        /*
         * Start with all pixels waterColor.
         */
        graphics.setColor(waterColor);
        graphics.fillRect(0, 0, textureSize, textureSize);
        /*
         * Fill a kite-shaped "wake" with wakeColor.
         */
        int center = textureSize / 2;
        int p1 = textureSize / 5;
        int p2 = textureSize - p1;
        int[] xPoints = {center, p1, center, p2};
        int[] yPoints = {0, center, p2, center};
        graphics.setColor(wakeColor);
        int numPoints = xPoints.length;
        graphics.fillPolygon(xPoints, yPoints, numPoints);
        /*
         * Fill a small square with rockColor2.
         */
        int low = textureSize / 4;
        int high = textureSize - low;
        int[] xPoints2 = {center, high, center, low};
        int[] yPoints2 = {high, center, low, center};
        graphics.setColor(rockColor2);
        int numPoints2 = xPoints2.length;
        graphics.fillPolygon(xPoints2, yPoints2, numPoints2);
        /*
         * Fill the upper-right corner with rockColor1.
         */
        int[] xPoints1 = {center, center, high};
        int[] yPoints1 = {center, high, center};
        graphics.setColor(rockColor1);
        int numPoints1 = xPoints1.length;
        graphics.fillPolygon(xPoints1, yPoints1, numPoints1);
        /*
         * Fill the lower-left corner with rockColor3.
         */
        int[] xPoints3 = {center, center, low};
        int[] yPoints3 = {center, low, center};
        graphics.setColor(rockColor3);
        int numPoints3 = xPoints3.length;
        graphics.fillPolygon(xPoints3, yPoints3, numPoints3);
        /*
         * Downsample the image (by 10x) to the desired final size.
         */
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalSize,
                finalSize, Scalr.OP_ANTIALIAS);
        /*
         * Write the downsampled image to the asset file.
         */
        String filePath
                = String.format("%s/rock.png", assetDirPath);
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
