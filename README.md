The Shapes Project provides a set of procedurally generated textures
for use in grahical user interfaces (GUIs).

Complete source code (in Java) is provided under [a BSD license][license].

<img height="150" src="https://i.imgur.com/qsSK33r.png">

<a name="build"/>

## How to build Shapes from source

 1. Install build software:
   + a Java Development Kit and
   + [Gradle]
 2. Download and extract the source code from GitHub:
   + using Git:
     + `git clone https://github.com/stephengold/Shapes.git`
     + `cd Shapes`
 3. Set the `JAVA_HOME` environment variable:
   + using Bash:  `export JAVA_HOME="` *path to your JDK* `"`
   + using Windows Command Prompt:  `set JAVA_HOME="` *path to your JDK* `"`
 4. Run the Gradle wrapper:
   + using Bash:  `./gradlew textures`
   + using Windows Command Prompt:  `.\gradlew textures`

After a successful build,
texture assets will be found in `Textures/build`.

<a name="conventions"/>

## Conventions

Package names begin with `com.github.stephengold.textures.gui`

The source code is compatible with JDK 7.

[ant]: https://ant.apache.org "Apache Ant Project"
[bsd3]: https://opensource.org/licenses/BSD-3-Clause "3-Clause BSD License"
[firefox]: https://www.mozilla.org/en-US/firefox "Firefox"
[git]: https://git-scm.com "Git"
[github]: https://github.com "GitHub"
[gradle]: https://gradle.org "Gradle Project"
[heart]: https://github.com/stephengold/Heart "Heart Project"
[jme]: https://jmonkeyengine.org  "jMonkeyEngine Project"
[license]: https://github.com/stephengold/Shapes/blob/master/LICENSE "Shapes license"
[markdown]: https://daringfireball.net/projects/markdown "Markdown Project"
[minie]: https://github.com/stephengold/Minie "Minie Project"
[mint]: https://linuxmint.com "Linux Mint Project"
[netbeans]: https://netbeans.org "NetBeans Project"
[utilities]: https://github.com/stephengold/jme3-utilities "Jme3-utilities Project"

<a name="acks"/>

## Acknowledgments

Like most projects, the Shapes Project builds on the work of many who
have gone before.  I therefore acknowledge the following
software developers:

 + plus the creators of (and contributors to) the following software:
    + the [Git] revision-control system and GitK commit viewer
    + the [Firefox] web browser
    + the [Gradle] build tool
    + the Java compiler, standard doclet, and runtime environment
    + [jMonkeyEngine][jme] and the jME3 Software Development Kit
    + the [Linux Mint][mint] operating system
    + LWJGL, the Lightweight Java Game Library
    + the [Markdown] document-conversion tool
    + the [NetBeans] integrated development environment

I am grateful to [Github]
for providing free hosting for this project
and many other open-source projects.

I'm also grateful to my dear Holly, for keeping me sane.

If I've misattributed anything or left anyone out, please let me know so I can
correct the situation: sgold@sonic.net
