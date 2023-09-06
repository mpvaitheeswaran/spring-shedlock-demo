# spring-shedlock-demo

<p>
Spring Batch is a powerful Spring module who be used to run jobs.
A common problem is to manage multiple instances of a same job because Spring Batch don't have a default lock management.
There are several ways to manage this situation, but all of these do more or less the same thing:
the multiple instances share the same database who manages the synchronization between the nodes/instances.
To be more exact, when a job has to be run (maybe with a trigger of a cron), the instance check in the database if that job was already "blocked" from another instance; if so, then the instance will not run the job.

There are many libraries that solve the problem of the managing multiple instances of a job:
if you already use the Quarz scheduler in the project, then you can use this library; else, Shedlock, that do purely job locking, could to be the right choice.
</p>

## Create the USER e SHEDLOCK tables in the database only for oracle

```sql
CREATE TABLE MY_USER (
  id NUMBER(19)  PRIMARY KEY,
  name VARCHAR2(250) NOT NULL,
  surname VARCHAR2(250) NOT NULL,
  address VARCHAR2(250) DEFAULT NULL
);

CREATE SEQUENCE MY_USER_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER USER_seq_tr
 BEFORE INSERT ON MY_USER FOR EACH ROW
 WHEN (NEW.id IS NULL)
BEGIN
 SELECT MY_USER_seq.NEXTVAL INTO :NEW.id FROM DUAL;
END;
/


CREATE TABLE SHEDLOCK (
  name VARCHAR2(64),
  lock_until TIMESTAMP(3) NULL,
  locked_at TIMESTAMP(3) NULL,
  locked_by VARCHAR2(255),
  PRIMARY KEY (name)
);

```

