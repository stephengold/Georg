/*
 Copyright (c) 2021, Stephen Gold
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
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3utilities.Heart;
import jme3utilities.MyString;
import jme3utilities.math.MyMath;

/**
 * A console application to generate the "smoke.png" texture for a particle.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class MakeSmoke extends MakeSquareTexture {
    // *************************************************************************
    // constants and loggers

    /**
     * message logger for this class
     */
    final private static Logger logger
            = Logger.getLogger(MakeSmoke.class.getName());
    // *************************************************************************
    // constructors

    private MakeSmoke() {
        super(64);
    }
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the MakeSmoke application.
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
        MakeSmoke application = new MakeSmoke();
        /*
         * Log the working directory.
         */
        String userDir = System.getProperty("user.dir");
        logger.log(Level.INFO, "working directory is {0}",
                MyString.quote(userDir));
        /*
         * Generate a color image map.
         */
        application.makeSmoke("smoke.png");
    }
    // *************************************************************************
    // private methods

    /**
     * Generate a color image map for a smoke particle.
     */
    private void makeSmoke(String fileName) {
        Graphics2D graphics = createBufferedImage();
        /*
         * Set the opacity of each pixel.
         */
        for (int x = 0; x < textureSize; x++) {
            for (int y = 0; y < textureSize; y++) {
                float xx = (2 * x - textureSize) / (float) textureSize;
                float yy = (2 * y - textureSize) / (float) textureSize;
                float alpha = 1f - MyMath.hypotenuse(xx, yy);
                alpha = FastMath.clamp(alpha, 0f, 1f);
                Heart.setGrayPixel(graphics, x, y, 1f, alpha);
            }
        }

        int finalSize = 32;
        downsampleAndWrite(finalSize, fileName);
    }
}
