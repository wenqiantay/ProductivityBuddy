FROM node:23 AS ng-build

WORKDIR /src

RUN npm i -g @angular/cli

COPY AppClient/public public
COPY AppClient/src src
COPY AppClient/*.json .

RUN npm ci && ng build

FROM openjdk:23-jdk AS j-build

WORKDIR /src

COPY App-Server/.mvn .mvn
COPY App-Server/src src
COPY App-Server/mvnw .
COPY App-Server/pom.xml .

# Copy angular files over to static
COPY --from=ng-build /src/dist/app-client/browser/ src/main/resources/static

RUN chmod a+x mvnw && ./mvnw package -Dmaven.test.skip=true

FROM openjdk:23-jdk 

WORKDIR /app

COPY --from=j-build /src/target/server-0.0.1-SNAPSHOT.jar productivityapp.jar

ENV PORT=8080

EXPOSE ${PORT}

SHELL [ "/bin/sh", "-c" ]
ENTRYPOINT SERVER_PORT=${PORT} java -jar productivityapp.jar