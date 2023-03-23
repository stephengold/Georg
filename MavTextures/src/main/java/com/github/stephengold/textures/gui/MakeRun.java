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
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;

/**
 * A console application to generate the "run.png" texture for a button.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeRun extends MakeSquareTexture {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeRun.class.getName());
    // *************************************************************************
    // constructors

    private MakeRun() {
        super(2048);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeRun application.
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
        MakeRun application = new MakeRun();
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
        Color green = new Color(0f, 0.7f, 0f, opacity);
        Color black = new Color(0f, 0f, 0f, opacity);
        /*
         * Generate a color image map.
         */
        application.makeRun(green, black, "run.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a "run" button.
     */
    private void makeRun(Color fgColor, Color bgColor, String fileName) {
        Graphics2D graphics = createOpaque(fgColor);

        // circular background for the button itself
        double r = 0.44;
        int xy = (int) Math.round(textureSize * (0.5 - r));
        int diameter = (int) Math.round(textureSize * 2.0 * r);
        graphics.setColor(bgColor);
        graphics.fillOval(xy, xy, diameter, diameter);

        // right-pointing triangle
        int numPoints = 3;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];
        double height = 0.6;
        double width = 0.4;
        xPoints[0] = (int) Math.round(textureSize * (0.5 - 0.3 * width));
        yPoints[0] = (int) Math.round(textureSize * (0.5 - 0.5 * height));
        xPoints[1] = (int) Math.round(textureSize * (0.5 - 0.3 * width));
        yPoints[1] = (int) Math.round(textureSize * (0.5 + 0.5 * height));
        xPoints[2] = (int) Math.round(textureSize * (0.5 + 0.7 * width));
        yPoints[2] = (int) Math.round(textureSize * (0.5));
        graphics.setColor(fgColor);
        graphics.fillPolygon(xPoints, yPoints, numPoints);

        int finalSize = 128;
        downsampleAndWrite(finalSize, fileName);
    }
}
