#!/bin/bash

PATH_PROJECT=~/dev/US-TFG-PlanifyAPI

cd $PATH_PROJECT

# Ejecutar proyecto en segundo plano
./mvnw spring-boot:run &

# Esperar unos segundos a que cargue el proyecto
sleep 10

curl http://localhost:8080/api/youtube
