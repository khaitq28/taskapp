#### In this project we will implement an application  (react + spring boot) with security done by Oauth2 (keycloak) and API Management (WSO2)
### To make it simple we use docker-compose  
 ✔ postgres                                                                                                                                                                                                            1.1s 
 ✔ keycloak  (for Identify Provider)                                                                                                                                                                                                            0.0s 
 ✔ wso2apim  (for API Gateway + API Management)               

### start docker-compose:

  docker-compose up -d


### Keycloak:
http://localhost:8081/admin/master/console

login with admin/admin

- Create a realms "taskapp"
- Then create a client "taskapp-frontend"  with the configs valid redirect URL   http://localhost:3000/*
- set Authentication flow : Standard flow  +  Direct access grants
- Create 2 roles "admin" "user" in this application client. 

- Create 2 users and asign roles (role for the client "taskapp-frontend")  for them (admin for the Bob one and user for Alice)
- Bob is an admin of "taskapp-frontend", Alice is a normal user of "taskapp-frontend"

Test the API with the token from Keycloak: (the same for Bob)

- Get token: 
<img width="1159" height="348" alt="image" src="https://github.com/user-attachments/assets/3d05c9c7-103a-409b-b359-3f2c63e37e78" />

### Start the taskapp springboot API
Swagger UI: 
- http://localhost:8080/taskapp/swagger-ui/index.html#/

Use the token to test endpoints. Endpoints are secured with some conditions... based on user Role (Admin or User or has right on task....)
