The Georg Project provides procedurally generated assets
for use in [JMonkeyEngine][jme] applications.

It contains 3 subprojects:

1. FuzeCreekTextures: generate textures for [the FuzeCreek project][fuzecreek]
2. HeartTextures: generate textures for [the Heart project][heart]
3. MavTextures: generate textures for [the More Advanced Vehicles project][mav]

Complete source code (in Java) is provided under
[a 3-clause BSD license][license].

<img height="450" src="https://i.imgur.com/4B5J8jU.png">

<a name="build"></a>

## How to build Georg from source

1. Install a [Java Development Kit (JDK)][adoptium],
   if you don't already have one.
2. Download and extract the Georg source code from GitHub:
  + using [Git]:
    + `git clone https://github.com/stephengold/Georg.git`
    + `cd Georg`
3. Point the `JAVA_HOME` environment variable to your JDK installation:
   (In other words, set it to the path of a directory/folder
   containing a "bin" that contains a Java executable.
   That path might look something like
   "C:\Program Files\Eclipse Adoptium\jdk-17.0.3.7-hotspot"
   or "/usr/lib/jvm/java-17-openjdk-amd64/" or
   "/Library/Java/JavaVirtualMachines/zulu-17.jdk/Contents/Home" .)
  + using Bash or Zsh: `export JAVA_HOME="` *path to installation* `"`
  + using [Fish]: `set -g JAVA_HOME "` *path to installation* `"`
  + using Windows Command Prompt: `set JAVA_HOME="` *path to installation* `"`
  + using PowerShell: `$env:JAVA_HOME = '` *path to installation* `'`
4. Run the [Gradle] wrapper:
  + using Bash or Fish or PowerShell or Zsh: `./gradlew textures`
  + using Windows Command Prompt: `.\gradlew textures`

After a successful build,
texture assets will be found in `FuzeCreekTextures/build`,
`HeartTextures/build`, and `MavTextures/build`.

<a name="conventions"></a>

## Conventions

The source code is compatible with JDK 7.


<a name="acks"></a>

## Acknowledgments

Like most projects, the Georg Project builds on the work of many who
have gone before.  I therefore acknowledge the creators of (and contributors to)
the following software:

+ the [Firefox] web browser
+ the [Git] revision-control system and GitK commit viewer
+ the [GitKraken] client
+ the [Gradle] build tool
+ the [ImgScalr] Library
+ the Java compiler, standard doclet, and virtual machine
+ [jMonkeyEngine][jme] and the jME3 Software Development Kit
+ the [Linux Mint][mint] operating system
+ LWJGL, the Lightweight Java Game Library
+ the [Markdown] document-conversion tool
+ the [NetBeans] integrated development environment

I am grateful to [Github] and [Imgur]
for providing free hosting for this project
and many other open-source projects.

I'm also grateful to my dear Holly, for keeping me sane.

If I've misattributed anything or left anyone out, please let me know, so I can
correct the situation: sgold@sonic.net


[adoptium]: https://adoptium.net/releases.html "Adoptium Project"
[ant]: https://ant.apache.org "Apache Ant Project"
[bsd3]: https://opensource.org/licenses/BSD-3-Clause "3-Clause BSD License"
[firefox]: https://www.mozilla.org/en-US/firefox "Firefox"
[fish]: https://fishshell.com/ "Fish command-line shell"
[fuzecreek]: https://github.com/stephengold/FuzeCreek "Fuze Creek Project"
[git]: https://git-scm.com "Git"
[github]: https://github.com "GitHub"
[gitkraken]: https://www.gitkraken.com "GitKraken client"
[gradle]: https://gradle.org "Gradle Project"
[heart]: https://github.com/stephengold/Heart "Heart Project"
[imgscalr]: https://github.com/rkalla/imgscalr "ImgScalr Library"
[imgur]: https://imgur.com/ "Imgur"
[jme]: https://jmonkeyengine.org "jMonkeyEngine Project"
[license]: https://github.com/stephengold/Georg/blob/master/LICENSE "Georg license"
[markdown]: https://daringfireball.net/projects/markdown "Markdown Project"
[mav]: https://github.com/stephengold/jme-vehicles "More Advanced Vehicles Project"
[minie]: https://github.com/stephengold/Minie "Minie Project"
[mint]: https://linuxmint.com "Linux Mint Project"
[netbeans]: https://netbeans.org "NetBeans Project"
[utilities]: https://github.com/stephengold/jme3-utilities "Jme3-utilities Project"
