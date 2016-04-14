![Limbus Logo](http://i.imgur.com/dK33gNI.png)

# Limbus Core [![Build Status](https://travis-ci.org/Souler/limbus.core.svg?branch=master)](https://travis-ci.org/Souler/limbus.core) [![Release](https://jitpack.io/v/souler/limbus.core.svg)](https://jitpack.io/#souler/limbus.core) [![Coverage Status](https://coveralls.io/repos/github/Souler/limbus.core/badge.svg?branch=master)](https://coveralls.io/github/Souler/limbus.core?branch=master)

Limbus Core is library for scraping video websites abd websites that link the previous ones.

## Installation

We are not in Maven Central, but you can install us with maven thanks to [jitpack](https://jitpack.io/)!
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories> 
    <dependencies>
        <dependency>
            <groupId>com.github.souler</groupId>
            <artifactId>limbus.core</artifactId>
            <version>1.+</version>
        </dependency>
    </dependencies>
```

## Documentation
Available via [jitpack](https://jitpack.io/) at [here](https://jitpack.io/com/github/souler/limbus.core/1.0.0/javadoc/)

## How it works

### Video sites

Given a cloud video provider url that would open a flash/javascript video player if you open it
in your web browser, this classes allow you to extract the actual URL of the video that plays.

```java
try {
    String videoUri = VideoSiteScraper.getVideoURI("http://cloud-video-provider.com/qwertyasd");
    // videoUri points to the media file that plays on the browser
} catch (IOException | ScrapingException e) {
    e.printStackTrace()
    // Something went wrong :(
}
```

#### Supported sites
* FlashX via `FlashXScraper`
* Gamovideo via `GamovideoScraper`
* Nowvideo via `NowvideoScraper`
* Powvideo via `PowvideoScraper`
* StreamCloud via `StreamCloudScraper`
* Streame via `StreameScraper`
* StreaminTo via `StreaminToScraper`
* StreamPlay via `StreamPlayScraper`

You can check if any scraper stope woking [here](https://travis-ci.org/Souler/limbus.core)

### Content sites

*TODO!*
#### Supported content sites
* HDFull