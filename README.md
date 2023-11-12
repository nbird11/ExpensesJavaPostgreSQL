![Title Banner - Expense Tracker](./titleBanner.png)

## Overview

This expense tracker termial application is the next iteration of my previous Java application
which used a CSV file to track income and expense information. Instead, this program uses a
local PostgreSQL database to store the information, although it's organized in a very similar
way.

The purpose for writing this software was so that I could better track my spending compared to my
income in order to build better financial habits and budget my money more wisely. I also wanted
to improve my other program with more common methods of storing information to practice for any
future work I might be doing.

Link to YouTube demonstration:

[Expense Tracker JDBC PostgreSQL - Demo](https://youtu.be/n8u5SfMi4Ik)

## Relational Database

The database I am using for this project is called `transactions`, and it has only one table called `records`.

The `records` table has the following fields:

```
id SERIAL PRIMARY KEY,
date DATE,
debit DECIMAL,
credit DECIMAL,
location VARCHAR(25),
account VARCHAR(25),
description VARCHAR(50)
```

## Development Environment

I used the most recent release of the Java Development Kit (JDK-21) to develop this program.

Almost exactly the same as with my previous iteration of this program, I used some Windows batch
files to run the right commands in powershell and compile all my java files (using the `javac`
command) and then run my program main (using the `java` command). The only difference with this
version is that because I needed to include the JDBC API to properly use interfaces from
`java.sql`, I had to include the executable `.jar` file in the classpath for each of the commands.

> compile.bat
>
> ```
> javac -cp "./lib/postgresql-42.6.0.jar" -d ./build [source files]
> ```

> run.bat
>
> ```
> cd ./build
> java -cp ".;../lib/postgresql-42.6.0.jar" [class file where main is]
> ```

---

Now I can just run

```
./compile; ./run
```

in the command line and it will automatically compile and run the full program.

## Useful Websites

- [PostgreSQL Official Documentation](https://www.postgresql.org/docs/current/sql.html)
- [Using PostgreSQL in Java - JDBC Tutorials](https://www.postgresqltutorial.com/postgresql-jdbc/)

## Future Work

#### TODO:

- In DBInteractor.initDB(), verify that the `records` table has the correct fields.

#### Features:

- Add option to modify specified fields of a specific record.
- Add option to remove a record from the database.
