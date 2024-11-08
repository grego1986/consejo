# Usa una imagen base de Maven con Eclipse Temurin JDK 21 para la construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia el pom.xml y el código fuente
COPY pom.xml .
COPY src ./src

# Ejecuta Maven para compilar el proyecto
RUN mvn clean package -DskipTests

# Usa una imagen base de OpenJDK 21 para ejecutar la aplicación
FROM eclipse-temurin:21-jdk-jammy

# Copia el archivo .jar generado en la fase de construcción
COPY --from=build /app/target/consejo-0.0.1-SNAPSHOT.jar concejo.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/concejo.jar"]
