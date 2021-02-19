CREATE DATABASE IF NOT EXISTS dev;
USE dev;
CREATE TABLE IF NOT EXISTS service (
	    name VARCHAR(30),
       	url VARCHAR(200),
       	username VARCHAR(30),
        id INT NOT NULL AUTO_INCREMENT,
        created DATETIME DEFAULT NOW(),
        updated DATETIME DEFAULT NOW(),
        status INT DEFAULT 0,
        PRIMARY KEY (id)
);

-- Users should be in a separate table,and services should only be stored once

INSERT INTO service (name,url, username)
VALUES ('Sam','www.samhamra.com', 'sam');

INSERT INTO service (name,url, username)
VALUES ('Kry','www.kry.se', 'kry');

DELIMITER ;;
CREATE TRIGGER before_update BEFORE UPDATE ON `service`
FOR EACH ROW
BEGIN
SET NEW.updated = NOW();
END;;
DELIMITER ;

