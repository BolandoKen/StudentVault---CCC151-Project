#!/bin/bash

# Compile all .java files with the MySQL Connector in classpath
javac -cp ".:mysql-connector-j-9.3.0.jar" *.java

# Run the GUI class with the MySQL Connector in classpath
java -cp ".:mysql-connector-j-9.3.0.jar" GUI
