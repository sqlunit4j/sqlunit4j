# SQLUnit4J

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/958d317e31e4455f9b67560f24d79767)](https://app.codacy.com/app/ryaneberly/sqlunit4j?utm_source=github.com&utm_medium=referral&utm_content=sqlunit4j/sqlunit4j&utm_campaign=Badge_Grade_Dashboard)

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

### By Example
#### Basic
Script:
```
select * from table;
```
Output:
```
select * from table;
+-+-+
|X|Y|
+-+-+
|A|1|
+-+-+
```

#### Update/Delete
Script:
```
update TABLE2 set Col1='Zzz' where Col1='Aaa';
delete from TABLE2 where Col1='Bbb';
```
Output:
```
update TABLE2 set Col1='Zzz' where Col1='Aaa';
1 records updated/deleted

delete from TABLE2 where Col1='Bbb';
1 records updated/deleted
```

#### Silent Update/Delete
Script:
```
--@Silent
update TABLE2 set Col1='Zzz' where Col1='Aaa';
--@Silent
delete from TABLE2 where Col1='Bbb';
```
Output:
```
update TABLE2 set Col1='Zzz' where Col1='Aaa';

delete from TABLE2 where Col1='Bbb';
```

#### Vars and Print
Script:
```
def foo=123;
def bar=123;
print foo bar;
print 'foo:${foo} ${foo} bar:${bar}';
```
Output:
```
123 123
foo:123 123 bar:123 
```
#### WaitFor
Script:
```
--@WaitFor(maxMillis=1500)
call proc1('123');
```
Output:
```
call proc1('123');
+--+----+
|ID|NAME|
+--+----+
|0 |123 |
+--+----+
```
#### Nesting calls and implied loops
Script:
````
select * from table2;
  call simpleProc(${COL1});
````

Output:
````
select * from table2;
  call simpleProc(?);
  +--+----+
  |ID|NAME|
  +--+----+
  |0 |Aaa |
  +--+----+


  call simpleProc(?);
  +--+----+
  |ID|NAME|
  +--+----+
  |0 |Bbb |
  +--+----+
````
