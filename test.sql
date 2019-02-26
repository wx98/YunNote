# Host: rm-uf6k5d9yj8dt4h0z3lo.mysql.rds.aliyuncs.com  (Version 5.7.18-log)
# Date: 2018-11-10 18:12:14
# Generator: MySQL-Front 6.1  (Build 1.26)


#create database test;
#use test;

#
# Structure for table "user"
#


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `UID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL DEFAULT '',
  `Password` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;


#
# Structure for table "udata"
#

DROP TABLE IF EXISTS `udata`;
CREATE TABLE `udata` (
  `UData` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) NOT NULL DEFAULT '0',
  `Sex` varchar(255) DEFAULT NULL,
  `Birthday` date DEFAULT NULL,
  `RDate` date DEFAULT NULL,
  PRIMARY KEY (`UData`),
  KEY `UID` (`UID`),
  CONSTRAINT `udata_ibfk_1` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

#
# Structure for table "ndata"
#

DROP TABLE IF EXISTS `ndata`;
CREATE TABLE `ndata` (
  `Ndata` int(11) NOT NULL AUTO_INCREMENT,
  `UID` int(11) NOT NULL DEFAULT '0',
  `Title` varchar(255) DEFAULT NULL,
  `Data` date DEFAULT NULL,
  `DateTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`Ndata`),
  KEY `UID` (`UID`),
  CONSTRAINT `ndata_ibfk_1` FOREIGN KEY (`UID`) REFERENCES `user` (`UID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# View "user_information"
#

DROP VIEW IF EXISTS `user_information`;
CREATE
  ALGORITHM = TEMPTABLE
  VIEW `user_information`
  AS
  SELECT
    `user`.`UID`,
    `user`.`Name`,
    `udata`.`Sex`,
    `udata`.`Birthday`,
    `udata`.`RDate`
  FROM
    (`user`
      JOIN `udata`)
  WHERE
    (`user`.`UID` = `udata`.`UID`);

#
# View "user_login"
#

DROP VIEW IF EXISTS `user_login`;
CREATE
  ALGORITHM = UNDEFINED
  VIEW `user_login`
  AS
  SELECT
    `UID`, `Name` AS 'UserName'
  FROM
    `user`;

#
# Procedure "User_Id_Information"
#

DROP PROCEDURE IF EXISTS `User_Id_Information`;
CREATE PROCEDURE `User_Id_Information`(IN ID int)
BEGIN
	DECLARE _Id INT(11);
	SET _ID = ID;
	select * from user_information where UID = _ID;

END;

#
# Procedure "User_Insert"
#

DROP PROCEDURE IF EXISTS `User_Insert`;
CREATE PROCEDURE `User_Insert`(
		IN name VARCHAR(255),
		IN password1 VARCHAR(255),
		IN sex VARCHAR(255),
		IN birthday DATE 
)
BEGIN
	DECLARE _name VARCHAR(255);
	DECLARE _password VARCHAR(255);
	DECLARE _sex VARCHAR(255);
	DECLARE _birthday DATE ;
	SET _name = name;
	SET _password = password1;
	SET _sex = sex;
	SET _birthday = birthday;
    INSERT INTO `user`(`Name`,`Password`) values("_name","_password");
    INSERT INTO `udata` (`UID`,`Sex`,`Birthday`,`RDate`) values ((select UID from user where Name = _name),'_sex','_birthday','2018-11-10');
END;
