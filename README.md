# Hostfully - Test Review - Backend
## Booking RESTful Java API
Booking API is a simple RESTful API for managing bookings and blockings in a property. 
It provides endpoints for creating, reading, updating, deleting, cancel and rebook bookings, as well
as provide endpoints for creating, reading, updating and deleting Blockings.


## Instalation
Follow these steps to get started:

1. Clone this repo (git clone https://github.com/jaderdomonte/booking-api.git)

2. Get into the project folder that has the pom.xml file.

3. Execute the commands below:

    - mvn package
    - java -jar target/booking-api-0.0.1-SNAPSHOT.jar

Or skip steps 2 and 3 and import the project into your IDE and run it.

4. Access: http://localhost:4000/swagger-ui/index.html#/

## Tecnology
- Java 17
- Maven
- SpringBoot
- Spring Data JPA
- Spring Validation
- Spring Web
- H2 database
- Lombok
- Junit
- Mockito
 
## Features

Bookings
   - Create Booking.
   - Get a Booking by id.
   - Get Booking by filter.
   - Update Booking.
   - Delete Booking.
   - Cancel Booking.
   - Activate Booking.

Blockings
   - Create Blocking.
   - Get Blockings by filter.
   - Update Blocking.
   - Delete Blocking.




