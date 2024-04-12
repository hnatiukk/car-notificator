FROM maven:latest as DEPS

WORKDIR /app
COPY dto/pom.xml dto/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY observer-service/pom.xml observer-service/pom.xml
COPY ../pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:go-offline -DexcludeArtifactIds=dto

FROM maven:latest as BUILDER

WORKDIR /app
COPY --from=DEPS /root/.m2 /root/.m2
COPY --from=DEPS /app /app
COPY dto/src dto/src
COPY user-service/src user-service/src
COPY notification-service/src notification-service/src
COPY observer-service/src observer-service/src
RUN mvn -B -e clean install -DskipTests=true