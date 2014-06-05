#Kama for Android [![Build Status](https://travis-ci.org/Label305/Kama-for-Android.svg?branch=master)](https://travis-ci.org/Label305/Kama-for-Android)

Kama for Android is a library for easy executing HTTP requests, and parsing their JSON response into Pojo's. It uses [Jackson](https://github.com/FasterXML/jackson) to map JSON to Plain Old Java Objects.

##Setup

To use this library, add the following dependency to your `build.gradle`:

```
dependencies {
       compile 'com.label305.kama-for-android:library:0.1.+'
}
```
    
##Usage

###POJO's
A Plain Old Java Object needs to be formatted according to the Jackson library as follows:
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyObject {
    
    @JsonProperty("name")
    private String name;

}
```

###Json
You can execute HTTP requests and parse their responses by using the `JsonGetter`, `JsonPoster`, `JsonDeleter` and `JsonPutter` classes. Do not forget to provide the `Class` parameter in the constructor! 

Example:
```java
/* Retrieve and parse a single object */
JsonGetter<MyObject> jsonGetter = new JsonGetter<MyObject>(MyObject.class);
jsonGetter.setUrl(MY_URL);
jsonGetter.setJsonTitle("my_object");
jsonGetter.setUrlData(myUrlData);

MyObject myObject = jsonGetter.execute();

/* Retrieve and parse a list of objects */
JsonGetter<MyObject> jsonGetter = new JsonGetter<MyObject>(MyObject.class);
jsonGetter.setUrl(MY_URL);
jsonGetter.setJsonTitle("my_object");
jsonGetter.setUrlData(myUrlData);

List<MyObject> myObject = jsonGetter.executeReturnsObjectsList();
```

The `Poster`, `Deleter` and `Putter` classes work similarly.

If there is no parseable response, use `Void`:
```java
/* Retrieve and parse a single object */
JsonDeleter<Void> jsonDeleter = new JsonDeleter<Void>();
jsonDeleter.setUrl(MY_URL);
jsonDeleter.setUrlData(myUrlData);

jsonDeleter.execute(); //returns null
```
If you forget to pass the class in the constructor, the execute methods will return `null`!

If the response code is not `200` or `202`, a `HttpResponseKamaException` is thrown, including the meta data, stored in an instance of `KamaError` class.

###Kama formatted JSON
A Kama formatted JSON response looks like this:

```json
{
    "meta": {
        "code": 200
    },
    "response": {
        "my_objects": []
    }
}
```

You can use the `KamaWrapper` class to do Kama requests. This class wraps a `AbstractJsonRequester` and adds the necessary headers and url parameters for the request. Instead of providing your `MyObject` class to the `AbstractJsonRequester`, provide `KamaObject`. Provide the `MyObject` class to the `KamaWrapper`:

```java
/* Retrieve and parse a single object */
JsonGetter<KamaObject> jsonGetter = new JsonGetter<>(KamaObject.class);
jsonGetter.setUrl(MY_URL);
jsonGetter.setJsonTitle("my_object");
jsonGetter.setUrlData(myUrlData);

KamaWrapper<MyObject> wrapper = new KamaWrapper(mContext, jsonGetter, MyObject.class);
MyObject myObject = wrapper.execute();
```

## License
Copyright 2014 Label305 B.V.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
