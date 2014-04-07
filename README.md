#Kama for Android

##Setup

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
                exclude 'META-INF/LICENSE.txt'
                exclude 'META-INF/LICENSE'
                exclude 'META-INF/NOTICE.txt'
                exclude 'META-INF/NOTICE'
                exclude 'LICENSE.txt'
            }
        }

##Usage

###Json
You can execute HTTP requests and parse their responses by using the `JsonGetter`, `JsonPoster`, `JsonDeleter` and `JsonPutter` classes.

Example:

    JsonGetter jsonGetter = new JsonGetter();
    jsonGetter.setUrl(MY_URL);
    jsonGetter.setReturnTypeClass(MyObject.class);
    jsonGetter.setJsonTitle("my_object");
    jsonGetter.setUrlData(myUrlData);




###Kama formatted json
A Kama formatted json response looks like this:

    {
        "meta": {
            "code": 200
        },
        "response": {
            "deals": []
        }
    }

You can use the classes `KamaGetter`, `KamaPoster`, `KamaDeleter` and `KamaPutter` to do kama requests.

