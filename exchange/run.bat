@echo off

@echo off

REM Stop Docker Compose services
echo Stopping Docker Compose services...
docker-compose down

REM Build the project using Gradle
echo Building the project...
call .\gradlew clean build -x test

REM Check if the build was successful
if %ERRORLEVEL% NEQ 0 (
    echo Gradle build failed. Exiting.
    exit /b %ERRORLEVEL%
)

REM Start Docker Compose services
echo Starting Docker Compose services...
docker-compose up --build -d

REM Check if Docker Compose started successfully
if %ERRORLEVEL% NEQ 0 (
    echo Docker Compose failed to start. Exiting.
    exit /b %ERRORLEVEL%
)

echo Application started successfully.