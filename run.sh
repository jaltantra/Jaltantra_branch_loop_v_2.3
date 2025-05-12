#!/bin/bash

# Build and run Spring Boot application
./mvnw clean
./mvnw package

# Run Spring Boot app in background
java -jar target/JaltantraLoopSB-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev > /dev/null 2>&1 &

# Run Python script in background
python3 /path/to/your_python_script.py > /dev/null 2>&1 &

# Optional: Wait or log to confirm
echo "Spring Boot and Python applications started in background."