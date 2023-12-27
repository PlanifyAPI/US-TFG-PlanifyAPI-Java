#!/bin/bash

PATH_PROJECT=~/dev/US-TFG-PlanifyAPI

cd $PATH_PROJECT

# Función para detener el proyecto Spring
detener_proyecto() {
    echo "Deteniendo el proyecto Spring..."
    
    # Obtener el PID del proceso del proyecto Spring
    PID=$(ps aux | grep '[s]pring-boot:run' | awk '{print $2}')
    
    # Detener el proceso si se encuentra en ejecución
    if [ -n "$PID" ]; then
        kill "$PID"
        echo "Proyecto Spring detenido."
    else
        echo "El proyecto Spring no está en ejecución."
    fi

    exit 0
}

# Capturar la señal SIGINT (Ctrl+C) y ejecutar la función detener_proyecto
trap 'detener_proyecto' SIGINT

# Ejecutar proyecto en segundo plano
./mvnw spring-boot:run &

# Esperar unos segundos a que cargue el proyecto
sleep 10

curl http://localhost:8080/api/youtube

