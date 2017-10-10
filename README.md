# Java Hibernate Essentials
Java Database Access with Hibernate

Author Info
-----------
Author: Andrew Gurung <br>
URL: http://www.andrewgurung.com/

-----------

## Hibernate Explained
- Hibernate is a framework that simplifies DB access
- Data is represented as simple Java objects accessed through session managers
  - Object Relational Mapping (ORM) tool
- Hibernate has its own query language known as HQL
- Java Developers can use Java objects instead of dealing with SQL tables
  - Database changes only requires XML changes
- Download URL: http://hibernate.org/orm/releases/
  - Download the latest Hibernate stable release zip file
  - Unzip the file
  - The required Hibernate files are inside `hibernate-release-xxx.Final\lib\required`
- Netbeans
  - Check if Hibernate plugin is installed
  - Tools -> Plugins
- MySQL for OSX
  - Run `mysql-5.7.19-macos10.12-x86_64.dmg`
  - Open MySQL Preference Pane from System Preferences to start and stop MySQL
  - Add aliases to commonly used programs like mysql and mysqladmin
  ```
  alias mysql=/usr/local/mysql/bin/mysql
  alias mysqladmin=/usr/local/mysql/bin/mysqladmin
  ```
  - Easier way to invoke mysql command is to add `/usr/local/mysql/bin/` to Path
  ```
  PATH=${PATH}:/usr/local/mysql/bin
  ```
  - Check MySQL version
  ```
  mysql --version
  ```
  - IMPORTANT: Change password
  ```
  mysql -u root -p -h 127.0.0.1 -P 3306
  SET PASSWORD = PASSWORD('password');
  ```
-----------

## Hibernate Example

### Hibernate Configuration file

### Reverse engineering

### Hibernate mapping file

### Plain old Java object

-----------

## CRUD Operations
-----------

## Searches and Queries
-----------
