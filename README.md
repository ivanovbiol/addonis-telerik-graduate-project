# Addonis - The better add-ons registry.
Graduate team project (Java Web Application) of **Hristian Ivanov** and **Ivan Ivanov**. 

## Description

**Addonis is an Addons Registry web application similar to JetBrains Market Store (https://plugins.jetbrains.com). Allows users to download addons that are appropriate for their IDE, rate addons uploaded by others and upload their own addons.**

Database username: root   
Database password: root  

Admin username: admin    
Admin passowrd: aA!12345678  

Enabled user username: user1  
Enabled user passowrd: aA!12345678    

Disabled user username: user9  
Disabled user passowrd: aA!12345678    

Some of the public functionality of the application and possible actions for **non registered** users are:

**·** Login and register.

**·** Browse addons for their preferred IDE.

**·** Search addon by name.

**·** Sort addons by name, count of downloads, upload date and last commit date.

**·** Download addons.

Some of the possible actions for **registered** users are:

**·** First, they must first **verify their accounts via email** if they want to have rights and be able to upload addons, otherwise they will have the same privilidges as not registered users.

**·** Manage their own profiles and addons.

**·** Browse addons for their preferred IDE.

**·** Sort addons by name, count of downloads, upload date and last commit date.

**·** Download addons.

**·** Rate existing addons.

**·** Invite а friend to register.

Some of the possible actions for **admin** users are:

**·** See a list of users and search them by phone number, username or email.

**·** Block/unblock users.

**·** Approve pending addons.

**·** Delete addons.

**·** Modify addons.

## Technologies

**·** Spring
**·** SpringMVC
**·** Hibernate
**·** Thymeleaf
**·** HTML
**·** CSS
**·** JUnit
**·** MariaDB

## Swagger documentation available on the following link

- http://localhost:8080/swagger-ui.html

## Database

![scheme](/images/DB.PNG)


## Home page

![scheme](/images/HomePage.PNG)

![scheme](/images/HomePage2.PNG)


## All Addons

![scheme](/images/AllAddons.PNG)


## NOTE  

Currently the invite a frient and email profile verification (involved in the registration process) functionalities are unavailable because of Gmail's terms:

![scheme](/images/mail.PNG)

***



