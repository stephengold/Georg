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
 * A console application to generate the "powered-by.jpeg" texture.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakePoweredBy {
    // *************************************************************************
    // constants and loggers

    /**
     * size of the texture map (pixels per side)
     */
    final private static int textureSize = 2048;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakePoweredBy.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakePoweredBy application.
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
        MakePoweredBy application = new MakePoweredBy();
        /*
         * Log the working directory.
         */
        String userDir = System.getProperty("user.dir");
        logger.log(Level.INFO, "working directory is {0}",
                MyString.quote(userDir));
        /*
         * Generate a color image map.
         */
        application.makePoweredBy();
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map.
     */
    private void makePoweredBy() {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureSize, textureSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        /*
         * Start with all pixels bgColor.
         */
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, textureSize, textureSize);
        /*
         * Add 10 stars.
         */
        for (int yIndex = 0; yIndex < 2; ++yIndex) {
            for (int xIndex = 0; xIndex < 5; ++xIndex) {
                graphics.setColor(Color.BLACK);
                drawStar(graphics, xIndex, yIndex, 0.09);
                graphics.setColor(new Color(0.953f, 0.784f, 0f, 1f));
                drawStar(graphics, xIndex, yIndex, 0.08);
            }
        }
        /*
         * Add the lettering.
         */
        graphics.setColor(Color.DARK_GRAY);
        graphics.setFont(new Font("Serif", Font.ITALIC, 140));
        drawString(graphics, "POWERED BY:", 0.5, 0.5);

        graphics.setColor(new Color(0.294f, 0.063f, 0f, 1f));
        graphics.setFont(new Font("Serif", Font.BOLD, 180));
        drawString(graphics, "jMonkeyEngine", 0.5, 0.64);
        /*
         * Downsample the image to the desired final size.
         */
        int finalSize = 512;
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalSize,
                finalSize, Scalr.OP_ANTIALIAS);
        /*
         * Write the downsampled image to the asset file.
         */
        String filePath
                = String.format("%s/%s", assetDirPath, "powered-by.png");
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    // *************************************************************************
    // private methods

    private void drawStar(Graphics2D graphics, int xIndex, int yIndex,
            double outerRadius) {
        int numPoints = 10;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];
        double startTheta = Math.PI;
        double innerRadius = outerRadius / 2;
        double centerX = 0.1 + 0.2 * xIndex;
        double centerY = 0.3 + 0.5 * yIndex;

        double thetaStep = Math.PI / 5;
        for (int i = 0; i < 10; ++i) {
            double theta = startTheta + i * thetaStep;
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);

            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double x = centerX + radius * sin;
            double y = centerY + radius * cos;
            xPoints[i] = (int) Math.round(textureSize * x);
            yPoints[i] = (int) Math.round(textureSize * y);
        }

        graphics.fillPolygon(xPoints, yPoints, numPoints);
    }

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

        int x = (int) Math.round(textureSize * centerX - width / 2.0);
        int y = (int) Math.round(textureSize * baseY);
        graphics.drawString(text, x, y);
    }
}
