This project is for the performance assessment for C195 Software II: Advanced Java Concepts at WGU.

The project is a Java application that allows the user to create, edit, and delete customers and appointments in a database.
The application also allows the user to view a calendar of appointments for the current month and week.
There are other notable features which were required for the project. As well as some that weren't required but I added for fun.

I incorporated a New User feature on the login screen. Mainly because I figured a lot of this project out on my own, and didn't really use much of the student repo.
I started the project off with the helper package, which establish the JDBC Connection functions which were part of a class video.
After that I was off to a good start on my own.

The following classes can be found in the model package:
Appointments, Contacts, Country, Customers, Division, Users.

I initially start the project off with this package, as it has some general setter and getter methods that I use throughout the project.
Class constructors can also be found here.

After warming up to the project requirements, and general functionality of the project, I moved onto building another package:
dataAccess.
This package started off with a SQL Query library, named QueryChronicles. This class contains all of the SQL queries that I use throughout the project.
A lot of the big lifting database methods can be found in QueryChronicles, although some other classes in the package also have database methods.
This came about simply because of how I tackled the project altogether. Trial and error.

There are many classes which I rebuilt, restructured, or completely scrapped. I had a lot of fun with this project, and I learned a lot.

After building the dataAccess package and the models package, I realized there was some redundancy in the code.
I kept both packages around, regardless. After really getting into it, I started to realize how many different ways there were to handle different situations.

The exceptions package contains the GateKeeper and Siren classes.
The Siren class is an Alert Library that I built to handle all of the alerts in the project.
I did this to make the code a little cleaner and more readale without being so repetitive and redundant.

The GateKeeper class handles all of the input validation for the project.
It has several methods that incorporate lambda expressions to perform input validation, but that didn't last long.
I decided to use boolean binding to handle input validation. I created two boolean binding methods in the CNS which monitor all of the inputs in the GUI and only allow a user to submit
data if all of the inputs are valid. I incoporated this same idea into Software I, and I really thought it to be more effective than repetitive lambda expressions.

The merlin package contains the HotTubTimeMachine class.
The HotTubTimeMachine handles all of the time manipulation methods for the project.

The numericNexus package contains the NumberGenie class.
The NumberGenie handles all of the random ID generation methods.
All of the methods are essentially the same, but they loop through different ID's.

The theAgency package contains the AgentFord class.
The AgentFord class handles all of the methods that deal with monitoring using activity, user logs, exception logs, etc.