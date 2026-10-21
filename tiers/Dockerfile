FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN mvn -B -q package

FROM quay.io/wildfly/wildfly:37.0.1.Final-jdk17
COPY docker/wildfly/configurar.cli /tmp/configurar.cli
RUN /opt/jboss/wildfly/bin/jboss-cli.sh --file=/tmp/configurar.cli \
 && rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history /opt/jboss/wildfly/standalone/data /opt/jboss/wildfly/standalone/log
COPY --from=build /build/dr-ear/target/disaster-management.ear /opt/jboss/wildfly/standalone/deployments/disaster-management.ear
EXPOSE 8080
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-full.xml", "-b", "0.0.0.0"]
