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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;
import org.imgscalr.Scalr;

/**
 * A console application to generate the "steering.png" texture for a
 * steering-wheel indicator.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakeSteering {
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
            = Logger.getLogger(MakeSteering.class.getName());
    /**
     * filesystem path to the asset directory/folder for output
     */
    final private static String assetDirPath = "build";
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeSteering application.
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
        MakeSteering application = new MakeSteering();
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
        /*
         * Generate a color image map.
         */
        application.makeSteering(black, "steering.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate an image map for a steering-wheel indicator.
     */
    private void makeSteering(Color fgColor, String assetPath) {
        /*
         * Create a blank, color buffered image for the texture map.
         */
        BufferedImage image = new BufferedImage(textureSize, textureSize,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = image.createGraphics();
        //graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        //        RenderingHints.VALUE_ANTIALIAS_ON);

        // circular rim
        graphics.setColor(fgColor);
        fillSection(graphics, 0.0, Math.PI);
        fillSection(graphics, Math.PI, 2.0 * Math.PI);

        // 2 spokes forming a chevron
        int numPoints = 6;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];
        double x0 = 0.04;
        double y0 = 0.6;
        double x3 = 0.8;
        double y3 = 0.85;
        xPoints[0] = (int) Math.round(textureSize * x0);
        yPoints[0] = (int) Math.round(textureSize * y0);
        xPoints[1] = (int) Math.round(textureSize * 0.5);
        yPoints[1] = (int) Math.round(textureSize * 0.35);
        xPoints[2] = (int) Math.round(textureSize * (1.0 - x0));
        yPoints[2] = (int) Math.round(textureSize * y0);
        xPoints[3] = (int) Math.round(textureSize * x3);
        yPoints[3] = (int) Math.round(textureSize * y3);
        xPoints[4] = (int) Math.round(textureSize * 0.5);
        yPoints[4] = (int) Math.round(textureSize * 0.6);
        xPoints[5] = (int) Math.round(textureSize * (1.0 - x3));
        yPoints[5] = (int) Math.round(textureSize * y3);
        graphics.fillPolygon(xPoints, yPoints, numPoints);
        /*
         * Downsample the image to the desired final size.
         */
        int finalSize = 256;
        BufferedImage downsampledImage = Scalr.resize(image,
                Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, finalSize,
                finalSize, Scalr.OP_ANTIALIAS);
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

    /**
     * Fill a section of the circular rim.
     *
     * @param graphics the graphics context on which to draw (not null)
     * @param startTheta starting angle, measured CCW from the +Y axis
     * @param endTheta ending angle, measured CCW from the +Y axis
     */
    private void fillSection(Graphics2D graphics, double startTheta,
            double endTheta) {
        int samplesPerArc = 64;
        double innerRadius = 0.43;
        double outerRadius = 0.5;

        double thetaStep = (endTheta - startTheta) / (samplesPerArc - 1);
        int numPoints = 2 * samplesPerArc;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        for (int i = 0; i < samplesPerArc; i++) {
            double theta = startTheta + i * thetaStep;
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);

            double xx = 0.5 + outerRadius * sin;
            double yy = 0.5 + outerRadius * cos;
            xPoints[i] = (int) Math.round(textureSize * xx);
            yPoints[i] = (int) Math.round(textureSize * yy);

            int j = numPoints - i - 1;
            xx = 0.5 + innerRadius * sin;
            yy = 0.5 + innerRadius * cos;
            xPoints[j] = (int) Math.round(textureSize * xx);
            yPoints[j] = (int) Math.round(textureSize * yy);
        }

        graphics.fillPolygon(xPoints, yPoints, numPoints);
    }
}
