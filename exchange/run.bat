@echo off

REM Finish Docker Compose services
echo Starting Docker Compose services...
docker-compose down

REM Build the project using Gradle
echo Building the project...
call .\gradlew build

REM Start Docker Compose services
echo Starting Docker Compose services...
docker-compose up -d
