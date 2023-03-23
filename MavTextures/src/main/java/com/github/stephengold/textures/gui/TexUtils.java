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

import java.awt.Graphics2D;
import java.util.logging.Logger;

/**
 * Utility methods used to generate textures.
 *
 * @author Stephen Gold sgold@sonic.net
 */
final class TexUtils {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(TexUtils.class.getName());
    // *************************************************************************
    // constructors

    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private TexUtils() {
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Fill a section of a circular ring with the current color.
     *
     * @param graphics the graphics context on which to draw (not null)
     * @param outerRadius the outer radius of the ring (&gt;0)
     * @param startTheta starting angle, measured CCW from the +Y axis
     * @param endTheta ending angle, measured CCW from the +Y axis
     * @param textureSize size of the texture map (pixels per side)
     */
    static void fillSection(Graphics2D graphics, double innerRadius,
            double outerRadius, double startTheta, double endTheta,
            int textureSize) {
        int samplesPerArc = 99;
        double thetaStep = (endTheta - startTheta) / (samplesPerArc - 1);
        int numPoints = 2 * samplesPerArc;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        for (int i = 0; i < samplesPerArc; ++i) {
            double theta = startTheta + i * thetaStep;
            double sin = Math.sin(theta);
            double cos = Math.cos(theta);

            double x = 0.5 + outerRadius * sin;
            double y = 0.5 + outerRadius * cos;
            xPoints[i] = (int) Math.round(textureSize * x);
            yPoints[i] = (int) Math.round(textureSize * y);

            int j = numPoints - i - 1;
            x = 0.5 + innerRadius * sin;
            y = 0.5 + innerRadius * cos;
            xPoints[j] = (int) Math.round(textureSize * x);
            yPoints[j] = (int) Math.round(textureSize * y);
        }

        graphics.fillPolygon(xPoints, yPoints, numPoints);
    }

    /**
     * Fill a speaker shape (concave hexagon) with the current color.
     */
    static void fillSpeaker(Graphics2D graphics, int textureSize) {
        int numPoints = 6;
        int[] xPoints = new int[numPoints];
        int[] yPoints = new int[numPoints];

        double height = 0.6;
        double width = 0.3;
        double x1 = 0.4;
        double x2 = x1 + width / 2;
        double x0 = x1 - width / 2;

        xPoints[0] = (int) Math.round(textureSize * x0);
        yPoints[0] = (int) Math.round(textureSize * (0.5 - 0.2 * height));
        xPoints[1] = (int) Math.round(textureSize * x1);
        yPoints[1] = (int) Math.round(textureSize * (0.5 - 0.2 * height));
        xPoints[2] = (int) Math.round(textureSize * x2);
        yPoints[2] = (int) Math.round(textureSize * (0.5 - 0.5 * height));
        xPoints[3] = (int) Math.round(textureSize * x2);
        yPoints[3] = (int) Math.round(textureSize * (0.5 + 0.5 * height));
        xPoints[4] = (int) Math.round(textureSize * x1);
        yPoints[4] = (int) Math.round(textureSize * (0.5 + 0.2 * height));
        xPoints[5] = (int) Math.round(textureSize * x0);
        yPoints[5] = (int) Math.round(textureSize * (0.5 + 0.2 * height));
        graphics.fillPolygon(xPoints, yPoints, numPoints);
    }
}
