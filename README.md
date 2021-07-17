# Introduction 
This project use Spring boot, spring data
Database: Mysql


# Getting Started
Installation:
1.	JKD 8
2.	Maven 3.6 or above
3.	MySql server
	+ Create new mysql schema (the default schema configured in configuration file is "wegotest", port 3306). 
	+ Update credential information for mysql connection in src/main/resources/application.properties
	+ Project uses liquibase to create 2 default tables when starting server. There is no need to run script manually to create these tables.


# Build, run and Test
- Eclipse: Import maven project to eclipse then run spring boot application
- Run by command line:
	+ build project: mvn clean package install
	+ run: mvn spring-boot:run
- Once application started, the default url is "http://localhost:8088/"
- 2 endpoint (GET) to test:
	+ Load default data -> this enpoint will insert all car park information to "car_park_information" table from csv file (I populate this csv file by downloading it from website and add more lat/lon columns after converting). We need to call this api to insert default data.
		http://localhost:8088/carpark/load-data 
	+ Query endpoint:
		http://localhost:8088/carpark/nearest?latitude=1.37326&longitude=103.897&page=0&per_page=5
		http://localhost:8088/carpark/nearest?latitude=1.37326&longitude=103.897
		
		page & per_page are optional, if these params are not specified the system will set default page 1 and 10 records per page.


