# 159.261 Game Programming (Assignment 2)
**Copyright (c) 2021, Damian Coventry**

**All rights reserved**

![Main Menu](/ScreenShot0.png "Main Menu")  
![Episode Splash](/ScreenShot1.png "Episode Splash")  
![In Game](/ScreenShot2.png "In Game")  

## Massey University Disclaimer
This is an assignment for Massey University course 159.261. Under no circumstances is it to be copied and submitted anywhere as plagiarised work. It is work created solely by me as original work. It has been submitted for 159.261 Game Programming S2 2021.  

## Building and running the project
I built this will IntelliJ, but it also builds with Eclipse.

### Dependencies
 - [Libbulletjme](https://github.com/stephengold/Libbulletjme). This project provides the ability to use the Bullet Physics engine within Java.
 - [LWJGL - Lightweight Java Game Library 3](https://github.com/LWJGL/lwjgl3/releases). This project provides access to OpenGL and GLSL shaders.
 - [JOML – Java OpenGL Math Library](https://github.com/JOML-CI/JOML). This project provides Vector and Matrix maths routines that slot in well to LWJGL.

### Setup
I created a local directory named `C:\Development\159.261-Game-Prog-Assgn2`. I created a subdirectory within this directory named `lib`.

The JDK I'm using is `openjdk-16`.

### Libbulletjme
Go to this link https://github.com/stephengold/Libbulletjme/releases/tag/10.5.0  
Download these files. Store them in the directory `lib`.  
`Libbulletjme-10.5.0.jar`  
`Libbulletjme-10.5.0-sources.jar`  
`Windows32DebugDp_bulletjme.dll`  
`Windows32DebugSp_bulletjme.dll`  
`Windows32ReleaseDp_bulletjme.dll`  
`Windows32ReleaseSp_bulletjme.dll`  
`Windows64DebugDp_bulletjme.dll`  
`Windows64DebugSp_bulletjme.dll`  
`Windows64ReleaseDp_bulletjme.dll`  
`Windows64ReleaseSp_bulletjme.dll`  

### LWJGL
Go to this link https://github.com/LWJGL/lwjgl3/releases  
Download the file `lwjgl-3.2.3.zip`.  
Unzip it into the directory `lib`. It will make 38 subdirectories.  
 
### JOML
Go to this link https://github.com/JOML-CI/JOML/releases/tag/1.10.1  
Download these files. Store them in the directory `lib`.  
`joml-1.10.1-sources.jar`  
`joml-1.10.1.jar`  
`joml-jdk8-1.10.1.jar`  

### Setup dependencies
#### IntelliJ
Click `File` -> `Project Structure` to open a dialog. From within the `Libraries` item, add the directory `lib` as a dependency, and also each of the `lwjglXXX` subdirectories within `lib` as dependencies.

Add a `Run/Debug Configuration` of type `Application`. Specify `com.lunargravity.application.Application` as the entry point.

To build, click `Build` -> `Build Project`.

To run, click `Run` -> `Run XYZ`. Or just click the green run icon on the top right toolbar.

#### Eclipse
Click `File` -> `New` -> `New Project`. A dialog opens. I called the project `LunarGravity`, and used the location `C:\Development\159.261-Game-Prog-Assgn2`. Click `Finish`.

In the top right of Eclipse, click `Open Perspective`. Double click `Java (default)`. The dialog closes.

Click `Run` -> `Run Configurations`. Select `Java Application` from the tree, then add a new configuration. I called mine simply `Java Application`, named the project `LunarGravity`, and set the Main class to `com.lunargravity.application.Application`.

Before closing this dialog, click the `Dependencies` tab. Select `Classpath Entries`, then click `Add JARs...`. I chose all JARs from within the `lib` directory (which is the JOML and Libbulletjme JARs.) Next I added all JARs from the following directories:  
`lib\lwjgl`  
`lib\lwjgl-glfw`  
`lib\lwjgl-opengl`  

To run the game click the green and white arrow on the main toolbar with the tooltip `Run Java Application`.
