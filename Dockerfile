FROM docker.rp-core.com/hub/maven:3.6.1-amazoncorretto-11 as builder
WORKDIR /app/gatekeeper-sparrow-server
COPY ./src ./src
COPY ./pom.xml ./pom.xml
COPY ./checkstyle.xml ./checkstyle.xml
ARG GIT_BRANCH=n/a
ARG GIT_COMMIT=n/a
ARG IMAGE_NAME=n/a
ARG IMAGE_TAG=n/a
RUN mvn package -U \
    -Dci.repository.branch=$GIT_BRANCH \
    -Dci.repository.revision=$GIT_COMMIT \
    -Dci.docker.image=$IMAGE_NAME \
    -Dci.docker.tag=$IMAGE_TAG

FROM docker.rp-core.com/devops/docker_centos_base:2_7.4 as installer
RUN yum install -y java-11-amazon-corretto-devel
RUN yum clean all
COPY --from=builder /app/gatekeeper-sparrow-server/target/gatekeeper-sparrow-server.jar /app/gatekeeper-sparrow-server/gatekeeper-sparrow-server.jar

EXPOSE 8005

CMD [ \
    "java", \
	"-Dcom.sun.management.jmxremote", \
	"-Dcom.sun.management.jmxremote.port=8005", \
	"-Dcom.sun.management.jmxremote.authenticate=false", \
	"-Dcom.sun.management.jmxremote.ssl=false", \
	"-Dspring.profiles.active=default", \
	"-XX:+UseParallelGC", \
	"-jar /app/gatekeeper-sparrow-server/gatekeeper-sparrow-server.jar"]
