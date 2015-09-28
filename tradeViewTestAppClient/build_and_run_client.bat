@echo off   
md build
echo "1. build directory created"
javac -d ./build -sourcepath src/main/java/ src/main/java/com/midikko/tradeviewtestapp/client/MainClassClient.java
echo "2. src compiled"
cd build
jar cfe client.jar com.midikko.tradeviewtestapp.client.MainClassClient com/*
echo "3. jar builded"
move client.jar ../client.jar
cd ..
rmdir build /s /q
echo "================="
java -jar client.jar

pause