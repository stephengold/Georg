/*
 Copyright (c) 2021-2023, Stephen Gold
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

import com.jme3.math.FastMath;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;

/**
 * A console application to generate the "skidmark.png" texture.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeSkidmark {
    // *************************************************************************
    // constants and loggers

    /**
     * dimensions of the texture map (pixels per side)
     */
    final private static int textureHeight = 2;
    final private static int textureWidth = 64;
    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeSkidmark.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeSkidmark application.
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
        MakeSkidmark application = new MakeSkidmark();
        /*
         * Log the working directory.
         */
        String userDir = System.getProperty("user.dir");
        logger.log(Level.INFO, "working directory is {0}",
                MyString.quote(userDir));
        /*
         * Generate a color image map.
         */
        application.makeSkidmark("skidmark.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a skidmark.
     */
    private void makeSkidmark(String fileName) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureWidth, textureHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();

        float x1 = 0.22f;
        float x2 = 0.36f;
        float x3 = 0.5f;

        float y0 = 0.66f;
        float y1 = 0.4f;
        float y2 = 0.66f;
        float y3 = 0.017f;

        float maxColumnIndex = textureWidth - 1;
        for (int columnIndex = 0; columnIndex < textureWidth; ++columnIndex) {
            float x = FastMath.abs(columnIndex / maxColumnIndex - 0.5f);
            /*
             * Calculate alpha using a piecewise-linear function.
             */
            float alpha;
            if (x > x2) {
                float slope = (y3 - y2) / (x3 - x2);
                float t = x - x2;
                alpha = y2 + slope * t;
            } else if (x > x1) { // x1 < x <= x2
                float slope = (y2 - y1) / (x2 - x1);
                float t = x - x1;
                alpha = y1 + slope * t;
            } else { // x <= x1
                float slope = (y1 - y0) / x1;
                alpha = y0 + slope * x;
            }

            for (int rowIndex = 0; rowIndex < textureHeight; ++rowIndex) {
                Heart.setGrayPixel(graphics, columnIndex, rowIndex, 1f, alpha);
            }
        }
        /*
         * Write the image to the asset file.
         */
        String filePath = String.format("%s/%s", assetDirPath, fileName);
        try {
            Heart.writeImage(filePath, image);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
