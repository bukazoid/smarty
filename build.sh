#!/bin/sh
mvn clean package
docker-compose build

echo "run 'docker-compose up' to start the application"