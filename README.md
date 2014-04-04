Kama for Android
================

Setup
-----

This library includes the [Stan](https://bitbucket.org/Label305/stan-android) library.
To use this library, make the following changes:

 * To `settings.gradle`:



     include ':kama'
     include ':stan'

     project(':kama').projectDir = new File(settingsDir, 'path/to/kama/app')
     project(':stan').projectDir = new File(settingsDir, 'path/to/stan/app')
     // You can use the stan library that comes with kama, or provide your own path.

 * Your app's `build.gradle`:



     repositories {
         maven { url 'http://download.crashlytics.com/maven' }
     }

     dependencies {
         compile project(':kama')
     }

     android {
         packagingOptions {
             exclude 'META-INF/DEPENDENCIES'
             exclude 'META-INF/LICENSE.txt'
             exclude 'META-INF/NOTICE.txt'
         }
     }

Usage
-----
`Under construction`