/*
 Copyright (c) 2020-2021, Stephen Gold
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

import com.jme3.math.FastMath;
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
 * A console application to generate the "mine.png" texture for Fuze Creek 2-D.
 *
 * Reference image:
 * https://en.wikipedia.org/wiki/File:British_Mk_14_Sea_Mine.jpg
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakeMine {
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
            = Logger.getLogger(MakeMine.class.getName());
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
        Color mineColor = new Color(0.73f, 0f, 0f, opacity); // red
        /*
         * Generate the color image map.
         */
        makeMine(mineColor, wakeColor, waterColor);
    }
    // *************************************************************************
    // private methods

    /**
     * Generate a color image map to visualize a naval mine protruding from the
     * water.
     */
    private static void makeMine(Color mineColor, Color wakeColor,
            Color waterColor) {
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
         * Fill a teardrop-shaped "wake" with wakeColor.
         */
        float xCenter = 0.5f * textureSize;
        float yCenter = 0.6f * textureSize;
        float wakeRadius = 0.35f * textureSize;
        int leftX = Math.round(xCenter - wakeRadius);
        int topY = Math.round(yCenter - wakeRadius);
        int width = Math.round(xCenter + wakeRadius) - leftX;
        int height = Math.round(yCenter + wakeRadius) - topY;
        graphics.setColor(wakeColor);
        graphics.fillOval(leftX, topY, width, height);

        float halfBase = 0.312f;
        int x1 = Math.round(xCenter);
        int x2 = Math.round(xCenter + halfBase * textureSize);
        int y2 = Math.round(yCenter - 0.16f * textureSize);
        int x3 = Math.round(xCenter - halfBase * textureSize);
        int[] xPoints = {x1, x2, x3};
        int[] yPoints = {0, y2, y2};
        int numPoints = xPoints.length;
        graphics.fillPolygon(xPoints, yPoints, numPoints);
        /*
         * Fill a circle with mineColor.
         */
        float mineRadius = 0.3f * textureSize;
        leftX = Math.round(xCenter - mineRadius);
        topY = Math.round(yCenter - mineRadius);
        width = Math.round(xCenter + mineRadius) - leftX;
        height = Math.round(yCenter + mineRadius) - topY;
        graphics.setColor(mineColor);
        graphics.fillOval(leftX, topY, width, height);
        /*
         * Fill horns with mineColor.
         */
        float halfAngle = 0.1f;
        float hornR = 0.45f * textureSize;
        float rSin1 = hornR * FastMath.sin(FastMath.QUARTER_PI - halfAngle);
        float rSin2 = hornR * FastMath.sin(FastMath.QUARTER_PI + halfAngle);

        int xa = Math.round(xCenter - rSin2);
        int xb = Math.round(xCenter - rSin1);
        int xc = Math.round(xCenter + rSin1);
        int xd = Math.round(xCenter + rSin2);
        int ya = Math.round(yCenter - rSin2);
        int yb = Math.round(yCenter - rSin1);
        int yc = Math.round(yCenter + rSin1);
        int yd = Math.round(yCenter + rSin2);

        int[] xPointsHorns1 = {xb, xa, xc, xd};
        int[] yPointsHorns1 = {ya, yb, yd, yc};
        int numPointsHorns1 = xPointsHorns1.length;
        graphics.fillPolygon(xPointsHorns1, yPointsHorns1, numPointsHorns1);

        int[] xPointsHorns2 = {xb, xa, xc, xd};
        int[] yPointsHorns2 = {yd, yc, ya, yb};
        int numPointsHorns2 = xPointsHorns2.length;
        graphics.fillPolygon(xPointsHorns2, yPointsHorns2, numPointsHorns2);
        /*
         * Downsample the image (by 10x) to the desired final size.
         */
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalSize,
                finalSize, Scalr.OP_ANTIALIAS);
        /*
         * Write the downsampled image to the asset file.
         */
        String filePath = String.format("%s/mine.png", assetDirPath);
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
