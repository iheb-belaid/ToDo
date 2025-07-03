# image java
FROM openjdk:17-jdk-slim
# nom du fichier jar généré
ARG jar_FILE=target/ToDo-0.0.1-SNAPSHOT.jar
#copier le jar dans le conteneur
COPY ${jar_FILE} app.jar
# le port exposé 8081 car 8080 est utilisé par jenkins
EXPOSE 8081
# commande de lancement
ENTRYPOINT ["java", "-jar","app.jar"]
