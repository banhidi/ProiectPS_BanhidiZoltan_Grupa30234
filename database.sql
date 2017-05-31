create database PhotoSharing;

use PhotoSharing;

drop table User;

create table if not exists User(
	Id int auto_increment,
    Name varchar(256) not null,
	Username varchar(64) unique,
    Password varchar(256) not null,
    Type enum('admin', 'regular'),
    primary key(Id));

insert into User(Name, Username, Password, Type) values
	("Banhidi Zoltan", "banhidi", "44e72a9b72031ab7d49c9b4468af1d3cd5c3adfb4236dc69ef2af47c5b322dd9", 'admin'), 
    ("Nagy Noemi", "noemi_cica", "a4b97d192b96c578828fafffb5aff7dd4754dee67c59aff21d89212f3c40cfb7", 'regular'), 
    ("Banhidi Attila", "attila", "790c82d0485610b5ffa261754969d36c2789138e47b257a424102091b372e857", 'regular');
    
create table if not exists UserData(
	Id int auto_increment, 
    UserIdA int,
    UserIdB int,
    DataBlob mediumblob not null,
    Description text,
    Type enum('image', 'text', 'other'),
    LastModified DateTime,
    primary key(Id),
    foreign key(UserIdA) references User(Id),
    foreign key(UserIdB) references User(Id));
    
insert into Image(FromUserId, ToUserId, Image, Message) values
	(18, 20, 'C:/Users/banhidi/Pictures/out.bmp', "Nonci");
    
    
create table if not exists Message(
    FromUserId int,
    ToUserId int, 
	MessageDateTime DateTime, 
    Message text,
    foreign key(FromUserId) references User(Id),
    foreign key(ToUserId) references User(Id));
    
insert into UserData(UserIdA, UserIdB, DataBlob, Description, Type, LastModified) values
	(18, 22, "C:\Users\banhidi\Pictures\20160501_134822.jpg"userdata, 'Noemi foto', 'image', now());
select load_file('C:\Users\banhidi\Pictures\20160501_134822.jpg');
    