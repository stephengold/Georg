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
 * A console application to generate the "speedo_bg_2.png" and
 * "tachometer_bg.png" textures for dials.
 *
 * @author Stephen Gold sgold@sonic.net
 */
class MakeDials extends MakeSquareTexture {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeDials.class.getName());
    // *************************************************************************
    // constructors

    private MakeDials() {
        super(2048);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeDials application.
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
        MakeDials application = new MakeDials();
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
        Color red = new Color(0.7f, 0f, 0f, opacity);
        Color black = new Color(0f, 0f, 0f, opacity);
        /*
         * Generate color image maps.
         */
        application.makeDial(black, "speedo_bg_2.png");
        application.makeDial(red, "tachometer_bg.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for the background of a dial.
     */
    private void makeDial(Color redzoneColor, String fileName) {
        Graphics2D graphics = createBufferedImage();
        /*
         * Fill the (cyan) outer edge.
         */
        double innerRadius = 0.48;
        double outerRadius = 0.495;
        Color cyan = new Color(0f, 0.3f, 0.3f, 1f);
        graphics.setColor(cyan);
        TexUtils.fillSection(graphics, innerRadius, outerRadius,
                0.0, 2.0, textureSize);
        TexUtils.fillSection(graphics, innerRadius, outerRadius,
                1.9, 2.1 * Math.PI, textureSize);
        /*
         * Fill the main (black) ring.
         */
        innerRadius = 0.3;
        outerRadius = 0.482;
        Color black = new Color(0f, 0f, 0f, 1f);
        graphics.setColor(black);
        TexUtils.fillSection(graphics, innerRadius, outerRadius,
                0.0, 2.0, textureSize);
        TexUtils.fillSection(graphics, innerRadius, outerRadius,
                1.9, 2.1 * Math.PI, textureSize);
        /*
         * Fill the redzone.
         */
        double redzoneRadius = 0.44;
        double halfPi = Math.PI / 2.0;
        graphics.setColor(redzoneColor);
        TexUtils.fillSection(graphics, redzoneRadius, outerRadius,
                halfPi - 1.0, halfPi, textureSize);

        int finalSize = 201;
        downsampleAndWrite(finalSize, fileName);
    }
}
