#!/bin/bash
mvn spring-boot:run \
  -Dspring-boot.run.profiles=default \
  -Dspring-boot.run.jvmArguments="
    -Xdebug
    -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
    -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8005
    -Dcom.sun.management.jmxremote.authenticate=false
    -Dcom.sun.management.jmxremote.ssl=false
    -Djava.rmi.server.hostname=localhost"
