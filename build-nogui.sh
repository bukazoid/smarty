#!/bin/sh
mvn clean package
docker-compose --file docker-compose-nogui.yml build

echo "run 'docker-compose up' to start the application"