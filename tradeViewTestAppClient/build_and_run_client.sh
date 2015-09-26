#!/bin/bash          
shopt -s globstar
mkdir build
echo "1. build directory created"
javac -d ./build src/**/*.java
echo "2. src compiled"
cd build
jar cfe client.jar com.midikko.tradeviewtestapp.client.MainClassClient com/*
echo "3. jar builded"
mv client.jar ../client.jar
cd ..
rm -rf build
echo "================="
java -jar client.jar