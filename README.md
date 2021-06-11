## 1 application.properties

spring.datasource.url=jdbc:mysql://localhost/ws_ticketing?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=*username*
spring.datasource.password=*password*

spring.jpa.hibernate.ddl-auto=create
logging.level.org.hibernate=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.cache=DEBUG
logging.level.org.hibernate.stat=DEBUG
spring.jpa.show-sql=false

spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect