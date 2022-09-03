FROM clojure:openjdk-17-lein-2.9.6-slim-buster as build

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app

RUN lein test
RUN lein uberjar
RUN cp /usr/src/app/target/uberjar/repository*standalone.jar /usr/local/bin/intel-repository.jar

FROM openjdk:11.0.11-jre-slim-buster

COPY --from=build /usr/local/bin/intel-repository.jar /usr/local/bin/intel-repository.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/local/bin/intel-repository.jar"]