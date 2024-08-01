#!/bin/sh
./mvnw clean package
docker-compose up --build