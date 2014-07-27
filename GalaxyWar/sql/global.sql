USE shard_practice;

DROP TABLE IF EXISTS user2db;
DROP TABLE IF EXISTS db;
CREATE TABLE db(
	dbid TINYINT(4),
	dbname CHAR(3),
	ip CHAR(15),
	PRIMARY KEY ( dbid )
);

CREATE TABLE user2db(
	uid INT(11) AUTO_INCREMENT,
	gid TINYINT(4),
	dbid TINYINT(4),
	PRIMARY KEY ( uid ),
	FOREIGN KEY ( dbid ) REFERENCES db ( dbid )
);

DROP PROCEDURE IF EXISTS adduser;
DELIMITER $$
CREATE PROCEDURE adduser( OUT ruid INT, OUT rdbid INT, OUT rgid TINYINT )
BEGIN
START TRANSACTION;
INSERT INTO user2db VALUES ();
SET ruid = LAST_INSERT_ID();
SET rgid = ruid % 4;
SET rdbid = ruid % 2;
UPDATE user2db SET dbid = rdbid, gid = rgid WHERE uid = ruid;
COMMIT;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS selectuser;
DELIMITER $$
CREATE PROCEDURE selectuser( OUT ruid INT, OUT rdbid INT )
BEGIN
SELECT FLOOR( RAND() * MAX( uid ) + 1 ) INTO ruid FROM user2db;
SELECT dbid INTO rdbid FROM user2db WHERE uid = ruid;
END $$
DELIMITER ;

INSERT INTO db VALUES( 0, "shard1", "10.73.45.50" );
INSERT INTO db VALUES( 1, "shard2", "10.73.45.54" );

