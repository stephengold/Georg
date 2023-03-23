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
package com.github.stephengold.textures.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;
import org.imgscalr.Scalr;

/**
 * A console application to generate the "compass.png" texture.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeCompass {
    // *************************************************************************
    // constants and loggers

    /**
     * dimensions of the texture map (pixels per side)
     */
    final private static int textureHeight = 400;
    final private static int textureWidth = 3600;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeCompass.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeCompass application.
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
        MakeCompass application = new MakeCompass();
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
        Color black = new Color(0f, 0f, 0f, opacity);
        /*
         * Generate a color image map.
         */
        application.makeCompass(white, black, "compass.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a compass.
     */
    private void makeCompass(Color fgColor, Color bgColor, String assetPath) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureWidth, textureHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        /*
         * Start with all pixels bgColor.
         */
        graphics.setColor(bgColor);
        graphics.fillRect(0, 0, textureWidth, textureHeight);
        /*
         * band around the equator
         */
        int x1 = Math.round(0f);
        int x2 = Math.round(textureWidth);
        int y1 = (int) Math.round(textureHeight * 0.7);
        int y2 = (int) Math.round(textureHeight * 0.8);
        graphics.setColor(fgColor);
        graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
        /*
         * 24 minor hash marks: one every 15 degrees of azimuth
         */
        double halfWidth = 0.002;
        for (int hashI = 0; hashI <= 24; ++hashI) {
            x1 = (int) Math.round(textureWidth * (hashI / 24.0 - halfWidth));
            x2 = (int) Math.round(textureWidth * (hashI / 24.0 + halfWidth));
            y1 = (int) Math.round(textureHeight * 0.8);
            y2 = (int) Math.round(textureHeight * 0.95);
            graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
        }
        /*
         * 8 major hash marks: one every 45 degrees of azimuth
         */
        halfWidth = 0.004;
        for (int hashI = 0; hashI <= 8; ++hashI) {
            x1 = (int) Math.round(textureWidth * (hashI / 8.0 - halfWidth));
            x2 = (int) Math.round(textureWidth * (hashI / 8.0 + halfWidth));
            y1 = (int) Math.round(textureHeight * 0.6);
            y2 = (int) Math.round(textureHeight * 0.95);
            graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
        }
        /*
         * 4 principal directions: one every 90 degrees of azimuth
         */
        graphics.setFont(new Font("Serif", Font.BOLD, 180));
        double baseY = 0.5;
        drawString(graphics, "N", 0.0, baseY);
        drawString(graphics, "E", 0.25, baseY);
        drawString(graphics, "S", 0.5, baseY);
        drawString(graphics, "W", 0.75, baseY);
        drawString(graphics, "N", 1.0, baseY);
        /*
         * 4 secondary directions
         */
        graphics.setFont(new Font("Serif", Font.BOLD, 120));
        drawString(graphics, "NE", 1 / 8.0, baseY);
        drawString(graphics, "SE", 3 / 8.0, baseY);
        drawString(graphics, "SW", 5 / 8.0, baseY);
        drawString(graphics, "NW", 7 / 8.0, baseY);
        /*
         * Downsample the image (by 10x) to the desired final size.
         */
        int finalHeight = textureHeight / 10;
        int finalWidth = textureWidth / 10;
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalWidth,
                finalHeight, Scalr.OP_ANTIALIAS);
        /*
         * Write the downsampled image to the asset file.
         */
        String filePath = String.format("%s/%s", assetDirPath, assetPath);
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    // *************************************************************************
    // private methods

    /**
     * Draw a string of text centered at the specified X coordinate.
     *
     * @param graphics the graphics context on which to draw (not null)
     * @param text the text to draw (not null)
     * @param centerX the X coordinate for the center (&ge;0, &le;1)
     * @param baseY the Y coordinate for the baseline (&ge;0, &le;1)
     */
    private void drawString(Graphics2D graphics, String text, double centerX,
            double baseY) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int width = fontMetrics.stringWidth(text);

        int x = (int) Math.round(textureWidth * centerX - width / 2.0);
        int y = (int) Math.round(textureHeight * baseY);
        graphics.drawString(text, x, y);
    }
}
