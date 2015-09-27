#!/bin/bash          
mkdir build
echo "1. build directory created"
javac -d ./build -sourcepath src/main/java/ src/main/java/com/midikko/tradeviewtestapp/server/MainClassServer.java
echo "2. src compiled"
cd build
jar cfe server.jar com.midikko.tradeviewtestapp.server.MainClassServer com/*
echo "3. jar builded"
mv server.jar ../server.jar
cd ..
rm -rf build
echo "================="
java -jar server.jar

$SHELL