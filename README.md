# Stock Broker Monitor

* Spring Boot
* Spring Data
* Apache Camel
* Lombock

SBM is a stand-alone appliccation whose proposes to monitoring stock market data. After all a local notification will be sended to you (only for Windows). In this proof-of-concept, there is a json file in `sbm-collector` resource folder, to exemplify the expect file.
    
See `application.properties` for general configuration

Running the application

    mvn spring-boot:run
    
Generating executable JARs

    mvn package spring-boot:repackage