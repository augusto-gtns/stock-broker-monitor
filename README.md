# Home-Broker Monitor


## Spring Boot
    
See `application*.properties` for general configuration

Health check and info detail

    curl -XGET -s http://localhost:8081/actuator/health
    curl -XGET -s http://localhost:8081/actuator/info

## Apache Camel

...

--- 
### Run jar

as a service
		
	javaw -Dfile.encoding=UTF-8 -jar D:\work\gtns\repo\smart-invest\hb-monitor\target\hb-monitor-1.jar

changing windows encoding
	
	chcp 65001 && java -Dfile.encoding=UTF-8 -jar D:\work\gtns\repo\smart-invest\hb-monitor\target\hb-monitor-1.jar
	
	

### Maven

Run application

    mvn spring-boot:run [-Pprd]

Generate executable JARs

    mvn package spring-boot:repackage [-Pprd]