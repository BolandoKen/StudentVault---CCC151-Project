#!/bin/bash
find . -name "*.class" -delete
javac -d bin src/*.java
echo "Compilation complete!"
