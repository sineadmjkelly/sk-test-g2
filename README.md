This document serves as a template for a README file for the service.
Instructions for using this template:
* Delete lines 1-7
* Delete everything below the "Getting Started with Your Template Service" header in the end of the document
* Remove all the comments in the text (starting with "<! --")
* Replace all italicized text (Note that the text is not meant to be italicized)

*Name of the G2 Service*
=======

<!-- Important!
Please spend some time filling the next part of the document to let any engineer quickly understand
what this service does and what its functions are. This is critical considering the quick growth of the service ecosystem at Toast. -->

## Description
<!-- Add 3-4 sentences describing the main idea behind the service  -->
*Your description here*

### RFC
<!-- Give a link to an RFC document describing the current service. This is a mandatory part as we need to ensure 
engineers can easily navigate to a more detailed documentation for the service. Feel free to include *any other* RFCs that 
may relate to the service and can help an engineer to understand the workflows. 
Be sure that by the time you deploy your service your RFC link is to the main branch, and not to a PR or specific commit -->
*[RFC](https://github.com/toasttab/system-docs/tree/main/<the-path>)*

**Related Documentation**

*[Related RFC](https://github.com/toasttab/system-docs/tree/main/<the-other-path>)*


## How to Develop
### Build
`./gradlew clean build --no-daemon`

### Debug
1. Add a new "Remote" run configuration in IntelliJ with its default settings (Attach to remote JVM, localhost:5005)
2. Start application with `./gradlew debug`
3. Attach IntelliJ's debugger by running the configuration in debug mode

### Run tests
`./gradlew clean test`

`./gradlew clean iTest -Pmode=test`

### Run application

#### Requirements
* developer setup complete
* running **java 8** 
* /etc/hosts setup to resolve services.eng.toasttab.com to 127.0.0.1

`./gradlew run`

It will start the service server at
```
https://services.eng.toasttab.com:15443 # CHANGEME - pick a new port for your service
```

You can use curl or [Postman](https://www.getpostman.com) to send HTTP request to the service, such as

**POST**
```
http://services.eng.toasttab.com:15443/v1/person # CHANGEME - add new examples for developers ramping up on your service
```
with the body containing the following x-www-form-urlencoded data a new person record will be created with the specified name
Key: "name"  Value: "Test Name"


**GET**
```
http://services.eng.toasttab.com:15443/v1/person/list
```
Returns all persons persisted in the database.

### Stop Application

Under the toast-services-template directory, execute

```sh
> ./gradlew kill
```

### Publish artifacts to local dev
`./gradlew publishToMavenLocal`

To see the version that is being published locally, you can run:
`./gradlew printVersion`


# Getting Started with Your Template Service 

*Instructions for after you've used the copyAndRename gradle task to generate this new repo - delete everything below 
here once your service is off the ground.*

1. Start the service using the `run` argument. 

    ```./gradlew run```

    The `run` argument is useful because it will run the server console in the foreground and show you relevant logging. 
    The `start` argument will background the logging so you won't easily see any issues.

2. In your server console you should see a message with the routes that are available in your service and that you can now request via a browser:

       INFO  2020-03-25T20:12:11.534Z mynewconfig<unknown> "main" [] i.d.j.DropwizardResourceConfig - The following paths were found for the configured resources:
       
           POST    /person (com.toasttab.service.mynewconfig.resources.PersonResource)
           GET     /person/list (com.toasttab.service.mynewconfig.resources.PersonResource)
           GET     /{v:(v\d/)?}status (com.toasttab.service.core.resources.ServiceStatusResource)

3. Test one of the requests. Take note that you need to use HTTP protocol and *NOT HTTPS*!

    ```http://services.eng.toasttab.com:15443/status```
    
    or
    
    ```http://services.eng.toasttab.com:15443/v1/person/list```

4. If your URL answers - congrats!  Your service is up and running! If not - beg for help in #devx or #scale. 
    We're friendly, I promise. 

## Java VM Options

If you are looking to pass arguments to the Java VM while working locally then you need to use the `JAVA_TOOL_OPTIONS` 
environment variable. Setting  `export JAVA_OPTS=...` is not sufficient to pass the arguments to the Java VM that is 
running the service because the values are inherited only by `gradlew` and are not passed to the JVM.

The `JAVA_TOOL_OPTIONS` local environment variable will automatically pass the values to the JVM. So you can use it to 
pass Xms or Xmx or a -javaagent value, as in:

`export JAVA_TOOL_OPTIONS="-javaagent:/Users/lmonahan/datadogapm/dd-java-agent.jar -Xms512m -Xmx1024m""`

After setting the environment variable you'll see confirmation output at your server start:

`$ ./gradlew run`

`Picked up JAVA_TOOL_OPTIONS: -javaagent:/Users/lmonahan/datadogapm/dd-java-agent.jar -Xms512m -Xmx1024m`

### Optional Configurations
#### OpenAPI Support

The template provides support for generating JSON-serializable API objects via [OpenAPI 3](https://swagger.io/specification/#version-3.0.3). 
The schema is defined in `toast-template-api/src/main/resource/toast-template-api.yaml` and objects are generated by 
the OpenAPI generator Gradle plugin.

**TODO:** Update the contact info in the API spec with your team's name (search for CHANGEME in the yaml).

To enable, uncomment the call to `generateKotlinOpenApi()` in the API module's `build.gradle` file. Generated objects 
can be found in the module's `build/generated` directory. OpenAPI can be used in addition to existing / manually-defined
 API models - provided the class definitions do not overlap.

If this feature is not desired, you can safely delete the `toast-template-api.yaml` file and remove the 
`generateKotlinOpenApi()` call completely.

### Linting

This service is set up to use ktlint using the [spotless plugin](https://github.com/diffplug/spotless/tree/main/plugin-gradle). This comes with two commands:
- `./gradlew spotlessCheck`: Check your kotlin files against the default rule set that ktlint uses (run by CI)
- `./gradlew spotlessApply`: Automatically apply any suggested changes by the linter to your files
