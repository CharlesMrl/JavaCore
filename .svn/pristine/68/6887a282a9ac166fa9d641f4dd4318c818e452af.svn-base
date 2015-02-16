
drop table users;
drop table games;
drop table friendships;
drop table moves;

create table users (
	id int AUTO_INCREMENT,
	name varchar(50),
	surname varchar(50),
	country varchar(30),
	city varchar(50),
	PRIMARY KEY( id )
);

create table games (
	id int AUTO_INCREMENT,
	fen varchar(100) DEFAULT 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
        uidb int,
	uidw int,
	winner int NULL,
	PRIMARY KEY( id )
);

create table friendships (
	id int AUTO_INCREMENT,
	uid1 int,
	uid2 int,
	PRIMARY KEY( id )
);

create table moves(
	id int AUTO_INCREMENT,
	pid int,
	fen varchar(100) DEFAULT 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
	type varchar(10),
        pos1 varchar(2),
	pos2 varchar(2),
	uid int,
	time datetime,
	PRIMARY KEY( id )
);

insert into users VALUES (NULL,'Pascard','Bastien','France','Nogent-Sur-Marne');
insert into users VALUES (NULL,'Denefle','Lucas','France','Boulbi');
insert into users VALUES (NULL,'Syoen','Louis','France','Paris');
insert into users VALUES (NULL,'Mariller','Charles','France','Colombes');
insert into users VALUES (NULL,'Sanchiz','Loic','France','Paris');

insert into friendships VALUES (NULL,1,3);
insert into friendships VALUES (NULL,3,1);
insert into friendships VALUES (NULL,1,5);
insert into friendships VALUES (NULL,5,1);
insert into friendships VALUES (NULL,2,3);
insert into friendships VALUES (NULL,3,2);
insert into friendships VALUES (NULL,2,4);
insert into friendships VALUES (NULL,4,2);
insert into friendships VALUES (NULL,3,5);
insert into friendships VALUES (NULL,5,3);
insert into friendships VALUES (NULL,4,5);
insert into friendships VALUES (NULL,5,4);

insert into games (uidb,uidw) VALUES (1,4);
insert into moves (pid,uid,type,time) VALUES (1,1,'start','2014-11-20 12:00:00');