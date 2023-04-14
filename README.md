# dbGUI
Project for C195, Software II: Advanced Java Concepts

Schedulizer

This project represents a comprehensive performance evaluation for the advanced Java concepts course, C195 Software II, at WGU.
It showcases a robust Java application that empowers users to efficiently manage customer appointments and information within a database.

Stephan Haloftis
shaloftis@wgu.edu
2/7/2023

JDK: 11.0.10
JRE: 11.0.10
JavaFX: 11.0.2
mysql-connector-java-8.0.23.jar

This project was created using IntelliJ IDEA 2020.3.3 (Ultimate Edition) and SceneBuilder 11.0.2.

The project boasts several key features, including a view of appointments, the ability to create, edit, and delete customers, and a new user feature on the login screen.
The latter was implemented as a result of the author's independent exploration of the project's requirements and functionality.

The model package contains essential classes such as Appointments, Contacts, Country, Customers, Division, and Users, each of which contains general setters and getters, as well as class constructors.

The dataAccess package was established next and features a SQL query library known as QueryChronicles, which contains all SQL queries used throughout the project, along with other database methods.
The author tackled the project through trial and error, leading to a consolidation of redundant code.

The exceptions package features the GateKeeper and Siren classes.
The Siren class, an alert library, was created to provide a clean and concise method of handling alerts. T
he GateKeeper class handles input validation through the use of boolean binding, monitoring all GUI inputs to ensure only valid data is submitted.
I was going to include Regex validation, but I decided to keep it simple. There seems to be a lot of debate about how well Regex works with address validation.
I only used it for names and phone numbers.

The merlin package houses the HotTubTimeMachine class, which handles all time manipulation within the project.
The numericNexus package contains the NumberGenie class, which generates random IDs through looping methods.
Finally, the theAgency package features the AgentFord class, which manages monitoring activities, user logs, and exception logs.

My I decided to create a visual aid for the additional report of my choice.
The report displays the number of customers per month, for the entire year.

There are also lambda expressions littered throughout the project, which I used to practice my functional programming skills.
There are much, MUCH more than two. I found them very useful for many aspects of the project.
