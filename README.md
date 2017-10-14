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
- Option 1: Adding Hibernate library to your eclipse project
  - [Tutorial Link](https://kaanmutlu.wordpress.com/2011/07/30/hibernate-installationsetup-on-eclipse-ide/)
  - Also add MySQL Connector jar file
- Option 2: Adding Hibernate through Maven
```
<dependencies>
  <!-- MySQL connector -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.5</version>
  </dependency>
  <!-- Hibernate 5.2.6 Final -->
  <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.2.6.Final</version>
  </dependency>
</dependencies>
```
-----------

## Hibernate Example
- [Source Code](hibernate-tutorial)

### Eclipse Plugin: Hibernate tool
  - Hibernate Tools built by JBoss
  - Change Show Perspective -> Hibernate

  1. Run Hibernate Console Configuration
  2. Select Configuration File
    - Run new Hibernate Configuration File
  3. Run Hibernate Reverse Engineering Configuration
  4. Create Hibernate Code Generation Configuration

### Hibernate Console Configuration
- Right click project -> New -> Hibernate -> Create Hibernate Console Configuration
- Configuration File: Click setup -> Create new -> hibernate.cfg.xml
-

### Hibernate Configuration file
- Filename: hibernate.cfg.xml
- Define required database properties
- To create 'hibernate.cfg.xml' file in Eclipse, install JBoss Hibernate Tool for wizard support
  - Connection URL: `jdbc:mysql://localhost:3306/messagerepository`
  - Driver class: com.mysql.jdbc.Driver
  - Set Username and Password
- Additional hibernate properties from the Hibernate GUI page (provided by JBoss Hibernate tool)
  - Dialect: org.hibernate.dialect.MySQLDialect
  - Show SQL: true
  - hibernate.current_session_context_class: thread
  - hibernate.query.factory_class: `org.hibernate.hql.internal.classic.ClassicQueryTranslatorFactory`
  - Note: MySQL 5.7.19: `jdbc:mysql://localhost:3306/hibernateexample`
  - Append these extra params to fix db connection errors and support Reverse Engineering Tool `?useSSL=false&amp;serverTimezone=UTC&amp;nullNamePatternMatchesAll=true`

### Reverse engineering
- Process of taking database tables and generating mapping files and POJO files
- Right click project -> Create hibernate reverse engineering file
- Filename: hibernate.reveng.xml
- Select table used for reverse engineering
- <schema-selection>: Identifies schema
- <table-filter>: Identifies tables to reverse engineer
- <type-mapping>: Convert types
```
<hibernate-reverse-engineering>
	<schema-selection match-catalog="databasename" />
	<table-filter match-name="tablename" />
</hibernate-reverse-engineering>
```

### Hibernate mapping file and POJOs
- Depends on `hibernate.reveng.xml` to generate mapping `*.hbm.xml` and POJO files
- Click on the tiny hibernate specific run icon in the toolbar to select 'Hibernate Code Generation Configuration' (Similar to Run As.. icon)
- Choose console configuration and reveng.xml files
- Package: helloworldexample
- In the Exporters tab, check the following
  - Use Java 5 syntax
  - Domain code (.java)
  - Hibernate XML Mappings (.hbm.xml)
- Run
- Then add the following line in hibernate.cfg.xml
  ```
  <mapping resource="helloworldexample/Message.hbm.xml"/>
  ```

#### Generated mapping and POJO files
Message.hbm.xml
```
<hibernate-mapping ...>
    <class catalog="messagerepository" name="helloworldexample.Message" table="message" ...>
        <id name="id" type="java.lang.Short">
            <column name="id"/>
            <generator class="identity"/>
        </id>
        <property name="message" type="string">
            <column length="50" name="message"/>
        </property>
    </class>
</hibernate-mapping>
```

Message.java (POJO)
- Default constructor
- private variables for columns
- Getters and Setters
-----------

## Java Application
- Define factory and registry
```
private static SessionFactory sessionFactory;
private static StandardServiceRegistry registry;
```

- Establish registry
```
registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();
```

- Create Metadata
```
MetadataSources sources = new MetadataSources(registry);
Metadata metadata = sources.getMetadataBuilder().build();
```

- Instantiate factory object
```
sessionFactory = metadata.getSessionFactoryBuilder().build();
```

- Open session
```
Session session = sessionFactory.openSession();
```

- Create transaction object
```
Transaction tx = null;
tx = session.beginTransaction();
```

- Persisting into database through POJOs
```
Message msg = new Message(m);
msgId = (Short) session.save(msg);
```

- Retrieving from database
```
List messages = session.createQuery("From Message").list();
for(Iterator iterator = messages.iterator(); iterator.hasNext(); ) {
	Message message = (Message) iterator.next();
	System.out.println("Message: " + message.getMessage());
}
```

- Finally commit transaction, close session and registry
```
tx.commit();

session.close();

StandardServiceRegistryBuilder.destroy(registry);
```
-----------

## Terminologies
### Data Persistence
- Only Java objects with variables can be stored
- Objects can be in 3 states of persistence
  1. Transient State:
    - Exist in memory. Database has no knowledge of object
    - Must ask session to save it
  2. Persistent State:
    - Saved objects that exists in database
  3. Detached State:
    - Object exists in database but connection is lost
    - Eg: Session maybe closed

### Session Factory
- Each application should have one `SessionFactory` instance
- `SessionFactory` is created during startup and is thread safe
- Separate `SessionFactory` object is required for multiple database connection

### Session
- Only open session when needed and destroy it immediately as it isn't thread safe
- Session objects wraps underlying JDBC resource making it easy to connect
- Methods include: save, persist, create, read, openSession

### Transaction
- Single unit of work
- Can be started, committed or rolled back
- Handled by transaction manager

### Mapping Strategy
- Class refers to tables. Class must be POJO with getters, setters and default constructor
- Instance of a class refers to a row
- Properties of a class refers to columns
- Hibernate uses configuration file for mapping data
- Filename: 'className'.hbm.xml

Options to identify mapping:
1. Configuration XML file
2. Annotations (after Java 5)

Sample hbm.xml
- Root: <hibernate-mapping>
- All class elements are included in root element
- Both class and table name are included
- <id>: element maps the unique ID attribute
- <generator>: Used to auto-generate primary keys
- <property>: Used to map class property to a column

-----------

## CRUD Operations

### Sample Project
- Employee: id(primary key), first_Name, last_Name, salary
- Phone: id(primary key), phone_Number, employee_id (foreign key)
- New rows are added, updated or deleted to database through Java objects/instances

### Add objects

### Update objects

### Delete objects
-----------

## Searches and Queries

### Hibernate Query language (HQL)

### Select clause

### Named parameters

### Aggregate methods

### Criteria query API

-----------
