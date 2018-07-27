# SQLUnit4J
##### Write SQL Unit tests in SQL (but not in your database)

SQLUnit4j is a simple framework based on JUnit and ioUnit that reads a suite .sql scripts from the file system and executed them against expected results.  

## Running SQLUnit4J with Gradle:

#### build.gradle
```
dependencies{
  compile group: 'com.github.iounit', name: 'iounit', version: '0.2.0'
  _Add the jdbc driver for your database here_
}
```
#### sqlunit4j.properties
```
driver=_driver class_
url=_jdbc url_
user=
password
```
#### Add sql scripts to the folder src/test/resources 
Use subfolders to group your scripts into suites.

#### gradle test

Runs the sql scripts as testss.  The first run will generate the expected results for you, subsequent runs will compare the output.
