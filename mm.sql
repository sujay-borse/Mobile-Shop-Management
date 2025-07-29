create database MobileShopDB;
use MobileShopDB;

create table mobiles(id int primary key auto_increment,brand varchar(90),model varchar(50),price double,stock int);
select * from mobiles;