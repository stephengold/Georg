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
package com.github.stephengold.textures.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;

/**
 * A console application to generate the "horn-silent.png" and "horn-sound.png"
 * textures for a horn button.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakeHorn extends MakeSquareTexture {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeHorn.class.getName());
    // *************************************************************************
    // constructors

    private MakeHorn() {
        super(2048);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeHorn application.
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
        MakeHorn application = new MakeHorn();
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
        Color black = new Color(0f, 0f, 0f, opacity);
        Color red = new Color(0.5f, 0f, 0f, opacity);
        Color yellow = new Color(0.7f, 0.7f, 0f, opacity);
        /*
         * Generate color image maps.
         */
        application.makeHorn(yellow, black, "horn-silent.png");
        application.makeHorn(yellow, red, "horn-sound.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a horn button.
     */
    private void makeHorn(Color fgColor, Color bgColor, String fileName) {
        Color white = new Color(1f, 1f, 1f, 1f);
        Graphics2D graphics = createOpaque(white);

        // circular background for the button itself
        double r = 0.44;
        int xy = (int) Math.round(textureSize * (0.5 - r));
        int diameter = (int) Math.round(textureSize * 2.0 * r);
        graphics.setColor(bgColor);
        graphics.fillOval(xy, xy, diameter, diameter);

        // c0 = center of lower arc of horn
        double c0x = 0.36;
        double c0y = 0.64;
        // c1 = center of upper arc of horn
        double c1x = 0.36;
        double c1y = 0.24;
        // c2 = center of left arc of coil
        double c2x = 0.45;
        double c2y = 0.58;
        // c3 = center of right arc of coil
        double c3x = 0.66;
        double c3y = 0.58;
        // c4 = center of mouthpiece
        double c4x = 0.88;
        double c4y = 0.44;
        r = 0.18;

        // the straight bore of the horn
        int x0 = (int) Math.round(textureSize * c0x);
        int y0 = (int) Math.round(textureSize * (c1y + r));
        int x1 = (int) Math.round(textureSize * c4x);
        int y1 = (int) Math.round(textureSize * (c0y - r));
        graphics.setColor(fgColor);
        graphics.fillRect(x0, y0, x1 - x0, y1 - y0);

        // the bell of the horn
        int numPoints = 26;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];
        for (int i = 0; i <= 12; ++i) {
            double theta = Math.PI * i / 24;
            double rcos = r * Math.cos(theta);
            double rsin = r * Math.sin(theta);
            xPoints[i] = (int) Math.round(textureSize * (c0x - rcos));
            yPoints[i] = (int) Math.round(textureSize * (c0y - rsin));
            xPoints[i + 13] = (int) Math.round(textureSize * (c1x - rsin));
            yPoints[i + 13] = (int) Math.round(textureSize * (c1y + rcos));
        }
        graphics.fillPolygon(xPoints, yPoints, numPoints);

        // the coil of the horn
        numPoints = 100;
        xPoints = new int[numPoints];
        yPoints = new int[numPoints];
        r = 0.12;
        double rr = 0.16;
        for (int i = 0; i <= 24; ++i) {
            double theta = Math.PI * i / 24;
            double rcos = r * Math.cos(theta);
            double rsin = r * Math.sin(theta);
            double rrcos = rr * Math.cos(theta);
            double rrsin = rr * Math.sin(theta);
            xPoints[i] = (int) Math.round(textureSize * (c2x - rsin));
            yPoints[i] = (int) Math.round(textureSize * (c2y - rcos));
            xPoints[i + 25] = (int) Math.round(textureSize * (c3x + rsin));
            yPoints[i + 25] = (int) Math.round(textureSize * (c3y + rcos));
            xPoints[i + 50] = (int) Math.round(textureSize * (c3x + rrsin));
            yPoints[i + 50] = (int) Math.round(textureSize * (c3y - rrcos));
            xPoints[i + 75] = (int) Math.round(textureSize * (c2x - rrsin));
            yPoints[i + 75] = (int) Math.round(textureSize * (c2y + rrcos));
        }
        graphics.fillPolygon(xPoints, yPoints, numPoints);

        // the mouthpiece of the horn
        numPoints = 13;
        xPoints = new int[numPoints];
        yPoints = new int[numPoints];
        r = 0.05;
        for (int i = 0; i <= 12; ++i) {
            double theta = Math.PI * i / 12;
            double rcos = r * Math.cos(theta);
            double rsin = r * Math.sin(theta);
            xPoints[i] = (int) Math.round(textureSize * (c4x - rsin));
            yPoints[i] = (int) Math.round(textureSize * (c4y - rcos));
        }
        graphics.fillPolygon(xPoints, yPoints, numPoints);

        int finalSize = 128;
        downsampleAndWrite(finalSize, fileName);
    }
}
