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
 * A console application to generate bank textures for Fuze Creek 2-D.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeBanks {
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
            = Logger.getLogger(MakeBanks.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private MakeBanks() {
    }
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
        Color landColor = new Color(0.66f, 0.48f, 0.35f, opacity);
        Color waterColor = new Color(0f, 0f, 0.73f, opacity); // dark blue
        /*
         * Generate color image maps.
         */
        for (int upstreamDX = -1; upstreamDX <= 1; ++upstreamDX) {
            for (int downstreamDX = -1; downstreamDX <= 1; ++downstreamDX) {
                makeBank("left", landColor, waterColor, upstreamDX,
                        downstreamDX);
                makeBank("right", waterColor, landColor, upstreamDX,
                        downstreamDX);
            }
        }
    }
    // *************************************************************************
    // private methods

    /**
     * Generate a color image map to visualize a LeftBankCell or RightBankCell.
     */
    private static void makeBank(String leftRight, Color leftColor,
            Color rightColor, int upstreamDeltaX, int downstreamDeltaX) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureSize, textureSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        /*
         * Start with all pixels leftColor.
         */
        graphics.setColor(leftColor);
        graphics.fillRect(0, 0, textureSize, textureSize);
        /*
         * Fill a polygon on the right side with rightColor.
         */
        int center = textureSize / 2;
        int downstream = pixelX(downstreamDeltaX);
        int upstream = pixelX(-upstreamDeltaX);
        int right = textureSize;
        int[] xPoints = {right, right, upstream, center, downstream};

        downstream = 0;
        upstream = textureSize;
        int[] yPoints = {downstream, upstream, upstream, center, downstream};

        graphics.setColor(rightColor);
        int numPoints = xPoints.length;
        graphics.fillPolygon(xPoints, yPoints, numPoints);
        /*
         * Downsample the image (by 10x) to the desired final size.
         */
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalSize,
                finalSize, Scalr.OP_ANTIALIAS);
        /*
         * Write the downsampled image to the asset file.
         */
        String upstreamMpz = mpz(upstreamDeltaX);
        String downstreamMpz = mpz(downstreamDeltaX);
        String filePath = String.format("%s/%sBank%s%s.png", assetDirPath,
                leftRight, upstreamMpz, downstreamMpz);
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Convert a deltaX value to a name.
     *
     * @param deltaX the value to convert (-1, 0, or +1)
     * @return a string of text
     */
    private static String mpz(int deltaX) {
        switch (deltaX) {
            case -1:
                return "Minus";
            case 0:
                return "Zero";
            case +1:
                return "Plus";
            default:
                throw new IllegalArgumentException("deltaX = " + deltaX);
        }
    }

    private static int pixelX(int deltaX) {
        switch (deltaX) {
            case -1:
                return 0;
            case 0:
                return textureSize / 2;
            case +1:
                return textureSize;
            default:
                throw new IllegalArgumentException("deltaX = " + deltaX);
        }
    }
}
