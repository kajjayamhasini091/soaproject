@echo off
REM Build and run script for SOA-Proj Microservices
REM Prerequisites: Java 21, Maven 3.9.x

SET JAVA_HOME=C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot
SET MAVEN_HOME=C:\Program Files\apache-maven-3.9.14-bin\apache-maven-3.9.14
SET PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo === Building all modules ===
call mvn clean install -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo BUILD FAILED
    pause
    exit /b 1
)

echo === BUILD SUCCESSFUL ===
echo.
echo Starting services in order...
echo.

echo [1/5] Starting Eureka Server on port 8761...
start "Eureka Server" cmd /c "java -jar eureka-server\target\eureka-server-1.0.0-SNAPSHOT.jar"
timeout /t 15 /nobreak

echo [2/5] Starting Auth Service on port 8081...
start "Auth Service" cmd /c "java -jar auth-service\target\auth-service-1.0.0-SNAPSHOT.jar"
timeout /t 10 /nobreak

echo [3/5] Starting Content Service on port 8082...
start "Content Service" cmd /c "java -jar content-service\target\content-service-1.0.0-SNAPSHOT.jar"
timeout /t 10 /nobreak

echo [4/5] Starting Access Service on port 8083...
start "Access Service" cmd /c "java -jar access-service\target\access-service-1.0.0-SNAPSHOT.jar"
timeout /t 10 /nobreak

echo [5/5] Starting Usage Service on port 8084...
start "Usage Service" cmd /c "java -jar usage-service\target\usage-service-1.0.0-SNAPSHOT.jar"

echo.
echo === All services started ===
echo Eureka Dashboard: http://localhost:8761
echo Auth Service:     http://localhost:8081
echo Content Service:  http://localhost:8082
echo Access Service:   http://localhost:8083
echo Usage Service:    http://localhost:8084
echo.
pause
