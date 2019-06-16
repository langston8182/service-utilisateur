FROM adoptopenjdk/openjdk11
MAINTAINER Cyril Marchive <cyril.marchive@gmail.com>
ARG JAR_FILE
#COPY ${JAR_FILE} /Users/cyril/kubernetes-docker/
ADD ${JAR_FILE} service-utilisateur.jar
EXPOSE 8100
#WORKDIR /Users/cyril/kubernetes-docker/
ENTRYPOINT ["/opt/java/openjdk/bin/java"]
CMD ["-jar", "/service-utilisateur.jar"]