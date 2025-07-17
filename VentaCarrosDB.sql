-- Crear la base de datos
-- drop database if exists VentaCarrosDB;
create database VentaCarrosDB;
use VentaCarrosDB;

-- Tabla de Clientes
create table Clientes(
    idCliente int auto_increment,
    nombre varchar(64) not null,
    apellido varchar(64) not null,
    telefono varchar(16) not null,
    correo varchar(128) not null,
    nit varchar(16) not null,
    estado varchar(32) default "Activo",
    contraseña varchar(64) not null,
    constraint pk_clientes primary key (idCliente)
);

-- Tabla de Productos
create table Productos(
    idProducto int auto_increment,
    nombre varchar(60) not null,
    precio decimal(10,2) not null,
    descripcion varchar(80) not null,
    stock int,
    estado varchar(32) default "Existente",
    constraint pk_productos primary key (idProducto)
);

-- Tabla de Compras
create table Compras(
    idCompra int auto_increment,
    fechaCompra date not null,
    idProducto int not null,
    subtotal decimal(10,2) not null,
    constraint pk_compras primary key (idCompra),
    constraint fk_compras_productos foreign key (idProducto)
        references Productos(idProducto)
);

create table Administradores (
    idAdministrador int auto_increment,
    nombre varchar(64) not null,
    apellido varchar(64) not null,
    telefono varchar(16) not null,
    correo varchar(128) not null,
    nit varchar(16) not null,
    estado varchar(32) default "Activo",
    contraseña varchar(64) not null,
    constraint pk_Administradores primary key (idAdministrador)
);
-- =================================== Procedimientos Almacenados==========================================
-- ==========Clientes============

delimiter //
	create procedure sp_listarClientes()
		begin
			select *
            from Clientes;
		end//
delimiter ;


delimiter //
	create procedure sp_agregarClientes(
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
			begin
					insert into Clientes(nombre, apellido, telefono, correo, nit, contraseña)
                    values(p_nombre, p_apellido, p_telefono, p_correo, p_nit, p_contraseña);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarClientes(
		in p_idCliente int,
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
		begin
			update Clientes C
				set
					C.nombre = p_nombre,
                    C.apellido = p_apellido,
                    C.telefono = p_telefono,
                    C.correo = p_correo,
                    C.nit = p_nit,
                    C.contraseña = p_contraseña
				where C.idCliente = p_idCliente;
        end//
delimiter ;

delimiter //
	create procedure sp_eliminarClientes(in p_idCliente int)
		begin
			update Clientes C
				set
					C.estado = "Inactivo"
				where C.idCliente = p_idCliente;
        end//
delimiter ;

-- ==========Productos==============

delimiter //
	create procedure sp_listarProductos()
		begin
			select *
            from Productos;
		end//
delimiter ;

delimiter //
	create procedure sp_agregarProductos(
		in p_nombre varchar(60),
        in p_precio decimal(10,2),
        in p_descripcion varchar(80),
        in p_stock int)
			begin
					insert into Productos(nombre, precio, descripcion, stock)
                    values(p_nombre, p_precio, p_descripcion, p_stock);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarProductos(
		in p_idProducto int,
		in p_nombre varchar(60),
        in p_precio decimal(10,2),
        in p_descripcion varchar(80),
        in p_stock int)
			begin
				update Productos P
					set
						P.nombre = p_nombre,
                        P.precio = p_precio,
                        P.descripcion = p_descripcion,
                        P.stock = p_stock
					where P.idProducto = p_idProducto;
            end//
delimiter ;

delimiter //
	create procedure sp_eliminarProductos(in p_idProducto int)
		begin
			update Productos P
            set
				P.estado = "Sin Existencias",
                P.stock = 0
			where P.idProducto = p_idProducto;
        end//
delimiter ;

-- ===================Compras============================
delimiter //
	create procedure sp_listarCompras()
		begin
			select *
            from Compras;
		end//
delimiter ;

delimiter //
	create procedure sp_agregarCompras(
		in p_fechaCompra date,
        in p_idProducto int,
        in p_subtotal decimal(10,2))
			begin
					insert into Compras(fechaCompra, idProducto, subtotal)
                    values(p_fechaCompra, p_idProducto, p_subtotal);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarCompras(
		in p_idCompra int,
		in p_fechaCompra date,
        in p_idProducto int,
        in p_subtotal decimal(10,2))
			begin
				update Compras C
					set
						C.fechaCompra = p_fechaCompra ,
                        C.idProducto = p_idProducto ,
                        C.subtotal = p_subtotal
					where C.idCompra = p_idCompra;
            end//
delimiter ;

-- ==========Administrador============

delimiter //
	create procedure sp_listarAdministradores()
		begin
			select *
            from Administradores;
		end//
delimiter ;


delimiter //
	create procedure sp_agregarAdministradores(
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
			begin
					insert into Administradores(nombre, apellido, telefono, correo, nit, contraseña)
                    values(p_nombre, p_apellido, p_telefono, p_correo, p_nit, p_contraseña);
            end//
delimiter ;


delimiter //
	create procedure sp_actualizarAdministradores(
		in p_idAdministrador int,
		in p_nombre varchar(64),
        in p_apellido varchar(64),
        in p_telefono varchar(16),
        in p_correo varchar(126),
        in p_nit varchar(16),
        in p_contraseña varchar(64))
		begin
			update Administradores A
				set
					A.nombre = p_nombre,
                    A.apellido = p_apellido,
                    A.telefono = p_telefono,
                    A.correo = p_correo,
                    A.nit = p_nit,
                    A.contraseña = p_contraseña
				where A.idAdministrador = p_idAdministrador;
        end//
delimiter ;

delimiter //
	create procedure sp_eliminarAdministradores(in p_idAdministrador int)
		begin
			update Administradores A
				set
					A.estado = "Inactivo"
				where A.idAdministrador = p_idAdministrador;
        end//
delimiter ;

