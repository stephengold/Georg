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
import jme3utilities.math.MyMath;
import org.imgscalr.Scalr;

/**
 * A console application to generate the "raft2.png" texture for Fuze Creek 2-D.
 *
 * Reference image:
 * https://www.outdoorplay.com/images/sca-prod-imgs/aire-bubbabomb-inflatable-river-tube.Red.02.jpg
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakeRaft {
    // *************************************************************************
    // constants and loggers

    /**
     * square root of 2
     */
    final public static float root2 = FastMath.sqrt(2f);
    /**
     * "silver ratio"
     */
    final public static float deltaS = 1f + root2;
    /**
     * height of the texture map (in pixels)
     */
    final private static int textureHeight = 640;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeRaft.class.getName());
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
        Color floatColor = new Color(0f, 0.5f, 0f, opacity); // green
        Color floorColor = new Color(0.2f, 0.2f, 0.2f, opacity); // dark gray
        Color waterColor = new Color(0f, 0f, 0.73f, opacity); // dark blue
        /*
         * Generate the color image map.
         */
        int raftWidth = 2; // width of the raft (in cells)
        makeRaft(raftWidth, floatColor, floorColor, waterColor);
    }
    // *************************************************************************
    // private methods

    /**
     * Generate a color image map to visualize an octagonal inflatable raft.
     */
    private static void makeRaft(int raftWidth, Color floatColor,
            Color floorColor, Color waterColor) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        int textureWidth = raftWidth * textureHeight;
        BufferedImage image = new BufferedImage(textureWidth, textureHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        /*
         * Start with all pixels waterColor.
         */
        graphics.setColor(waterColor);
        graphics.fillRect(0, 0, textureWidth, textureHeight);
        /*
         * Fill a large octagon with floatColor.
         */
        int left = 0;
        int right = textureWidth;
        int bottom = 0;
        int top = textureHeight;
        float a = (top - bottom) / deltaS;  // length of each short side
        float c = MyMath.rootHalf * a; // leg of each corner triangle

        int x1 = Math.round(left + c); // corner X coordinate
        int x2 = Math.round(right - c); // corner X coordinate
        int[] xPoints = {left, x1, x2, right, right, x2, x1, left};

        int y1 = Math.round(bottom + c); // corner Y coordinate
        int y2 = Math.round(top - c); // corner Y coordinate
        int[] yPoints = {y1, bottom, bottom, y1, y2, top, top, y2};

        graphics.setColor(floatColor);
        int numPoints = xPoints.length;
        graphics.fillPolygon(xPoints, yPoints, numPoints);
        /*
         * Fill an inner octagon with floorColor.
         */
        float thickness = 0.3f * (top - bottom); // thickness of the float
        int pixels = Math.round(thickness);
        left += pixels;
        right -= pixels;
        bottom += pixels;
        top -= pixels;
        a = (top - bottom) / deltaS;  // length of each short side
        c = MyMath.rootHalf * a; // leg of each corner triangle

        x1 = Math.round(left + c); // corner X coordinate
        x2 = Math.round(right - c); // corner X coordinate
        int[] innerXPoints = {left, x1, x2, right, right, x2, x1, left};

        y1 = Math.round(bottom + c); // corner Y coordinate
        y2 = Math.round(top - c); // corner Y coordinate
        int[] innerYPoints = {y1, bottom, bottom, y1, y2, top, top, y2};

        graphics.setColor(floorColor);
        int innerNumPoints = innerXPoints.length;
        graphics.fillPolygon(innerXPoints, innerYPoints, innerNumPoints);
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
        String filePath
                = String.format("%s/raft%d.png", assetDirPath, raftWidth);
        try {
            Heart.writeImage(filePath, downsampledImage);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
