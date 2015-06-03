create database if not exists `sbs`;

USE `sbs`;

DROP TABLE IF EXISTS `onetimepasswords`;
DROP TABLE IF EXISTS `transactions`;
DROP TABLE IF EXISTS `modificationrequests`;
DROP TABLE IF EXISTS `deleteaccount`;
DROP TABLE IF EXISTS `user_keys`;
DROP TABLE IF EXISTS `pendingtransactions`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `account`;
DROP TABLE IF EXISTS `user_attempts`;
DROP TABLE IF EXISTS `user_tickets`;
DROP TABLE IF EXISTS `usersPII`;

CREATE  TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  confirmpassword VARCHAR(60) NOT NULL,
  firstname VARCHAR(45) NOT NULL,
  lastname VARCHAR(45) NOT NULL,
  sex VARCHAR(10) NOT NULL,
  MerchantorIndividual VARCHAR(45) NOT NULL,
  phonenumber VARCHAR(45) NOT NULL,
  email VARCHAR(45) NOT NULL,
  SSN VARCHAR(45) NOT NULL,
  address VARCHAR(45) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  userExpired TINYINT NOT NULL DEFAULT 1 ,
  userLocked TINYINT NOT NULL DEFAULT 1 ,
  userDetailsExpired TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (username));
  

CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
  


CREATE TABLE account (
	username VARCHAR(45) NOT NULL,
	accountnumber int(11) NOT NULL AUTO_INCREMENT,
	accountbalance float(45) NOT NULL,
    debit float(45) NOT NULL,
    credit float(45) NOT NULL,
	PRIMARY KEY (accountnumber))
;

CREATE TABLE pendingtransactions (
id INT(11) NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
amount float(45) NOT NULL,
pending TINYINT NOT NULL DEFAULT 1,
accountnumberfrom int(11) NOT NULL,
accountnumberto int(11) NOT NULL,
billpay TINYINT NOT NULL DEFAULT 0,
PRIMARY KEY (id),
KEY fk_username_idx (username),
CONSTRAINT fk_username_pending FOREIGN KEY (username) REFERENCES users (username)
);



CREATE TABLE user_attempts (
  id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  attempts VARCHAR(45) NOT NULL,
  lastModified datetime NOT NULL,
  PRIMARY KEY (id)
);

CREATE  TABLE modificationrequests (
id INT(11) NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
firstname VARCHAR(45) NOT NULL, 
lastname VARCHAR(45) NOT NULL, 
sex VARCHAR(10) NOT NULL,  
MerchantorIndividual VARCHAR(45) NOT NULL,  
phonenumber VARCHAR(45) NOT NULL,  
email VARCHAR(45) NOT NULL, 
address VARCHAR(45) NOT NULL, 
requestcompleted TINYINT NOT NULL DEFAULT 0, 
PRIMARY KEY (id));

CREATE TABLE deleteaccount (
user_keys_id int(11) NOT NULL AUTO_INCREMENT,
username VARCHAR(45) NOT NULL,
deleteaccount TINYINT NOT NULL,
PRIMARY KEY(user_keys_id));

CREATE TABLE user_keys (
  user_keys_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  userKey blob NOT NULL,
  PRIMARY KEY (user_keys_id));
  
CREATE TABLE user_tickets (
  id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  requestcompleted TINYINT NOT NULL DEFAULT 0, 
  requestapproved TINYINT NOT NULL DEFAULT 0, 
  requestrejected TINYINT NOT NULL DEFAULT 0,
  requesttype VARCHAR(10) NOT NULL, 
  PRIMARY KEY (id)
);

CREATE TABLE transactions (
	id INT(11) NOT NULL,	
	usernamefrom VARCHAR(45) NOT NULL,
	usernameto VARCHAR(45) NOT NULL,
	usernamefromaccountnumber INT(11) NOT NULL,
	usernametoaccountnumber INT(11) NOT NULL,
	transactiontype VARCHAR(45) NOT NULL,
	userdelete TINYINT NOT NULL,
	transactiondate timestamp
);

CREATE TABLE onetimepasswords (
	email VARCHAR(45) NOT NULL,
	otp VARCHAR(8),
	dateandtime timestamp
);

CREATE  TABLE usersPII (
  username VARCHAR(45) NOT NULL,
  authorized TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (username));

  INSERT INTO users 
  VALUES ('admin', '$2a$10$aUUBBHgMXlx.UrQLuIlK4.YX2R0hMU10tWdtF0d.Z3Dn5bHVJLrx.', '$2a$10$aUUBBHgMXlx.UrQLuIlK4.YX2R0hMU10tWdtF0d.Z3Dn5bHVJLrx.', 'admin', 'admin', 'male','Merchant', '4808452326',
  'skulkar9@asu.edu', 'ssn', 'address', true,true,true,true);
  INSERT INTO users 
  VALUES ('shivam', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', 'shivam', 'shivam','male', 'Merchant', '4804804801',
  'shivam@asu.edu', 'ssn', 'address', true,true,true,true);
  INSERT INTO users 
  VALUES ('skgarg', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', 'shivam', 'shivam','male', 'Merchant', '4804804801',
  'shivam@asu.edu', 'ssn', 'address', true,true,true,true);
  INSERT INTO users 
  VALUES ('skgarg1', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', '$2a$10$hRzs1QvAQ.LYHRZLohvjJuCow9BKLQ0MXVuTv9YxpBvNVt87NbBDK', 'shivam', 'shivam','male', 'Merchant', '4804804801',
  'shivam@asu.edu', 'ssn', 'address', true,true,true,true);
  INSERT INTO users 
  VALUES ('employee', '$2a$10$G5HnxB7yjAEdxUQ4lDwkHuSPAoaITmM7O2DFp762bpytmUlisTTxa', '$2a$10$G5HnxB7yjAEdxUQ4lDwkHuSPAoaITmM7O2DFp762bpytmUlisTTxa', 'employee', 'employee','male', 'Merchant', '4804804802',
  'employee@asu.edu', 'ssn', 'address', true,true,true,true);

  INSERT INTO user_roles (username, role)
VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO user_roles (username, role)
VALUES ('shivam', 'ROLE_USER');
INSERT INTO user_roles (username, role)
VALUES ('skgarg', 'ROLE_USER');
INSERT INTO user_roles (username, role)
VALUES ('employee', 'ROLE_EMPLOYEE');
  
insert into account values ("skgarg","12345678","500","0","0");
insert into account values ("shivam","98765432","500","0","0");

insert into usersPII values ("shivam",true);
insert into usersPII values ("skgarg",false);

INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype) VALUES ("skgarg",false,false,false,"Modify");
INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype) VALUES ("shivam",false,false,false,"Modify");
INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype) VALUES ("skgarg",true,false,true,"Modify");
INSERT into user_tickets(username, requestcompleted, requestapproved, requestrejected, requesttype) VALUES ("skgarg1",true,true,false,"Modify");
