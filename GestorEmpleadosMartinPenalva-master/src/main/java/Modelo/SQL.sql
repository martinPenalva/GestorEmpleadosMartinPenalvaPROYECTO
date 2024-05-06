create database gestorEmpleados;
use  gestorEmpleados;
create table Empleados(
id int primary key auto_increment ,
nombre varchar(50) not null,
puesto varchar(50)not null,
salario int not null,
fecha date not null
);
drop table Empleados;
drop database gestorEmpleados;