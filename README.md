# Building jar

```
mvn clean package
cp src/main/resources/app.properties target/app.properties
```

First command will create two jars in the target directory:
* bandcamp-downloader.jar
* bandcamp-downloader-jar-with-dependencies.jar

BandcampDownloader depends on Google's gson - this dependency is contained in the second jar.

Second command is copying file with app's properties to the target directory. This file has to be in the same directory as jar file (on the same level) for everything to work correcty.

# Running from jar

## BandcampDownloader

This is the main program.

Enter
```
java -jar bandcamp-downloader-jar-with-dependecies.jar
```
...or...
```
java -cp bandcamp-downloader-jar-with-dependencies.jar bartibart.downloader.bandcamp.BandcampDownloader
```
to see help:
```
Usage:
  BandcampDownloader [-dir=<download directory>] <URL>

URL has to be one of:
  https://<artist>.bandcamp.com/album/<album>
  https://<artist>.bandcamp.com/track/<track>
  <artist>.bandcamp.com/album/<album>
  <artist>.bandcamp.com/track/<track>

Default download directory can be set via changing "default.directory" property in "app.properties" file.
Alternatively, via command:
  java -cp <jar name> bartibart.downloader.bandcamp.app.AppConfig -edit default.directory=<new value>
```

## AppConfig

This is an extra program for people who don't like to edit text files in standard text editors.

Enter
```
java -cp bandcamp-downloader-jar-with-dependecies.jar bartibart.downloader.bandcamp.app.AppConfig
```
to see help:
```
Usage:
  EDIT:        AppConfig -edit <property=value> <property=value>...
  DISPLAY:     AppConfig <property> <property>...
  DISPLAY ALL: AppConfig -all
Properties:
  default.directory = default directory for downloaded albums/tracks
```
