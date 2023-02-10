# Energybox Backend Coding Challenge

Please complete the assignment detailed below email me back with your code either zipped as an attachment or a link to where you're working on the code ( ie GitHub, bitbucket, gitlab etc. ) 
Please also suggest a day when we can spend 45 minutes to an 1 hour reviewing your work. As we might want to include colleagues from other timezones, the time for the interview will be around 8pm. 

If you have any questions/concerns feel free to reach out to me via email ( marcus.hunger@energybox.com ). 
I will try to reply as quickly as possible.

### Tech to use:

- Java
- Spring Boot
- Spring Data
- Maven
- Neo4J (Use of an object graph mapper is completely fine and encouraged)

### Getting Started

You'll need to set up a local Neo4J instance. A docker-compose file is included in the sample project for convenience.

Don't feel obligated to spend any more than 2-5 hours on this. You're welcome to spend more time if you're having fun, but you won't be judged on how much you complete.

Project POM file is purposefully bare. Feel free to add dependencies of your choosing. 

---

Our company is rolling out a new backend service that will allow developers to create Sensors and Gateways in our system. 
A sensor can only be connected to one Gateway at a time, while a Gateway can have N sensors connected to it.

A relationship of type CONNECTED_TO should be created between sensor and gateway when a sensor is assigned to a given gateway.

Sensors should have a type attribute. And a sensor can have multiple types assigned to it. 

Some example of sensor types could be temperature, humidity, electricity.

Try to complete the user stories below as best you can, and feel free to add in any features you'd like to see that may not be detailed here. The stories are purposefully vague and open to interpretation.

### User Stories:

- As a user I'd like to query for all the existing sensors.

- As a user I'd like to query for all gateways

- As a user I'd like to query all sensors assigned to an existing gateway

- As a user I'd like to create a new sensor

- As a user I'd like to create a new gateway

- As a user I'd like to assign a sensor to a given gateway.

- As a user I'd like to query for sensors of a certain type.

- As a user I'd like to query for a gateway that has electrical sensors assigned to it.

Bonus:

- Sensors can have a last_reading for each sensor type. The last_reading consists of a timestamp and a value.

- As a user I'd like to query all of the existing last_readings of a given sensor.

### Solution Instruction & API usage

1. Checkout the project
2. Please make sure neo4j service already running as configured by `docker-compose.yml`
   Note: ( In case of neo4j connection port,username, password change , please change the configuration at `application.yml`)
3. Build `mvn clean install` & run the springboot application `java -jar target/backend-coding-challenge-0.0.1-SNAPSHOT.war`
4. User stories solution :
   - As a user I'd like to create a new sensor

   `POST` request `http://localhost:8082/api/v1/sensors/create` with example payload `{"type":"electricity"}`

   - As a user I'd like to create a new gateway

   `POST` request `http://localhost:8082/api/v1/gateways/create` with example payload `{"name":"Gateway1"}`

   - As a user I'd like to query for all gateways

   `GET` request `http://localhost:8082/api/v1/gateways/get/all`

   - As a user I'd like to query for all the existing sensors.

   `GET` request `http://localhost:8082/api/v1/sensors/get/allsensors`

   - As a user I'd like to query all sensors assigned to an existing gateway

   `GET` request `http://localhost:8082/api/v1/gateways/get/allsensorwithgateway`

   - As a user I'd like to query for sensors of a certain type.

   `GET` request `http://localhost:8082/api/v1/sensors/get/{type}` (type : ELECTRICITY, HUMIDITY, TEMPERATURE)
   
    Example : `http://localhost:8082/api/v1/sensors/get/ELECTRICITY`

   - As a user I'd like to assign a sensor to a given gateway.

   `POST` request `http://localhost:8082/api/v1/sensors/createrelation/{ exiting sensor type }/{exisitng gateway name}`

    Example: `http://localhost:8082/api/v1/sensors/createrelation/ELECTRICITY/Gateway1`

   - As a user I'd like to query for a gateway that has electrical sensors assigned to it.

   `GET` request `http://localhost:8082/api/v1/sensors/get/ELECTRICITY`

   - As a user I'd like to query all of the existing last_readings of a given sensor.

   `GET` request `http://localhost:8082/api/v1/sensors/get/all`
