#!/bin/bash

set -eu

SDIR=/home/sgold/Git/Georg/FuzeCreekTextures/build
DDIR=/home/sgold/Git/FuzeCreek/FC2D/src/main/resources/Textures/cells
/bin/cp $SDIR/*Bank*.png $DDIR

DDIR=/home/sgold/GitFuzeCreek/FC2D/src/main/resources/Textures
/bin/cp $SDIR/raft*.png $DDIR


SDIR=/home/sgold/Git/Georg/HeartTextures/build
DDIR=/home/sgold/Git/Heart/HeartLibrary/src/main/resources/Textures/shapes
/bin/cp $SDIR/*.png $DDIR

SDIR=/home/sgold/Git/Georg/MavTextures/build
DDIR=/home/sgold/Git/jme-vehicles/MavCommon/src/main/resources/Textures/Georg
/bin/cp $SDIR/horn-silent.png $SDIR/horn-sound.png \
        $SDIR/left-triangle.png $SDIR/mute.png $SDIR/pause.png \
        $SDIR/power-off.png $SDIR/power-on.png \
        $SDIR/run.png $SDIR/single-step.png $SDIR/sound.png  $DDIR

SDIR=/home/sgold/Git/Georg/MavTextures/build
DDIR=/home/sgold/Git/jme-vehicles/MavLibrary/src/main/resources/Textures/Georg
/bin/cp $SDIR/compass.png $SDIR/skidmark.png $SDIR/smoke.png \
        $SDIR/speedo_bg_2.png $SDIR/speedo_needle_2.png \
        $SDIR/steering.png $SDIR/tachometer_bg.png  $DDIR
