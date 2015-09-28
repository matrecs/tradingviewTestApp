@echo off    
md build
echo "1. build directory created"
javac -d ./build -sourcepath src/main/java/ src/main/java/com/midikko/tradeviewtestapp/server/MainClassServer.java
echo "2. src compiled"
cd build
jar cfe server.jar com.midikko.tradeviewtestapp.server.MainClassServer com/*
echo "3. jar builded"
move server.jar ../server.jar
cd ..
rmdir build /s /q
echo "================="
java -jar server.jar

pause