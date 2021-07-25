#!/bin/bash

set -eu

CP=/bin/cp
GIT=/home/sgold/Git
GEORG=$GIT/Georg

SDIR=$GEORG/FuzeCreekTextures/build
DDIR=$GIT/FuzeCreek/FC2D/src/main/resources/Textures/cells
$CP $SDIR/*Bank*.png $SDIR/rock.png  $DDIR

DDIR=$GIT/FuzeCreek/FC2D/src/main/resources/Textures
$CP $SDIR/raft*.png $DDIR


SDIR=$GEORG/HeartTextures/build
DDIR=$GIT/Heart/HeartLibrary/src/main/resources/Textures/shapes
$CP $SDIR/*.png $DDIR


SDIR=$GEORG/MavTextures/build
DDIR=$GIT/jme-vehicles/MavCommon/src/main/resources/Textures/Georg
$CP $SDIR/horn-*.png $SDIR/left-triangle.png $SDIR/mute.png $SDIR/pause.png \
    $SDIR/power-*.png $SDIR/run.png $SDIR/single-step.png $SDIR/sound.png  $DDIR

DDIR=$GIT/jme-vehicles/MavLibrary/src/main/resources/Textures/Georg
$CP $SDIR/compass.png $SDIR/skidmark.png $SDIR/smoke.png \
    $SDIR/speedo*.png $SDIR/steering.png $SDIR/tachometer_bg.png  $DDIR
