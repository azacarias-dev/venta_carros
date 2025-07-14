# VentaCarros

Aplicación de escritorio construida con Java y JavaFX que simula una tienda de ventas de carros. Permite la gestión básica de clientes, productos (vehículos) y compras.

## Tecnologías Utilizadas

- Java 21
- JavaFX 23.0.2
- MySQL 5.1.47
- CSS para el diseño de interfaces
- Maven como sistema de construcción

---

## Características

- Registro e inicio de sesión de clientes.
- Gestión de clientes (Agregar, listar, actualizar, "eliminar" lógicamente).
- Gestión de productos (carros): alta, baja, modificación y listado.
- Registro y visualización de compras.
- Estilo visual moderno y responsivo.

---

## Requisitos

- JDK 21+
- Maven 3+
- Servidor MySQL activo

---

## Configuración Inicial de la Base de Datos

La aplicación depende de una base de datos llamada `VentaCarrosDB`. Puedes crearla con el siguiente script:

```sql
-- Crear la base de datos
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

```

## Ejecución del Proyecto

1. Clona el repositorio o descarga el código fuente.
2. Asegúrate de tener configurado tu entorno (Java y Maven).
3. Crea la base de datos con el script anterior.
4. Configura las credenciales de la conexión en la clase `Conexion.java`:

```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/VentaCarrosDB?userSSL=False";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

5. Ejecuta con Maven o desde tu IDE:

```bash
clean javafx:run
```

---

## Primeros Pasos en la Aplicación

1. Registrarse como nuevo cliente.
2. Iniciar sesión con los datos proporcionados.
3. Acceder a las diferentes funcionalidades del sistema:

   - Registrar productos.
   - Realizar compras.
   - Gestionar información de clientes y productos.

---

## Estructura del Proyecto (Simplificada)

```
ventaCarros/
├── src/
│   ├── main/java/org/ventacarros
│   │   ├── controller/
│   │   ├── database/
│   │   ├── model/
│   │   └── system/
│   ├── resources/view/
│   └── resources/css/
├── pom.xml
└── README.md
```

---

## Autor

- Proyecto académico desarrollado por \Alessandro Zacarias \Javier Gomez \Miguel Tamat, para fines educativos.

---
