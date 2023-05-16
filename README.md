### Project structure

- `src/main/java` - contains the source code
- `src/test/java` - contains tests

### Run the project

Make sure you have got Java >= 18 and maven installed on your machine. <br/>
First you need to clean and build the project, run the command:

```aidl
mvn clean install
```

This will run tests and generate a jar file with all the dependencies. Jar file will be located in `target` folder.
For running the jar, run the command from project root for example with the following arguments:

```
java -jar target/cron-1.0-SNAPSHOT-jar-with-dependencies.jar "1,5-9 5 1/15 * * /usr/bin/command"
```

Alternatively, you can run the `main` method in `App.java` in your chosen IDE, e.g. `IntelliJ`
Project is using lombock library, so you might need to install the plugin for your IDE and enable annotation processing.