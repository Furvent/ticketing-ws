## 1 application.properties in main

server.servlet.context-path=/api

server.port=8082

spring.datasource.url=jdbc:mysql://localhost/ws_ticketing?createDatabaseIfNotExist=true&serverTimezone=UTC

spring.datasource.username=**username**

spring.datasource.password=**password**


spring.jpa.hibernate.ddl-auto=create

logging.level.org.hibernate=INFO

logging.level.org.hibernate.SQL=DEBUG

logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

logging.level.org.hibernate.cache=DEBUG

logging.level.org.hibernate.stat=DEBUG

spring.jpa.show-sql=false

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect


## 2 application.properties in test

server.servlet.context-path=/api

server.port=8082

spring.datasource.url=jdbc:h2://mem:db;DB_CLOSE_DELAY=-1

spring.datasource.username=user

spring.datasource.password=user

spring.datasource.driver-class-name=org.h2.driver

spring.jpa.hibernate.ddl-auto=create-drop

spring.jpa.show-sql=false
