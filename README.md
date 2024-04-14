# Car Notificator
**Web service for sending notifications about the availability of new cars for sale.**
## Used technologies
- Java 21
- Spring Framework
  - Spring Boot
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Spring Test
- Apache Maven
- Hibernate
- PostgreSQL
- Apache Kafka
- Docker
- Flyway
- Thymeleaf
- Tailwind CSS
## Architecture schema
![](assets/images/Untitled-2024-04-05-1557.png)
## Services description
### User service
- Registers new users
- Identifies and authorize users
- Provides operations with subscriptions
- Works with email services
- Works with database
### Observer service
- Observers an availability of new cars for sale
- Collect information about new cars and send messages to users by notification service
- Form messages for users
### Notification service
- Provides Telegram bot for users interaction
- Implements connecting Telegram account with CarNotificator account
- Sends messages to users
## Screenshots
![](assets/images/photo_2024-04-10_09-38-47.jpg)
![](assets/images/photo_4_2024-04-10_12-00-08.jpg)
![](assets/images/photo_2_2024-04-10_12-00-08.jpg)
![](assets/images/photo_3_2024-04-10_12-00-08.jpg)
![](assets/images/photo_6_2024-04-10_12-00-08.jpg)
![](assets/images/photo_5_2024-04-10_12-00-08.jpg)
![](assets/images/photo_1_2024-04-10_12-00-08.jpg)
