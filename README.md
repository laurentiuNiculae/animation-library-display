# Animation lib display

This project implements display to screen capabilities to my [animation library](https://github.com/laurentiuNiculae/animation-library)

## External Dependecies:

I have used:
* Java 23
* [Raylib](https://github.com/CreedVI/Raylib-J/releases/tag/v0.5.2) 
* [Processing](https://processing.org/download)

Example installing processing jar:

```bash
$ mvn install:install-file -Dfile=./core/library/core.jar -DgroupId=org.processing -DartifactId=core -Dversion=4.0.0 -Dpackaging=jar
```

Same for the raylib jar.

## Build

Build a jar

```bash
$ mvn package
```

Then run it 

```bash
$ java -jar ./target/animation-renderer-lib-2.0.0.jar
```

