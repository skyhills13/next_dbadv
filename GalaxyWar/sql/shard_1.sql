USE shard_practice;

DROP TABLE IF EXISTS slog;
DROP TABLE IF EXISTS ship;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS galaxy;

CREATE TABLE galaxy(
	gid TINYINT(4),
	name VARCHAR(15),
	hp INT(11),
	PRIMARY KEY ( gid )
);

CREATE TABLE user(
	uid INT(11),
	gid TINYINT(4),
	PRIMARY KEY ( uid, gid ),
	FOREIGN KEY ( gid ) REFERENCES galaxy ( gid )
);

CREATE TABLE ship(
	sid INT(11) AUTO_INCREMENT,
	uid INT(11),
	gid TINYINT(4),
	atk INT(11),
	PRIMARY KEY ( sid ),
	FOREIGN KEY ( uid, gid ) REFERENCES user ( uid, gid )
);

CREATE TABLE slog(
	logid INT(11) AUTO_INCREMENT,
	uid INT(11),
	log VARCHAR(64),
	log_time TIMESTAMP NOT NULL,
	PRIMARY KEY ( logid ),
	FOREIGN KEY ( uid ) REFERENCES user( uid )
);

INSERT INTO galaxy VALUES( 1, "galaxy2", 100000 );
INSERT INTO galaxy VALUES( 3, "galaxy4", 100000 );


DROP PROCEDURE IF EXISTS adduser;
DELIMITER $$
CREATE PROCEDURE adduser( IN ruid INT, IN rgid TINYINT )
BEGIN
	START TRANSACTION;
	INSERT INTO user VALUES( ruid, rgid );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );
	INSERT INTO ship VALUES( NULL, ruid, rgid, FLOOR( RAND() * 96 + 5 ) );

	INSERT INTO slog VALUES( NULL, ruid, CONCAT( "User #", ruid, " created ships on Galaxy #", rgid ), NULL );
	COMMIT;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS attack;
DELIMITER $$
CREATE PROCEDURE attack( IN ruid INT, OUT ratk INT, OUT rgid TINYINT )
BEGIN
	START TRANSACTION;
	SELECT COALESCE( SUM(atk), 0 ) INTO ratk FROM ship WHERE uid = ruid;
	SELECT FLOOR( RAND() * 4 ) INTO rgid;
	IF ISNULL( ratk ) THEN
		SET ratk = 0;
	ELSE
		INSERT INTO slog VALUES( NULL, ruid, CONCAT( "User #", ruid, " attacks ", ratk, " damage to Galaxy #", rgid ), NULL );
	END IF;
	COMMIT;
END $$
DELIMITER ;

