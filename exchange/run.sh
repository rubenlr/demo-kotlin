#!/bin/bash

docker-compose down

./gradlew clean build

docker-compose up --build -d