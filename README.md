## 1 application.properties

	spring.datasource.url=jdbc:mysql://localhost/testticketting?serverTimezone=UTC
	spring.datasource.username=root
	spring.datasource.password=1234
	#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

	spring.jpa.hibernate.ddl-auto=create-drop
	spring.jpa.show-sql=true
	logging.level.org.hibernate=INFO
	logging.level.org.hibernate.SQL=DEBUG
	logging.level.org.hibernate.cache=DEBUG
	logging.level.org.hibernate.stat=DEBUG
	##logging.level.org.hibernate=ERROR   une fois en prod mettre cette syntaxe pour epurer les logs
	spring.jpa.properties.hibernate.format_sql=true
	spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect