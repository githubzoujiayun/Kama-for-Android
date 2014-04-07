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

    /* Retrieve and parse a single object */
    JsonGetter<MyObject> jsonGetter = new JsonGetter<MyObject>(MyObject.class);
    jsonGetter.setUrl(MY_URL);
    jsonGetter.setJsonTitle("my_object");
    jsonGetter.setUrlData(myUrlData);

    MyObject myObject = jsonGetter.execute();

    /* Retrieve and parse a single object */
    JsonGetter<MyObject> jsonGetter = new JsonGetter<MyObject>(MyObject.class);
    jsonGetter.setUrl(MY_URL);
    jsonGetter.setJsonTitle("my_object");
    jsonGetter.setUrlData(myUrlData);

    List<MyObject> myObject = jsonGetter.executeReturnsObjectsList();

The `Poster`, `Deleter` and `Putter` classes work similarly.

If there is no parseable response, use `Void`:

    /* Retrieve and parse a single object */
    JsonDeleter<Void> jsonDeleter = new JsonDeleter<Void>();
    jsonDeleter.setUrl(MY_URL);
    jsonDeleter.setUrlData(myUrlData);

If you forget to pass the class in the constructor, the execute methods will return `null`!

###Kama formatted json
A Kama formatted json response looks like this:

    {
        "meta": {
            "code": 200
        },
        "response": {
            "my_objects": []
        }
    }

You can use the classes `KamaGetter`, `KamaPoster`, `KamaDeleter` and `KamaPutter` to do kama requests.
These classes add the necessary headers and url parameters for each request.

If the response code is not `200`, a `HttpResponseKamaException` is thrown, including the meta data, stored in an instance of `KamaError` class.

