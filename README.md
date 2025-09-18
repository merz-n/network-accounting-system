# Network Accounting System

A simple Java console application to manage **networks**, **devices**, and **device connections** in PostgreSQL.  
Built with Java 21, Maven, JDBC, Flyway, and JUnit 5.

---

# Features
- Manage **networks** (create, edit, delete)
- Manage **devices** inside networks (create, search, list)
- Manage **connections** between devices (create, delete)
- Search devices by name, IP, type, or status
- Reports:
  - Devices and connections in a selected network
  - All networks with their devices
  - Count of active devices per network

---

# Tech Stack
- Java 21
- Maven
- PostgreSQL + Flyway (automatic DB migrations)
- JUnit 5 (DAO tests)

---

# Database Setup
1. Start PostgreSQL locally (port 5432).  
2. Create a database and user:
```sql
CREATE DATABASE network_project;
CREATE USER admin WITH PASSWORD 'admin';
GRANT ALL PRIVILEGES ON DATABASE network_project TO admin;

---

No need to create tables manually!
On application startup, Flyway will automatically run the migration (V1__init.sql) and create all required tables.

---

Run the App -> 
Build the project: mvn clean package
Run the fat-jar (contains all dependencies: PostgreSQL driver, Flyway): java -jar target/network-accounting-system-1.0-SNAPSHOT-shaded.jar

On startup you will see:

✅ Database ready and connected!
Enter:
1 Add network
2 Add device
3 Add connection
4 Remove network
5 Remove connection
6 Edit network
10 Find device by name
11 Find device by IP
12 Find device by type
13 Find device by status
14 Show devices and connections in a selected network
15 Show all networks with devices
16 Report: active devices count per network
0 Exit


Run Tests -> mvn test

---

Notes

Database schema is maintained by Flyway (src/main/resources/db/migration/V1__init.sql).
Default DB credentials: admin / admin, DB name network_project.(Change in Main.java and Database.java if needed.)
To run with your own PostgreSQL user/password, adjust connection parameters.




