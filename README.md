# nda-chat
This web application allows you to create chats (using Pusher's Presence Channels) with the functionality to request e-signed Non-Disclosure Agreements (NDAs) to its members using the HelloSign API.

The stack is the following:

- Java 7 or higher
- Maven as the build manager
- Spring Boot as the server-side framework
- H2 as in-memory database
- Thymeleaf as the server-side template engine
- jQuery and Handlebars for the client-side interaction

You can follow the [tutorial](http://tutorials.pluralsight.com/interesting-apis/requesting-e-signatures-in-a-chat-with-pusher-hellosign-and-spring-boot) to build this application or jump straight to the code.

# Requirements

- [A Pusher account](https://pusher.com/)
- [A HelloSign account](https://www.hellosign.com/)
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) is preferred, but the app will work with Java 7 also.
- [Maven](https://maven.apache.org/download.cgi)
- [Ngrok](https://ngrok.com/) (optionally, if you want to expose your local server to the Internet)

# Installation
1. Clone this repository and `cd` into it.
2. Edit the `config.js` file to enter your Fanout realm information.
3. Configure a Pusher Webhook for listening to Channel existence and Presence events, both with `http://<SERVER:PORT>/pusher/webhook` as the URL (if your trying the app locally, use Ngrok to get a public URL).
4. Configure a HelloSign template with:
    - A sign field with `Consultant` as the role
    - A custom field with the label `name` 
5. Configure a HelloSign Webhook with `http://<SERVER:PORT>/hellosign/webhook` as the URL (if your trying the app locally, use Ngrok to get a public URL).
6. Start the application with one of the following commands, just replace `XXX` with your Pusher and HelloSign keys (you can also configure the env variables in your IDE):

    ```
    mvn spring-boot:run -Dspring.application.json='{"pusher":{"appId":"XXX", "key":"XXX", "secret":"XXX"},"hellosign":{"apikey":"XXX", "templateId":"XXX", "testMode":true}}'
    ```
    
    Or
    
    ```
    mvn package -DskipTests
    java -jar target/nda-chat-0.0.1-SNAPSHOT.jar --spring.application.json='{"pusher":{"appId":"XXX", "key":"XXX", "secret":"XXX"},"hellosign":{"apikey":"XXX", "templateId":"XXX", "testMode":true}}'
    ```
    
7. Go to `http://localhost:8080` (or `http://<NGROK_URL>:8080` or whatever your URL is) and create a chat.
8. In another browser, join the chat created in the previous step and start playing with the app.
9. You can view the state of the database at any time with the [H2 web console](http://localhost:8080/h2-console) using the default URL `jdbc:h2:mem:testdb`, and the user `sa` with no password.
 

# License
MIT