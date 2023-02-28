# Client-Server_AirlineReservationSystem
A Java project I did in my 4th semester, basically a client application that connects to a multi-thread server to operate, the server uses MYSQL database to store/fetch data.

code for database, if i remember right.

create table Account
(
Acc_ID INT NOT NULL AUTO_INCREMENT,
Primary Key (Acc_ID),
Email VARCHAR(24) NOT NULL,
Pass VARCHAR(24) NOT NULL,
FName VARCHAR(24) NOT NULL,
MName VARCHAR(24) NOT NULL,
LName VARCHAR(24) NOT NULL,
DOB Date,
Mobile_No VARCHAR(16) NOT NULL,
CreatedOn Date,
LastLogin Date,
IsActive INT
);

create table Ticket
(
Ticket_ID INT,
PRIMARY KEY(Ticket_ID),
FlightCode VARCHAR(5),
IssueDate Date,
ExpireDate Date,
Airline_Name VARCHAR(24),
Seat_No VARCHAR(6),
FromWhere VARCHAR(60),
FromCode VARCHAR(4),
ToWhere VARCHAR(60),
ToCode VARCHAR(4),
DepartureTime VARCHAR(8),
DepartureDate Date,
ReachingTime VARCHAR(8),
ReachingDate DATE,
Gate VARCHAR(3),
Terminal VARCHAR(3),
Price INT,
DiscountPrice INT,
isBought INT
);

create table Inventory
(
Inventory_ID INT,
Account_ID INT,
CONSTRAINT FK_ACCID FOREIGN KEY(Account_ID) REFERENCES Account(Acc_ID),
Ticket_ID INT,
CONSTRAINT FK_TKTID FOREIGN KEY(Ticket_ID) REFERENCES Ticket(Ticket_ID)
);
