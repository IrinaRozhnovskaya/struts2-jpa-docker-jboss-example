# struts2-jpa-docker-jboss-example

This repository contains simple Struts2 example (including Docker, JBoss EAP, JPA, PostgreSQL)

_Struts2_

Convention Plugin
https://struts.apache.org/plugins/convention/

pom.xml
~~~~
<dependency>
   <groupId>org.apache.struts</groupId>
   <artifactId>struts2-convention-plugin</artifactId>
   <version>${struts.version}</version>
</dependency>
~~~~
Convention plugin assumes that all of the results are stored in WEB-INF/content (WEB-INF/content/index.jsp). 

Convention plugin finds packages named struts, struts2, action or actions.

Classes implement com.opensymphony.xwork2.Action or their name ends with Action. 

Action classes will be configured to respond to specific URLs, based on the package name that the class is defined in and the class name itself:
 com.github.actions.IndexAction -> /
 com.example.actions.products.DisplayAction-> /products
 com.example.struts.company.details.ShowCompanyDetailsAction -> /company/details
 
CDI Plugin

pom.xml
~~~~
<dependency>
   <groupId>org.apache.struts</groupId>
   <artifactId>struts2-cdi-plugin</artifactId>
   <version>${struts.version}</version>
</dependency>
~~~~
 Inevitable requirement to provide at least an empty beans.xml in WEB-INF
 
 beans.xml
~~~~
<?xml version="1.0" encoding="UTF-8"?>
<beans
   xmlns="http://xmlns.jcp.org/xml/ns/javaee"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
                      http://xmlns.jcp.org/xml/ns/javaee/beans_1_1.xsd"
   bean-discovery-mode="all">
</beans>
~~~~
Use @Inject annotation from javax.inject.Inject with your Struts 2 CDI plugin and CDI integration in general!

_JBoss datasource configuration_

jboss-eap-6.4\standalone\configuration\standalone.xml
~~~~
<datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" jta="true" use-java-context="true" use-ccm="false">
     <connection-url>jdbc:postgresql://${env.POSTGRES_HOST:127.0.0.1}:${env.POSTGRES_PORT:5432}/${env.POSTGRES_DB:db}</connection-url>
        <driver>postgresql</driver>
         <security>
           <user-name>${env.POSTGRES_USER:sa}</user-name>
           <password>${env.POSTGRES_PASSWORD:sa}</password>
         </security>
 </datasource>
 <drivers>
    <driver name="postgresql" module="org.postgresql">
      <xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
    </drive>
 </drivers>
~~~~
Put module.xml and postgresql-9.4-1206-jdbc4.jar in jboss-eap-6.4\modules\org\postgresql\main

module.xml
~~~~
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-9.4-1206-jdbc4.jar"/>
  </resources>
  <dependencies>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
    <module name="javax.servlet.api" optional="true"/>
  </dependencies>
</module>
~~~~
src\main\resources\META-INF\persistence.xml
~~~~
<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
~~~~

JBoss in docker configuration

Dockerfile
~~~~
FROM daggerok/jboss-eap-6.4:6.4.21-alpine
RUN echo "JAVA_OPTS=\"\$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005\"" >> ${JBOSS_HOME}/bin/standalone.conf
EXPOSE 5005
HEALTHCHECK --timeout=1s --retries=66 \
        CMD wget -q --spider http://127.0.0.1:8080/struts2-jpa-docker-jboss-example/health \
         || exit 1
ADD --chown=jboss ./jboss-eap-6.4/modules/org/postgresql ${JBOSS_HOME}/modules/org/postgresql
ADD --chown=jboss ./jboss-eap-6.4/standalone/configuration/standalone.xml ${JBOSS_HOME}/standalone/configuration/standalone.xml
ADD --chown=jboss ./target/*.war ${JBOSS_HOME}/standalone/deployments/struts2-jpa-docker-jboss-example.war
~~~~
docker-compose.yaml
~~~~
version: '2.1'
services:
  postgres:
    image: healthcheck/postgres:alpine
    restart: unless-stopped
    ports: ["5432:5432"]
    environment:
      POSTGRES_DB: db
      POSTGRES_USER: sa
      POSTGRES_PASSWORD: sa
    networks:
      struts2-jpa-docker-jboss-example.com:
        aliases:
          - db.struts2-jpa-docker-jboss-example.com
  jboss:
    depends_on:
      postgres:
        condition: service_healthy
    build: .
    restart: unless-stopped
    ports:
      - '5005:5005'
      - '8080:8080'
      - '9990:9990'
    environment:
      POSTGRES_DB: db
      POSTGRES_USER: sa
      POSTGRES_PASSWORD: sa
      POSTGRES_HOST: db.struts2-jpa-docker-jboss-example.com
    networks:
      struts2-jpa-docker-jboss-example.com:
        aliases:
          - struts2.struts2-jpa-docker-jboss-example.com
          - jboss.struts2-jpa-docker-jboss-example.com
networks:
  struts2-jpa-docker-jboss-example.com:
    driver: bridge
~~~~
_JPA configuration_

persistence.xml
~~~~
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0"
             xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">
  <persistence-unit name="" transaction-type="JTA">
    <jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>
    <properties>
      <property name="hibernate.hbm2ddl.auto" value="update"/>
      <property name="hibernate.show_sql" value="true"/>
      <property name="hibernate.format_sql" value="false"/>
    </properties>
  </persistence-unit>
</persistence>
~~~~

build and run using docker-compose-maven-plugin (see pom.xml)

./mvnw ; ./mvnw -Pdocker docker-compose:up

open http://127.0.0.1:8080/struts2-jpa-docker-jboss-example/

./mvnw -Pdocker docker-compose:down
~~~~


