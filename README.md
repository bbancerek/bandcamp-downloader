1. Building jar

mvn clean package
cp src/main/resources/app.properties target/app.properties

2. Running from jar

2.1. BandcampDownloader

java -jar bandcamp-downloader-jar-with-dependecies.jar

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


2.2. AppConfig

java -cp bandcamp-downloader-jar-with-dependecies.jar bartibart.downloader.bandcamp.app.AppConfig

Usage:
  EDIT:        AppConfig -edit <property=value> <property=value>...
  DISPLAY:     AppConfig <property> <property>...
  DISPLAY ALL: AppConfig -all
Properties:
  default.directory = default directory for downloaded albums/tracks
